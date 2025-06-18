package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.gui.screen.config.ModernBetaGraphicalProviderSettingsScreen;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistryKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.tags.ModernBetaSettingsPresetCategoryTags;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.world.gen.chunk.ChunkGenerator;
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
    
    private final TriConsumer<NbtCompound, NbtCompound, NbtCompound> onDone;
    private final String hintString;
    private final GeneratorOptionsHolder generatorOptionsHolder;
    private final Registry<ModernBetaSettingsPreset> presetRegistry;
    private final Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry;

    private ModernBetaSettingsPreset preset;
    private ButtonWidget buttonPreset;

    public ModernBetaWorldScreen(Screen parent, GeneratorOptionsHolder generatorOptionsHolder, TriConsumer<NbtCompound, NbtCompound, NbtCompound> onDone) {
        super(Text.translatable(TEXT_TITLE), parent);
        
        ChunkGenerator chunkGenerator = generatorOptionsHolder.selectedDimensions().getChunkGenerator();
        ModernBetaChunkGenerator modernBetaChunkGenerator = (ModernBetaChunkGenerator)chunkGenerator;
        ModernBetaBiomeSource modernBetaBiomeSource = (ModernBetaBiomeSource)modernBetaChunkGenerator.getBiomeSource();

        this.presetRegistry = generatorOptionsHolder.getCombinedRegistryManager().getOrThrow(ModernBetaRegistryKeys.SETTINGS_PRESET);
        this.presetCategoryRegistry = generatorOptionsHolder.getCombinedRegistryManager().getOrThrow(ModernBetaRegistryKeys.SETTINGS_PRESET_CATEGORY);

        this.onDone = onDone;
        this.hintString = TEXT_HINTS[new Random().nextInt(TEXT_HINTS.length)];
        
        this.preset = new ModernBetaSettingsPreset(
            modernBetaChunkGenerator.getChunkSettings(),
            modernBetaBiomeSource.getBiomeSettings(),
            modernBetaBiomeSource.getCaveBiomeSettings()
        );
        this.generatorOptionsHolder = generatorOptionsHolder;
    }
    
    public void setPreset(ModernBetaSettingsPreset preset) {
        this.preset = preset;
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            this.onDone.accept(
                this.preset.chunkSettings().toCompound(),
                this.preset.biomeSettings().toCompound(),
                this.preset.caveBiomeSettings().toCompound()
            );
            this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 154, this.height - 26, BUTTON_LENGTH, BUTTON_HEIGHT).build());
        
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> 
            this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 4, this.height - 26, BUTTON_LENGTH, BUTTON_HEIGHT).build());
        
        Text hintText = Text.translatable(this.hintString).formatted(Formatting.GRAY);
        int hintTextWidth = this.textRenderer.getWidth(hintText.asOrderedText());
        int hintTextHeight = this.textRenderer.fontHeight;
        
        this.addDrawableChild(new TextWidget(
            this.width / 2 - hintTextWidth / 2,
            this.height - 46,
            hintTextWidth,
            hintTextHeight,
            hintText,
            this.textRenderer
        ));

        MutableText presetText = Text.translatable(TEXT_PRESET).append(": ");
        Identifier presetKey = this.getPresetKey();
        presetText.append(presetKey == null ?
            Text.translatable(TEXT_PRESET_CUSTOM).formatted(Formatting.AQUA) :
            Text.translatable(TEXT_PRESET_NAME + "." + presetKey.toTranslationKey()).formatted(Formatting.YELLOW)
        );

        this.buttonPreset = ButtonWidget.builder(
            presetText,
            button -> this.client.setScreen(new ModernBetaSettingsPresetScreen(
                this,
                this.presetRegistry,
                this.presetCategoryRegistry,
                this.presetCategoryRegistry
                    //? if >=1.21.2 {
                    .getOrThrow
                    //?} else {
                    /*.getOrCreateEntryList
                    *///?}
                    (ModernBetaSettingsPresetCategoryTags.SELECTABLE)
                    .stream()
                    .map(RegistryEntry::getKey)
                    .flatMap(Optional::stream)
                    .map(RegistryKey::getValue)
                    .toList(),
                this.preset,
                true
            ))
        ).dimensions(0, 0, BUTTON_LENGTH_PRESET, BUTTON_HEIGHT_PRESET).build();

        RegistryEntryLookup<ModernBetaSettingsPreset> presetLookup =
            //? if >=1.21.2 {
            this.presetRegistry;
            //?} else {
            /*this.presetRegistry.getReadOnlyWrapper();
            *///?}

        ButtonWidget buttonChunk = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS),
            button -> this.client.setScreen(new ModernBetaGraphicalProviderSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.generatorOptionsHolder,
                this.preset.chunkSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::chunkSettings).toCompound(),
                nbtCompound -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setNbt(nbtCompound, null, null);
                    this.preset = updatedPreset.getLeft();
                },
                ModernBetaRegistries.CHUNK
            ))
        ).build();

        ButtonWidget buttonChunkAdvanced = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS_JSON),
            button -> this.client.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.preset.chunkSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::chunkSettings),
                string -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setJson(string, "", "");
                    this.preset = updatedPreset.getLeft();
                }
            ))
        ).size(20, 20).build();

        ButtonWidget buttonBiome = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS),
            button -> this.client.setScreen(new ModernBetaGraphicalProviderSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.generatorOptionsHolder,
                this.preset.biomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::biomeSettings).toCompound(),
                nbtCompound -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setNbt(null, nbtCompound, null);
                    this.preset = updatedPreset.getLeft();
                },
                ModernBetaRegistries.BIOME
            ))
        ).build();

        ButtonWidget buttonBiomeAdvanced = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS_JSON),
            button -> this.client.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_BIOME,
                this,
                this.preset.biomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::biomeSettings),
                string -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setJson("", string, "");
                    this.preset = updatedPreset.getLeft();
                }
            ))
        ).size(20, 20).build();

        ButtonWidget buttonCaveBiome = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS),
            button -> this.client.setScreen(new ModernBetaGraphicalProviderSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.generatorOptionsHolder,
                this.preset.caveBiomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::caveBiomeSettings).toCompound(),
                nbtCompound -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setNbt(null, null, nbtCompound);
                    this.preset = updatedPreset.getLeft();
                },
                ModernBetaRegistries.CAVE_BIOME
            ))
        ).build();

        ButtonWidget buttonCaveBiomeAdvanced = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS_JSON),
            button -> this.client.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_CAVE_BIOME,
                this,
                this.preset.caveBiomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::caveBiomeSettings),
                string -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.setJson("", "", string);
                    this.preset = updatedPreset.getLeft();
                }
            ))
        ).size(20, 20).build();
        
        ButtonWidget buttonReset = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS_RESET),
            button -> this.client.setScreen(new ModernBetaSettingsConfirmScreen(
                this,
                this::resetPreset,
                Text.translatable(TEXT_SETTINGS_RESET_MESSAGE),
                Text.translatable(TEXT_SETTINGS_RESET)
            ))
        ).build();

        ButtonWidget buttonPreview = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS_PREVIEW),
            button -> this.client.setScreen(new ModernBetaBiomePreviewScreen(
                Text.translatable(TEXT_SETTINGS_PREVIEW),
                this,
                this.generatorOptionsHolder,
                this.preset.biomeSettings().mapPreset(presetLookup, ModernBetaSettingsPreset::biomeSettings)
            ))
        ).build();

        GridWidget gridWidgetMain = this.createGridWidget();
        GridWidget gridWidgetSettings = this.createGridWidget();
        GridWidget gridWidgetActions = this.createGridWidget();

        GridWidget.Adder gridAdderMain = gridWidgetMain.createAdder(1);
        GridWidget.Adder gridAdderSettings = gridWidgetSettings.createAdder(3);
        GridWidget.Adder gridAdderActions = gridWidgetActions.createAdder(2);
        gridAdderSettings.getMainPositioner().alignVerticalCenter();

        gridAdderMain.add(this.buttonPreset);
        gridAdderMain.add(gridWidgetSettings);
        gridAdderMain.add(gridWidgetActions);
        
        this.addGridTextButtonTriplet(gridAdderSettings, TEXT_CHUNK, buttonChunk, buttonChunkAdvanced);
        this.addGridTextButtonTriplet(gridAdderSettings, TEXT_BIOME, buttonBiome, buttonBiomeAdvanced);
        this.addGridTextButtonTriplet(gridAdderSettings, TEXT_CAVE_BIOME, buttonCaveBiome, buttonCaveBiomeAdvanced);

        gridAdderActions.add(buttonReset);
        gridAdderActions.add(buttonPreview);

        gridWidgetMain.refreshPositions();
        SimplePositioningWidget.setPos(gridWidgetMain, 0, this.overlayTop + 8, this.width, this.height, 0.5f, 0.0f);
        gridWidgetMain.forEachChild(this::addDrawableChild);
    }

    private void resetPreset() {
        this.preset = ModernBetaSettingsPreset.referenced(ModernBetaSettings.DEFAULT_PRESET_ID);
    }

    private Identifier getPresetKey() {
        Identifier presetKey = null;
        for (ModernBetaSettings settings : this.preset.asList()) {
            Identifier subPresetKey = settings.get(SettingsComponentTypes.PRESET);
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
