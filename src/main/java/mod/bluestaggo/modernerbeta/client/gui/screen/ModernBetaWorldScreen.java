//~registryOr
package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.ModernBetaGraphicalProviderSettingsScreen;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.tags.ModernBetaSettingsPresetCategoryTags;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Optional;
import java.util.Random;

@Environment(EnvType.CLIENT)
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
    private static final String TEXT_SETTINGS_RESET = "createWorld.customize.modern_beta.settings.reset";
    private static final String TEXT_SETTINGS_RESET_MESSAGE = "createWorld.customize.modern_beta.settings.reset.message";
    private static final String TEXT_SETTINGS_PREVIEW = "createWorld.customize.modern_beta.settings.preview";
    //private static final String TEXT_INVALID_SETTINGS = "createWorld.customize.modern_beta.invalid_settings";
    
    private static final String[] TEXT_HINTS = new String[] {
        "createWorld.customize.modern_beta.hint.settings"
    };
    
    private final TriConsumer<CompoundTag, CompoundTag, CompoundTag> onDone;
    private final String hintString;
    private final WorldCreationContext context;
    private final Registry<ModernBetaSettingsPreset> presetRegistry;
    private final Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry;

    private ModernBetaSettingsPreset preset;
    private Button buttonPreset;

    public ModernBetaWorldScreen(Screen parent, WorldCreationContext context, TriConsumer<CompoundTag, CompoundTag, CompoundTag> onDone) {
        super(Component.translatable(TEXT_TITLE), parent);
        
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
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
            this.onDone.accept(
                this.preset.chunkSettings().toCompound(),
                this.preset.biomeSettings().toCompound(),
                this.preset.caveBiomeSettings().toCompound()
            );
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 - 154, this.height - 26, BUTTON_LENGTH, BUTTON_HEIGHT).build());
        
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> 
            this.minecraft.setScreen(this.parent)
        ).bounds(this.width / 2 + 4, this.height - 26, BUTTON_LENGTH, BUTTON_HEIGHT).build());
        
        Component hintText = Component.translatable(this.hintString).withStyle(ChatFormatting.GRAY);
        int hintTextWidth = this.font.width(hintText.getVisualOrderText());
        int hintTextHeight = this.font.lineHeight;
        
        this.addRenderableWidget(new StringWidget(
            this.width / 2 - hintTextWidth / 2,
            this.height - 46,
            hintTextWidth,
            hintTextHeight,
            hintText,
            this.font
        ));

        MutableComponent presetText = Component.translatable(TEXT_PRESET).append(": ");
        ResourceLocation presetKey = this.getPresetKey();
        presetText.append(presetKey == null ?
            Component.translatable(TEXT_PRESET_CUSTOM).withStyle(ChatFormatting.AQUA) :
            Component.translatable(TEXT_PRESET_NAME + "." + presetKey.toLanguageKey()).withStyle(ChatFormatting.YELLOW)
        );

        this.buttonPreset = Button.builder(
            presetText,
            button -> this.minecraft.setScreen(new ModernBetaSettingsPresetScreen(
                this,
                this.presetRegistry,
                this.presetCategoryRegistry,
                this.presetCategoryRegistry
                    //? if >=1.21.2 {
                    .getOrThrow
                    //?} else {
                    /*.getOrCreateTag
                    *///?}
                    (ModernBetaSettingsPresetCategoryTags.SELECTABLE)
                    .stream()
                    .map(Holder::unwrapKey)
                    .flatMap(Optional::stream)
                    .map(ResourceKey::location)
                    .toList(),
                this.preset,
                true
            ))
        ).bounds(0, 0, BUTTON_LENGTH_PRESET, BUTTON_HEIGHT_PRESET).build();

        HolderGetter<ModernBetaSettingsPreset> presetLookup =
            //? if >=1.21.2 {
            this.presetRegistry;
            //?} else {
            /*this.presetRegistry.asLookup();
            *///?}

        Button buttonChunk = Button.builder(
            Component.translatable(TEXT_SETTINGS),
            button -> this.minecraft.setScreen(new ModernBetaGraphicalProviderSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.context,
                this.preset.chunkSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::chunkSettings).toCompound(),
                nbtCompound -> {
                    Tuple<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setNbt(nbtCompound, null, null, presetLookup);
                    this.preset = updatedPreset.getA();
                },
                ModernBetaRegistries.CHUNK
            ))
        ).build();

        Button buttonChunkAdvanced = Button.builder(
            Component.translatable(TEXT_SETTINGS_JSON),
            button -> this.minecraft.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.preset.chunkSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::chunkSettings),
                string -> {
                    Tuple<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setJson(string, "", "");
                    this.preset = updatedPreset.getA();
                }
            ))
        ).size(20, 20).build();

        Button buttonBiome = Button.builder(
            Component.translatable(TEXT_SETTINGS),
            button -> this.minecraft.setScreen(new ModernBetaGraphicalProviderSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.context,
                this.preset.biomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::biomeSettings).toCompound(),
                nbtCompound -> {
                    Tuple<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setNbt(null, nbtCompound, null, presetLookup);
                    this.preset = updatedPreset.getA();
                },
                ModernBetaRegistries.BIOME
            ))
        ).build();

        Button buttonBiomeAdvanced = Button.builder(
            Component.translatable(TEXT_SETTINGS_JSON),
            button -> this.minecraft.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_BIOME,
                this,
                this.preset.biomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::biomeSettings),
                string -> {
                    Tuple<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setJson("", string, "");
                    this.preset = updatedPreset.getA();
                }
            ))
        ).size(20, 20).build();

        Button buttonCaveBiome = Button.builder(
            Component.translatable(TEXT_SETTINGS),
            button -> this.minecraft.setScreen(new ModernBetaGraphicalProviderSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.context,
                this.preset.caveBiomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::caveBiomeSettings).toCompound(),
                nbtCompound -> {
                    Tuple<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setNbt(null, null, nbtCompound, presetLookup);
                    this.preset = updatedPreset.getA();
                },
                ModernBetaRegistries.CAVE_BIOME
            ))
        ).build();

        Button buttonCaveBiomeAdvanced = Button.builder(
            Component.translatable(TEXT_SETTINGS_JSON),
            button -> this.minecraft.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_CAVE_BIOME,
                this,
                this.preset.caveBiomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::caveBiomeSettings),
                string -> {
                    Tuple<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setJson("", "", string);
                    this.preset = updatedPreset.getA();
                }
            ))
        ).size(20, 20).build();
        
        Button buttonReset = Button.builder(
            Component.translatable(TEXT_SETTINGS_RESET),
            button -> this.minecraft.setScreen(new ModernBetaSettingsConfirmScreen(
                this,
                this::resetPreset,
                Component.translatable(TEXT_SETTINGS_RESET_MESSAGE),
                Component.translatable(TEXT_SETTINGS_RESET)
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

        GridLayout gridWidgetMain = this.createGridWidget();
        GridLayout gridWidgetSettings = this.createGridWidget();
        GridLayout gridWidgetActions = this.createGridWidget();

        GridLayout.RowHelper gridAdderMain = gridWidgetMain.createRowHelper(1);
        GridLayout.RowHelper gridAdderSettings = gridWidgetSettings.createRowHelper(3);
        GridLayout.RowHelper gridAdderActions = gridWidgetActions.createRowHelper(2);
        gridAdderSettings.defaultCellSetting().alignVerticallyMiddle();

        gridAdderMain.addChild(this.buttonPreset);
        gridAdderMain.addChild(gridWidgetSettings);
        gridAdderMain.addChild(gridWidgetActions);
        
        this.addGridTextButtonTriplet(gridAdderSettings, TEXT_CHUNK, buttonChunk, buttonChunkAdvanced);
        this.addGridTextButtonTriplet(gridAdderSettings, TEXT_BIOME, buttonBiome, buttonBiomeAdvanced);
        this.addGridTextButtonTriplet(gridAdderSettings, TEXT_CAVE_BIOME, buttonCaveBiome, buttonCaveBiomeAdvanced);

        gridAdderActions.addChild(buttonReset);
        gridAdderActions.addChild(buttonPreview);

        gridWidgetMain.arrangeElements();
        FrameLayout.alignInRectangle(gridWidgetMain, 0, this.overlayTop + 8, this.width, this.height, 0.5f, 0.0f);
        gridWidgetMain.visitWidgets(this::addRenderableWidget);

        this.addRenderableWidget(Button.builder(Component.translatable("FHUCKEIFN "), button ->
            this.minecraft.setScreen(new ModernBetaImportExportScreen(Component.translatable("fsdgs"), this, this.preset, newPreset -> {
                if (newPreset != null)
                    this.preset = newPreset;
            }))).build());
    }

    private void resetPreset() {
        this.preset = ModernBetaSettingsPreset.referenced(ModernBetaSettings.DEFAULT_PRESET_ID);
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
}
