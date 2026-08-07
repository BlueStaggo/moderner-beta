//~registryOr
//~dotLocation
package mod.bluestaggo.modernerbeta.client.gui.screen;

import com.mojang.datafixers.util.Pair;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.ModernBetaDataPackExportScreen;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.graphical.ModernBetaGraphicalProviderSettingsScreen;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.json.ModernBetaImportExportScreen;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.json.ModernBetaSettingsScreen;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSavedPresetPack;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresets;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.tags.ModernBetaSettingsPresetCategoryTags;
import mod.bluestaggo.modernerbeta.util.LoggingUtil;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.slf4j.event.Level;

import java.io.IOException;
import java.nio.file.Path;

import java.util.List;

public class ModernBetaWorldScreen extends ModernBetaScreen {
    private static final String TEXT_TITLE = "createWorld.customize.modern_beta.title"; 
    private static final String TEXT_TITLE_CHUNK = "createWorld.customize.modern_beta.title.chunk"; 
    private static final String TEXT_TITLE_BIOME = "createWorld.customize.modern_beta.title.biome"; 
    private static final String TEXT_TITLE_CAVE_BIOME = "createWorld.customize.modern_beta.title.cave_biome"; 
    
    private static final String TEXT_PRESET = "createWorld.customize.modern_beta.preset";
    private static final String TEXT_PRESET_NAME = "createWorld.customize.modern_beta.preset.name";
    private static final String TEXT_PRESET_CUSTOM = "createWorld.customize.modern_beta.preset.custom";
    
    private static final String TEXT_CHUNK = "createWorld.customize.modern_beta.chunk";
    private static final String TEXT_BIOME = "createWorld.customize.modern_beta.biome";
    private static final String TEXT_CAVE_BIOME = "createWorld.customize.modern_beta.cave_biome";

    private static final String TEXT_SETTINGS = "createWorld.customize.modern_beta.settings";
    private static final String TEXT_SETTINGS_JSON = "createWorld.customize.modern_beta.settings.json";
    private static final String TEXT_SETTINGS_IMPORT_EXPORT = "createWorld.customize.modern_beta.settings.import_export";
    private static final String TEXT_SETTINGS_DATA_PACK_EXPORT = "createWorld.customize.modern_beta.settings.data_pack_export";
    private static final String TEXT_SETTINGS_SET_DEFAULT = "createWorld.customize.modern_beta.settings.set_default";
    private static final String TEXT_SETTINGS_DEFAULT_SAVED = "createWorld.customize.modern_beta.settings.default_saved";
    private static final String TEXT_SETTINGS_DEFAULT_SAVE_FAILED = "createWorld.customize.modern_beta.settings.default_save_failed";
    private static final String TEXT_SETTINGS_RESET = "createWorld.customize.modern_beta.settings.reset";
    private static final String TEXT_SETTINGS_RESET_MESSAGE = "createWorld.customize.modern_beta.settings.reset.message";
    private static final String TEXT_SETTINGS_PREVIEW = "createWorld.customize.modern_beta.settings.preview";
    private static final String TEXT_HINT_SETTINGS = "createWorld.customize.modern_beta.hint.settings";

    private final OnSettingsSave onDone;
    private final WorldCreationContext context;
    private final Registry<ModernBetaSettingsPreset> presetRegistry;
    private final Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry;

    private ModernBetaSettingsPreset preset;
    private Button buttonPreset;
    private Button buttonSetDefault;

