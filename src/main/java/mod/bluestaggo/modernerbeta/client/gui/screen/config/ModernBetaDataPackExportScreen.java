package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import com.google.gson.JsonElement;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.gui.FileDialog;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaScreen;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaSettingsPresetScreen;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.tags.ModernBetaSettingsPresetCategoryTags;
import mod.bluestaggo.modernerbeta.util.ModernBetaDataPack;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ModernBetaDataPackExportScreen extends ModernBetaScreen {
    public static final int TEXT_BOX_LENGTH = 227;
    
    private static final String TEXT_PRESET_CATEGORY = "createWorld.customize.modern_beta.preset_category";
    private static final String TEXT_PRESET_CATEGORY_NAME = "createWorld.customize.modern_beta.preset_category.name";

    private static final String DATA_PACK_EXPORT_PRESET_ID = "createWorld.customize.modern_beta.settings.data_pack_export.preset_id";
    private static final String DATA_PACK_EXPORT_PRESET_NAME = "createWorld.customize.modern_beta.settings.data_pack_export.preset_name";
    private static final String DATA_PACK_EXPORT_PRESET_DESCRIPTION = "createWorld.customize.modern_beta.settings.data_pack_export.preset_description";

    private static final String DATA_PACK_EXPORT = "createWorld.customize.modern_beta.settings.data_pack_export.export";
    private static final String DATA_PACK_EXPORT_SAVE_AS_TITLE = "createWorld.customize.modern_beta.settings.data_pack_export.save_as_title";

    private static final Logger LOGGER = LoggerFactory.getLogger(ModernerBeta.MOD_NAME);

    private final Registry<ModernBetaSettingsPreset> presetRegistry;
    private final Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry;
    private final RegistryAccess registries;

    private final ModernBetaSettingsPreset preset;
    private Identifier presetID;
    private String presetName = "";
    private String presetDescription = "";
    private Holder<ModernBetaSettingsPresetCategory> presetCategory;

    private EditBox idBox;
    private EditBox nameBox;
    private MultiLineEditBox descriptionBox;

    private Button exportButton;
    private Button categoryButton;

    public ModernBetaDataPackExportScreen(
        String title,
        Screen parent,
        ModernBetaSettingsPreset preset,
        RegistryAccess registries
    ) {
        super(Component.translatable(title), parent);
        this.layout.setContentMarginTop(0);

        this.preset = preset;
        this.presetRegistry = registries
                //? if >=1.21.2 {
                .lookupOrThrow
                //? } else {
                /*.registryOrThrow
                *///? }
                        (ModernBetaResourceKeys.SETTINGS_PRESET);
        this.presetCategoryRegistry = registries
                //? if >=1.21.2 {
                .lookupOrThrow
                //? } else {
                /*.registryOrThrow
                *///? }
                        (ModernBetaResourceKeys.SETTINGS_PRESET_CATEGORY);
        this.registries = registries;
    }

    @Override
    protected void init() {
        assert this.minecraft != null;
        if (this.presetID == null)
            this.presetID = getDefaultPresetID();

        super.init();
    }

    @Override
    protected void initContent(GridLayout contentLayout) {
        GridLayout gridWidgetOptions = this.createGridWidget();

        GridLayout.RowHelper mainContent = contentLayout.createRowHelper(1);
        GridLayout.RowHelper optionsContent = gridWidgetOptions.createRowHelper(2);
        optionsContent.defaultCellSetting().alignHorizontallyRight().alignVerticallyMiddle();

        this.categoryButton = Button.builder(
            this.getCategoryButtonLabel(),
            button -> this.minecraft.setScreen(ModernBetaSettingsPresetScreen.fromHolders(
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
                    (screen, category) -> {
                        this.presetCategory = category;
                        this.categoryButton.setMessage(this.getCategoryButtonLabel());

                        screen.onClose();
                    },
                false
            ))
        ).size(BUTTON_LENGTH_PRESET, BUTTON_HEIGHT_PRESET).build();

        Component idText = Component.translatable(DATA_PACK_EXPORT_PRESET_ID);
        StringWidget idLabel = new StringWidget(idText, this.font);
        this.idBox = new EditBox(this.minecraft.fontFilterFishy, 0, 0, TEXT_BOX_LENGTH, 20, Component.empty());
        this.idBox.setValue(this.presetID.toString());
        this.idBox.setResponder(string -> {
            Identifier parsed = Identifier.tryParse(string);

            if (parsed != null) {
                this.idBox.setTextColor(EditBox.DEFAULT_TEXT_COLOR);
            } else {
                this.idBox.setTextColor(0xFFFF0000);
            }

            this.presetID = parsed;
            this.exportButton.active = this.canExport();
        });

        Component nameText = Component.translatable(DATA_PACK_EXPORT_PRESET_NAME);
        StringWidget nameLabel = new StringWidget(nameText, this.font);
        this.nameBox = new EditBox(this.minecraft.fontFilterFishy, 0, 0, TEXT_BOX_LENGTH, 20, Component.empty());
        this.nameBox.setValue(this.presetName);
        this.nameBox.setResponder(string -> {
            this.presetName = string;
            this.exportButton.active = this.canExport();
        });

        Component descriptionText = Component.translatable(DATA_PACK_EXPORT_PRESET_DESCRIPTION);
        StringWidget descriptionLabel = new StringWidget(descriptionText, this.font);
        //? if >=1.21.6 {
        this.descriptionBox = MultiLineEditBox.builder().build(
        //?} else {
        /*this.descriptionBox = new MultiLineEditBox(
         *///?}
            this.font,
            //? if <1.21.6
            //0, 0,
            TEXT_BOX_LENGTH, 60,
            //? if <1.21.6
            //Component.literal(""),
            Component.literal("")
        );
        this.descriptionBox.setValue(this.presetDescription);
        this.descriptionBox.setCharacterLimit(175);
        this.descriptionBox.setValueListener(string -> {
            this.presetDescription = string;
            this.exportButton.active = this.canExport();
        });

        mainContent.addChild(categoryButton);
        mainContent.addChild(gridWidgetOptions);

        optionsContent.addChild(idLabel);
        optionsContent.addChild(idBox);

        optionsContent.addChild(nameLabel);
        optionsContent.addChild(nameBox);

        LayoutSettings layoutSettings = optionsContent.newCellSettings().alignVerticallyTop().paddingTop(6);
        optionsContent.addChild(descriptionLabel, 1, layoutSettings);
        optionsContent.addChild(descriptionBox, 1, layoutSettings.copy().paddingTop(0));
    }

    @Override
    protected void initFooter(GridLayout footerLayout) {
        GridLayout.RowHelper footerContent = footerLayout.createRowHelper(2);

        this.exportButton = Button.builder(Component.translatable(DATA_PACK_EXPORT), button -> {
            Language language = Language.getInstance();
            String title = language.getOrDefault(DATA_PACK_EXPORT_SAVE_AS_TITLE);

            File path = new File(this.minecraft.gameDirectory, presetID.getPath());
            FileDialog.saveFileDialog(
                this.minecraft.getWindow(),
                title,
                path.toPath(),
                List.of(new FileDialog.Filter(".zip", "ZIP file")),
                this::exportDatapack
            );
        }).size(BUTTON_LENGTH, BUTTON_HEIGHT).build();
        this.exportButton.active = false;

        Button widgetCancel = Button.builder(CommonComponents.GUI_CANCEL, button ->
            this.minecraft.setScreen(this.parent)
        ).size(BUTTON_LENGTH, BUTTON_HEIGHT).build();


        footerContent.addChild(this.exportButton);
        footerContent.addChild(widgetCancel);
    }

    private Identifier getDefaultPresetID() {
        assert this.minecraft != null;

        String playerName = this.minecraft.getUser().getName().toLowerCase(Locale.ROOT);
        return VersionCompat.id(playerName, "custom_preset");
    }

    private boolean canExport() {
        return this.presetID != null && !this.nameBox.getValue().isEmpty() &&
                !this.descriptionBox.getValue().isEmpty();
    }

    private Component getCategoryButtonLabel() {
        MutableComponent category = Component.translatable(TEXT_PRESET_CATEGORY).append(": ");
        category.append(this.presetCategory == null ?
            Component.translatable("gui.none").withStyle(ChatFormatting.YELLOW) :
            //~ if >=1.21.11 '.location' -> '.identifier'
            presetCategory.value().makeOrGetTitleComponent(presetCategory.unwrapKey().orElseThrow().identifier())
        );

        return category;
    }

    private void exportDatapack(boolean hasResult, String outputPath) {
        if (!hasResult)
            return;

        try (DataPackExporter exporter = new DataPackExporter(outputPath)) {
            if (this.presetCategory != null) {
                TagKey<ModernBetaSettingsPreset> tagKey = presetCategory.value().presetTag();

                FileToIdConverter converter = FileToIdConverter.json(VersionCompat.tagsDirPath(ModernBetaResourceKeys.SETTINGS_PRESET));
                Identifier pathLocation = converter.idToFile(tagKey.location());

                TagFile tagFile = new TagFile(List.of(TagEntry.element(this.presetID)), false);
                exporter.addJson(ModernBetaDataPack.encode(tagFile, TagFile.CODEC), "data", pathLocation);
            }

            Identifier pathLocation = ModernBetaDataPack.getPresetPath(this.presetID);
            ModernBetaSettingsPreset expanded = ModernBetaDataPack.expandPreset(
                this.preset, this.presetRegistry/*? if <1.21.2 {*//*.asLookup()*//*?}*/,
                Component.literal(this.presetName).withStyle(ChatFormatting.YELLOW),
                Component.literal(this.presetDescription)
            );
            JsonElement encodedPreset = ModernBetaDataPack.encode(
                expanded,
                ModernBetaSettingsPreset.CODEC,
                this.registries
            );
            exporter.addJson(encodedPreset, "data", pathLocation);

            exporter.addJson(
                ModernBetaDataPack.createMetadata(Component.translatable("pack.moderner_beta.exported_preset")),
                "pack.mcmeta"
            );
        } catch (Exception e) {
            LOGGER.error("Failed to export datapack!", e);
        }
    }

    private static class DataPackExporter implements AutoCloseable {
        private final ZipOutputStream stream;

        public DataPackExporter(String path) throws IOException {
            this.stream = new ZipOutputStream(new FileOutputStream(path));
        }

        private void addJson(JsonElement json, String parent, Identifier path) throws IOException {
            this.addJson(json, parent, path.getNamespace() + "/" + path.getPath());
        }

        private void addJson(JsonElement json, String path) throws IOException {
            this.addJson(json, "", path);
        }

        private void addJson(JsonElement json, String parent, String path) throws IOException {
            ZipEntry entry = new ZipEntry((!parent.isEmpty() ? parent + "/" : "") + path);
            this.stream.putNextEntry(entry);
            this.stream.write(ModernBetaDataPack.toBytes(json));
            this.stream.closeEntry();
        }

        @Override
        public void close() throws IOException {
            this.stream.close();
        }
    }
}
