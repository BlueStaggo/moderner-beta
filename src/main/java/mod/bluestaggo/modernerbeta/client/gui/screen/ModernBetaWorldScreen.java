//~registryOr
package mod.bluestaggo.modernerbeta.client.gui.screen;

import com.mojang.datafixers.util.Pair;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.ModernBetaDataPackExportScreen;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.graphical.ModernBetaGraphicalProviderSettingsScreen;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.json.ModernBetaImportExportScreen;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.json.ModernBetaSettingsScreen;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.tags.ModernBetaSettingsPresetCategoryTags;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Random;

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
    private static final String TEXT_SETTINGS_RESET = "createWorld.customize.modern_beta.settings.reset";
    private static final String TEXT_SETTINGS_RESET_MESSAGE = "createWorld.customize.modern_beta.settings.reset.message";
    private static final String TEXT_SETTINGS_PREVIEW = "createWorld.customize.modern_beta.settings.preview";
    
    private static final String[] TEXT_HINTS = new String[] {
        "createWorld.customize.modern_beta.hint.settings"
    };
    
    private final TriConsumer<ModernBetaSettings, ModernBetaSettings, ModernBetaSettings> onDone;
    private final String hintString;
    private final WorldCreationContext context;
    private final Registry<ModernBetaSettingsPreset> presetRegistry;
    private final Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry;

    private ModernBetaSettingsPreset preset;
    private Button buttonPreset;

    public ModernBetaWorldScreen(Screen parent, WorldCreationContext context, TriConsumer<ModernBetaSettings, ModernBetaSettings, ModernBetaSettings> onDone) {
        super(Component.translatable(TEXT_TITLE), parent, 33, 40);
        this.layout.setContentMarginTop(0);
        
        ChunkGenerator chunkGenerator = context.selectedDimensions().overworld();
        ModernBetaChunkGenerator modernBetaChunkGenerator = (ModernBetaChunkGenerator)chunkGenerator;
        ModernBetaBiomeSource modernBetaBiomeSource = (ModernBetaBiomeSource)modernBetaChunkGenerator.getBiomeSource();

        this.presetRegistry = context.worldgenLoadContext().lookupOrThrow(ModernBetaResourceKeys.SETTINGS_PRESET);
        this.presetCategoryRegistry = context.worldgenLoadContext().lookupOrThrow(ModernBetaResourceKeys.SETTINGS_PRESET_CATEGORY);

        this.onDone = onDone;
        this.hintString = TEXT_HINTS[new Random().nextInt(TEXT_HINTS.length)];
        
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
    }

    @Override
    protected void initContent(GridLayout contentLayout) {
        GridLayout gridWidgetSettings = this.createGridWidget();
        GridLayout gridWidgetActions = this.createGridWidget();

        GridLayout.RowHelper mainRows = contentLayout.createRowHelper(1);
        GridLayout.RowHelper settingsRows = gridWidgetSettings.createRowHelper(3);
        GridLayout.RowHelper actionRow = gridWidgetActions.createRowHelper(2);
        settingsRows.defaultCellSetting().alignVerticallyMiddle();

        this.buttonPreset = Button.builder(
            this.getPresetButtonLabel(),
            button -> this.minecraft.setScreen(new ModernBetaSettingsPresetScreen<>(
                this,
                this.presetCategoryRegistry
                    //? if >=1.21.2 {
                    .getOrThrow
                    //?} else {
                    /*.getOrCreateTag
                    *///?}
                    (ModernBetaSettingsPresetCategoryTags.SELECTABLE)
                    .stream()
                    .toList(),
                    (screen, name, preset) -> {
                        this.minecraft.setScreen(new ModernBetaSettingsPresetScreen<>(
                            screen,
                            this.presetRegistry
                                //? if >=1.21.2 {
                                .getOrThrow
                                //?} else {
                                /*.getOrCreateTag
                                 *///?}
                                (preset.presetTag())
                                .stream()
                                .toList(),
                            (parentScreen, presetName, settingsPreset) -> {
                                this.setPreset(ModernBetaSettingsPreset.referenced(presetName));

                                while (this.minecraft.screen instanceof ModernBetaSettingsPresetScreen<?> subPresetScreen) {
                                    this.minecraft.setScreen(subPresetScreen.parent);
                                }
                            },
                            true
                        ));
                    },
                false
            ))
        ).size(BUTTON_LENGTH_PRESET, BUTTON_HEIGHT_PRESET).build();

        HolderGetter<ModernBetaSettingsPreset> presetLookup =
            //? if >=1.21.2 {
            this.presetRegistry;
            //?} else {
            /*this.presetRegistry.asLookup();
            *///?}

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

        mainRows.addChild(this.buttonPreset);
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

    @Override
    protected void initFooter(GridLayout footerLayout) {
        GridLayout gridWidgetActions = this.createGridWidget();

        GridLayout.RowHelper mainRow = footerLayout.createRowHelper(1);
        GridLayout.RowHelper actionRow = gridWidgetActions.createRowHelper(2);

        Component hintText = Component.translatable(this.hintString).withStyle(ChatFormatting.GRAY);
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
            this.onDone.accept(
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
        this.setPreset(ModernBetaSettingsPreset.referenced(
                ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS).defaultSettingsPreset()));
    }

    private ResourceLocation getPresetKey() {
        ResourceLocation presetKey = null;
        for (ModernBetaSettings settings : this.preset.asList()) {
            ResourceLocation subPresetKey = settings.get(SettingsComponentTypes.PRESET);
            if (ModernBetaSettings.DEFAULT_PRESET_ID.equals(subPresetKey)) {
                subPresetKey = ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_MISCELLANEOUS).defaultSettingsPreset();
            }

            if (subPresetKey == null || presetKey != null && !presetKey.equals(subPresetKey)) {
                return null;
            }
            presetKey = subPresetKey;
        }
        return presetKey;
    }

    private Component getPresetButtonLabel() {
        MutableComponent presetText = Component.translatable(TEXT_PRESET).append(": ");
        ResourceLocation presetKey = this.getPresetKey();
        presetText.append(presetKey == null ?
            Component.translatable(TEXT_PRESET_CUSTOM).withStyle(ChatFormatting.AQUA) :
            Component.translatable(TEXT_PRESET_NAME + "." + presetKey.toLanguageKey()).withStyle(ChatFormatting.YELLOW)
        );

        return presetText;
    }
}