    public ModernBetaWorldScreen(Screen parent, WorldCreationContext context, OnSettingsSave onDone) {
        super(Component.translatable(TEXT_TITLE), parent, 33, 40);
        this.layout.setContentMarginTop(0);
        
        ChunkGenerator chunkGenerator = context.selectedDimensions().overworld();
        ModernBetaChunkGenerator modernBetaChunkGenerator = (ModernBetaChunkGenerator)chunkGenerator;
        ModernBetaBiomeSource modernBetaBiomeSource = (ModernBetaBiomeSource)modernBetaChunkGenerator.getBiomeSource();

        this.presetRegistry = context.worldgenLoadContext().lookupOrThrow(ModernBetaResourceKeys.SETTINGS_PRESET);
        this.presetCategoryRegistry = context.worldgenLoadContext().lookupOrThrow(ModernBetaResourceKeys.SETTINGS_PRESET_CATEGORY);

        this.onDone = onDone;
        
        this.preset = new ModernBetaSettingsPreset(
            modernBetaChunkGenerator.getChunkSettings(),
            modernBetaBiomeSource.getBiomeSettings(),
            modernBetaBiomeSource.getCaveBiomeSettings()
        );
        this.context = context;
    }
    
    public void setPreset(ModernBetaSettingsPreset preset) {
        this.preset = preset;
        this.buttonPreset.setMessage(this.getPresetButtonLabel());
        this.buttonSetDefault.setMessage(Component.translatable(TEXT_SETTINGS_SET_DEFAULT));
    }

    @Override
    protected void initContent(GridLayout contentLayout) {
        GridLayout gridWidgetPreset = new GridLayout();
        GridLayout gridWidgetSettings = this.createGridWidget();
        GridLayout gridWidgetActions = this.createGridWidget();

        GridLayout.RowHelper mainRows = contentLayout.createRowHelper(1);
        GridLayout.RowHelper presetRow = gridWidgetPreset.createRowHelper(2);
        GridLayout.RowHelper settingsRows = gridWidgetSettings.createRowHelper(3);
        GridLayout.RowHelper actionRow = gridWidgetActions.createRowHelper(2);
        presetRow.defaultCellSetting().paddingHorizontal(2);
        settingsRows.defaultCellSetting().alignVerticallyMiddle();

        this.buttonPreset = Button.builder(
            this.getPresetButtonLabel(),
            button -> this.minecraft.setScreen(this.createPresetScreen())
        ).size(BUTTON_LENGTH_PRESET, BUTTON_HEIGHT_PRESET).build();

        this.buttonSetDefault = Button.builder(
            Component.translatable(TEXT_SETTINGS_SET_DEFAULT),
            this::saveDefaultPreset
        ).size(100, BUTTON_HEIGHT).build();

        HolderGetter<ModernBetaSettingsPreset> presetLookup = this.getPresetLookup();

        Button buttonChunk = Button.builder(
            Component.translatable(TEXT_SETTINGS),
            button -> {
                ModernBetaSettings settings = this.preset.chunkSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::chunkSettings);

                this.minecraft.setScreen(new ModernBetaGraphicalProviderSettingsScreen(
                    TEXT_TITLE_CHUNK,
                    this,
                    this.context,
                    settings.toCompound(),
                    settings,
                    nbtCompound -> {
                        Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setNbt(this.context.worldgenLoadContext(), nbtCompound, null, null);
                        this.setPreset(updatedPreset.getFirst());
                    },
                    ModernBetaRegistries.CHUNK
                ));
            }
        ).build();

        Button buttonChunkAdvanced = Button.builder(
            Component.translatable(TEXT_SETTINGS_JSON),
            button -> this.minecraft.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.preset.chunkSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::chunkSettings),
                this.context.worldgenLoadContext(),
                string -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset =
                            this.preset.setJson(this.context.worldgenLoadContext(), string, "", "");
                    this.setPreset(updatedPreset.getFirst());
                }
            ))
        ).size(20, 20).build();

        Button buttonBiome = Button.builder(
            Component.translatable(TEXT_SETTINGS),
            button -> {
                ModernBetaSettings settings = this.preset.biomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::biomeSettings);

                this.minecraft.setScreen(new ModernBetaGraphicalProviderSettingsScreen(
                    TEXT_TITLE_CHUNK,
                    this,
                    this.context,
                    settings.toCompound(),
                    settings,
                    nbtCompound -> {
                        Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setNbt(this.context.worldgenLoadContext(), null, nbtCompound, null);
                        this.setPreset(updatedPreset.getFirst());
                    },
                    ModernBetaRegistries.BIOME
                ));
            }
        ).build();

        Button buttonBiomeAdvanced = Button.builder(
            Component.translatable(TEXT_SETTINGS_JSON),
            button -> this.minecraft.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_BIOME,
                this,
                this.preset.biomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::biomeSettings),
                this.context.worldgenLoadContext(),
                string -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset =
                            this.preset.setJson(this.context.worldgenLoadContext(), "", string, "");
                    this.setPreset(updatedPreset.getFirst());
                }
            ))
        ).size(20, 20).build();

        Button buttonCaveBiome = Button.builder(
            Component.translatable(TEXT_SETTINGS),
            button -> {
                ModernBetaSettings settings = this.preset.caveBiomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::caveBiomeSettings);

                this.minecraft.setScreen(new ModernBetaGraphicalProviderSettingsScreen(
                    TEXT_TITLE_CHUNK,
                    this,
                    this.context,
                    settings.toCompound(),
                    settings,
                    nbtCompound -> {
                        Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setNbt(this.context.worldgenLoadContext(), null, null, nbtCompound);
                        this.setPreset(updatedPreset.getFirst());
                    },
                    ModernBetaRegistries.CAVE_BIOME
                ));
            }
        ).build();

        Button buttonCaveBiomeAdvanced = Button.builder(
            Component.translatable(TEXT_SETTINGS_JSON),
            button -> this.minecraft.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_CAVE_BIOME,
                this,
                this.preset.caveBiomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::caveBiomeSettings),
                this.context.worldgenLoadContext(),
                string -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset =
                            this.preset.setJson(this.context.worldgenLoadContext(), "", "", string);
                    this.setPreset(updatedPreset.getFirst());
                }
            ))
        ).size(20, 20).build();

        Button buttonReset = Button.builder(
            Component.translatable(TEXT_SETTINGS_RESET),
            button -> this.minecraft.setScreen(new ConfirmScreen(
                confirmed -> {
                    if (confirmed) {
                        this.resetPreset();
                    }

                    this.minecraft.setScreen(this);
                },
                Component.empty(),
                Component.translatable(TEXT_SETTINGS_RESET_MESSAGE),
                Component.translatable(TEXT_SETTINGS_RESET),
                CommonComponents.GUI_CANCEL
            ))
        ).build();

        Button buttonPreview = Button.builder(
            Component.translatable(TEXT_SETTINGS_PREVIEW),
            button -> this.minecraft.setScreen(new ModernBetaBiomePreviewScreen(
                Component.translatable(TEXT_SETTINGS_PREVIEW),
                this,
                this.context,
                this.preset.biomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::biomeSettings)
            ))
        ).build();

        Button importExportButton = Button.builder(Component.translatable(TEXT_SETTINGS_IMPORT_EXPORT), button ->
            this.minecraft.setScreen(new ModernBetaImportExportScreen(TEXT_SETTINGS_IMPORT_EXPORT, this, this.preset, this.context.worldgenLoadContext(), str -> {
                Pair<ModernBetaSettingsPreset, Boolean> read = ModernBetaSettingsPreset.fromJson(this.context.worldgenLoadContext(), str);

                if (read.getSecond())
                    this.setPreset(read.getFirst());
            }))
        ).build();

        Button dataPackExportButton = Button.builder(Component.translatable(TEXT_SETTINGS_DATA_PACK_EXPORT), button ->
            this.minecraft.setScreen(new ModernBetaDataPackExportScreen(TEXT_SETTINGS_DATA_PACK_EXPORT, this, this.preset, this.context.worldgenLoadContext()))
        ).pos(0, 20).build();

        presetRow.addChild(this.buttonPreset);
        presetRow.addChild(this.buttonSetDefault);

        mainRows.addChild(gridWidgetPreset);
        mainRows.addChild(gridWidgetSettings);
        mainRows.addChild(gridWidgetActions);

        this.addGridTextButtonTriplet(settingsRows, TEXT_CHUNK, buttonChunk, buttonChunkAdvanced);
        this.addGridTextButtonTriplet(settingsRows, TEXT_BIOME, buttonBiome, buttonBiomeAdvanced);
        this.addGridTextButtonTriplet(settingsRows, TEXT_CAVE_BIOME, buttonCaveBiome, buttonCaveBiomeAdvanced);

        actionRow.addChild(buttonReset);
        actionRow.addChild(buttonPreview);

        actionRow.addChild(importExportButton);
        actionRow.addChild(dataPackExportButton);
    }

    private ModernBetaSettingsPresetScreen createPresetScreen() {
        List<Holder<ModernBetaSettingsPresetCategory>> categories =
            getTag(this.presetCategoryRegistry, ModernBetaSettingsPresetCategoryTags.SELECTABLE);

        List<ModernBetaSettingsPresetScreen.Item> categoryItems = categories.stream()
            .map(category -> ModernBetaSettingsPresetScreen.item(
                category,
                false,
                screen -> this.minecraft.setScreen(ModernBetaSettingsPresetScreen.fromHolders(
                    screen,
                    this.getPresets(category),
                    (parentScreen, preset) -> this.selectPreset(preset),
                    true
                ))
            ))
            .toList();
        List<ModernBetaSettingsPresetScreen.Item> presetItems = categories.stream()
            .flatMap(category -> this.getPresets(category).stream())
            .distinct()
            .map(preset -> ModernBetaSettingsPresetScreen.item(
                preset,
                true,
                screen -> this.selectPreset(preset)
            ))
            .toList();

        return new ModernBetaSettingsPresetScreen(
            this,
            categoryItems,
            presetItems,
            false
        );
    }

    private List<Holder<ModernBetaSettingsPreset>> getPresets(
        Holder<ModernBetaSettingsPresetCategory> category
    ) {
        return getTag(this.presetRegistry, category.value().presetTag());
    }

    private static <T> List<Holder<T>> getTag(Registry<T> registry, TagKey<T> tag) {
        return registry
            //? if >=1.21.2 {
            .getOrThrow
            //?} else {
            /*.getOrCreateTag
            *///?}
            (tag)
            .stream()
            .toList();
    }

    private void selectPreset(Holder<ModernBetaSettingsPreset> preset) {
        this.setPreset(ModernBetaSettingsPreset.referenced(preset));

        while (this.minecraft.screen instanceof ModernBetaSettingsPresetScreen screen) {
            this.minecraft.setScreen(screen.parent);
        }
    }

    @Override
    protected void initFooter(GridLayout footerLayout) {
        GridLayout gridWidgetActions = this.createGridWidget();

        GridLayout.RowHelper mainRow = footerLayout.createRowHelper(1);
        GridLayout.RowHelper actionRow = gridWidgetActions.createRowHelper(2);

        Component hintText = Component.translatable(TEXT_HINT_SETTINGS).withStyle(ChatFormatting.GRAY);
        int hintTextWidth = this.font.width(hintText.getVisualOrderText());
        int hintTextHeight = this.font.lineHeight;

        mainRow.addChild(new StringWidget(
            hintTextWidth,
            hintTextHeight,
            hintText,
            this.font
        ));
        mainRow.addChild(gridWidgetActions);

        Button doneButton = Button.builder(CommonComponents.GUI_DONE, button -> {
            this.onDone.onSave(
                this.preset.chunkSettings(),
                this.preset.biomeSettings(),
                this.preset.caveBiomeSettings()
            );
            this.minecraft.setScreen(this.parent);
        }).size(BUTTON_LENGTH, BUTTON_HEIGHT).build();

        Button cancelButton = Button.builder(CommonComponents.GUI_CANCEL, button ->
            this.minecraft.setScreen(this.parent)
        ).size(BUTTON_LENGTH, BUTTON_HEIGHT).build();

        actionRow.addChild(doneButton);
        actionRow.addChild(cancelButton);
    }

    private void resetPreset() {
        Identifier presetId = ModernerBeta.getDefaultPresetId();
        ModernBetaSettingsPreset preset = ModernBetaSettingsPreset
            .getPreset(presetId, this.getPresetLookup())
            .orElse(null);
        if (preset == null) {
            Identifier fallbackId = ModernBetaSettingsPresets.BETA_1_7_3.identifier();
            LoggingUtil.log(Level.WARN, "Default preset {} is unavailable; using {}", presetId, fallbackId);
            presetId = fallbackId;
            preset = ModernBetaSettingsPreset.getPreset(presetId, this.getPresetLookup()).orElse(null);
        }
        this.setPreset(preset == null ?
            ModernBetaSettingsPreset.referenced(presetId) :
            ModernBetaSettingsPreset.referencedWithMetadata(presetId, preset));
    }

    private void saveDefaultPreset(Button button) {
        Identifier presetId = this.getDirectPresetId();

        if (presetId == null) {
            Path configDir = ModernerBeta.getConfigDir();
            if (configDir != null) {
                try {
                    presetId = ModernBetaSavedPresetPack.save(
                        configDir,
                        this.preset,
                        this.getPresetLookup(),
                        this.context.worldgenLoadContext()
                    );
                } catch (IOException | RuntimeException exception) {
                    LoggingUtil.log(Level.ERROR, "Failed to save custom default preset", exception);
                }
            }
        }

        boolean saved = presetId != null && ModernerBeta.setDefaultSettingsPreset(presetId);
        button.setMessage(Component.translatable(
            saved ? TEXT_SETTINGS_DEFAULT_SAVED : TEXT_SETTINGS_DEFAULT_SAVE_FAILED
        ));
    }

    private HolderGetter<ModernBetaSettingsPreset> getPresetLookup() {
        //? if >=1.21.2 {
        return this.presetRegistry;
        //?} else {
        /*return this.presetRegistry.asLookup();
        *///?}
    }

    private Identifier getDirectPresetId() {
        return this.preset.asList().stream().allMatch(settings -> settings.size() == 1) ?
            this.getPresetId() :
            null;
    }

    private Identifier getPresetId() {
        Identifier presetId = null;
        for (ModernBetaSettings settings : this.preset.asList()) {
            Identifier settingsPresetId = settings.resolveDefaultPreset().get(SettingsComponentTypes.PRESET);

            if (settingsPresetId == null || presetId != null && !presetId.equals(settingsPresetId)) {
                return null;
            }
            presetId = settingsPresetId;
        }
        return presetId;
    }

    private Component getPresetButtonLabel() {
        Component name = this.preset.presetName().orElse(null);
        if (name == null) {
            Identifier presetId = this.getPresetId();
            name = presetId == null ?
                Component.translatable(TEXT_PRESET_CUSTOM).withStyle(ChatFormatting.AQUA) :
                ModernBetaSettingsPreset.getPreset(presetId, this.getPresetLookup())
                    .map(preset -> preset.makeOrGetTitleComponent(presetId))
                    .orElseGet(() -> Component.translatable(
                        TEXT_PRESET_NAME + "." + presetId.toLanguageKey()
                    ).withStyle(ChatFormatting.YELLOW));
        }
        return Component.translatable(TEXT_PRESET).append(": ").append(name);
    }

    public interface OnSettingsSave {
        void onSave(ModernBetaSettings chunkSettings, ModernBetaSettings biomeSettings, ModernBetaSettings caveBiomeSettings);
    }
}
