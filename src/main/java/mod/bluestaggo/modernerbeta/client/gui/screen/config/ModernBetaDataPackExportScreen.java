package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaScreen;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaSettingsPresetScreen;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.tags.ModernBetaSettingsPresetCategoryTags;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.DetectedVersion;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.data.DataProvider;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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

    private final ModernBetaSettingsPreset preset;
    private ResourceLocation presetID;
    private String presetName = "";
    private String presetDescription = "";
    private ResourceLocation presetCategory;

    private EditBox idBox;
    private EditBox nameBox;
    private MultiLineEditBox descriptionBox;

    private Button exportButton;
    private Button categoryButton;

    public ModernBetaDataPackExportScreen(
        String title,
        Screen parent,
        ModernBetaSettingsPreset preset,
        Registry<ModernBetaSettingsPreset> presetRegistry,
        Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry
    ) {
        super(Component.translatable(title), parent);
        this.layout.setContentMarginTop(0);

        this.preset = preset;
        this.presetRegistry = presetRegistry;
        this.presetCategoryRegistry = presetCategoryRegistry;
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
                    (screen, category, preset) -> {
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
            ResourceLocation parsed = ResourceLocation.tryParse(string);

            if (parsed != null) {
                this.idBox.setTextColor(EditBox.DEFAULT_TEXT_COLOR);
            } else {
                //noinspection DataFlowIssue
                this.idBox.setTextColor(ChatFormatting.RED.getColor() | 0xFF000000);
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
            String writeTo = getOutputPathFromSelection(path, title, "*.zip");

            this.exportDatapack(writeTo);
        }).size(BUTTON_LENGTH, BUTTON_HEIGHT).build();
        this.exportButton.active = false;

        Button widgetCancel = Button.builder(CommonComponents.GUI_CANCEL, button ->
            this.minecraft.setScreen(this.parent)
        ).size(BUTTON_LENGTH, BUTTON_HEIGHT).build();


        footerContent.addChild(this.exportButton);
        footerContent.addChild(widgetCancel);
    }

    private ResourceLocation getDefaultPresetID() {
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
            Component.translatable("gui.none").withStyle(ChatFormatting.AQUA) :
            Component.translatable(TEXT_PRESET_CATEGORY_NAME + "." + presetCategory.toLanguageKey()).withStyle(ChatFormatting.YELLOW)
        );

        return category;
    }

    private static  <T> JsonElement objectToJson(T value, Codec<T> codec) {
        DataResult<JsonElement> result = codec.encodeStart(JsonOps.INSTANCE, value);
        return VersionCompat.getOrThrow(result);
    }

    private static String getOutputPathFromSelection(File path, String title, String... filterPatterns) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer pointers = stack.mallocPointer(filterPatterns.length);
            for (String pattern : filterPatterns) {
                pointers.put(stack.UTF8(pattern));
                pointers.flip();
            }

            return TinyFileDialogs.tinyfd_saveFileDialog(
                title,
                path.toString(),
                pointers,
                null
            );
        }
    }

    private void exportDatapack(String outputPath) {
        //TODO: maybe make this async?
        if (outputPath == null)
            return;

        try (DataPackExporter exporter = new DataPackExporter(outputPath)) {
            if (this.presetCategory != null) {
                ModernBetaSettingsPresetCategory category = this.presetCategoryRegistry
                        //? if >=1.21.2 {
                        .getValue
                        //? } else {
                        /*.get
                         *///? }
                        (this.presetCategory);
                TagKey<ModernBetaSettingsPreset> tagKey = category.presetTag();

                FileToIdConverter converter = FileToIdConverter.json(VersionCompat.tagsDirPath(ModernBetaResourceKeys.SETTINGS_PRESET));
                ResourceLocation pathLocation = converter.idToFile(tagKey.location());

                TagFile tagFile = new TagFile(List.of(TagEntry.element(this.presetID)), false);
                exporter.addJson(objectToJson(tagFile, TagFile.CODEC), "data", pathLocation);
            }

            FileToIdConverter converter = FileToIdConverter.json(VersionCompat.elementsDirPath(ModernBetaResourceKeys.SETTINGS_PRESET));
            ResourceLocation pathLocation = converter.idToFile(this.presetID);

            ModernBetaSettingsPreset expanded = this.preset
                    .mapped(this.presetRegistry/*? if <1.21.2 {*//*.asLookup()*//*?}*/)
                    .withNameAndDesc(
                        Component.literal(this.presetName).withStyle(ChatFormatting.YELLOW),
                        Component.literal(this.presetDescription)
                    );
            exporter.addJson(objectToJson(expanded, ModernBetaSettingsPreset.CODEC), "data", pathLocation);

            PackMetadataSection metadataSection = new PackMetadataSection(
                //TODO: maybe autogenerated string?
                Component.literal("Moderner Beta exported preset"),
                DetectedVersion.BUILT_IN
                //? if >=1.21.6 {
                .packVersion
                //? } else {
                /*.getPackVersion
                *///? }
                (PackType.SERVER_DATA)
                //? if >=1.21.9
                    //.minorRange()
                //? if >=1.20.2 && <1.21.9
                , Optional.empty()
            );
            JsonElement metadataElement =
                    //? if >=1.20.2 {
                    objectToJson(
                        metadataSection,
                        //? if >=1.21.9 {
                        /*PackMetadataSection.SERVER_TYPE.codec()
                        *///? } else {
                        PackMetadataSection.CODEC
                        //? }
                    );
                    //? } else {
                    /*PackMetadataSection.TYPE.toJson(metadataSection);
                    *///? }
            JsonObject packObject = new JsonObject();
            packObject.add("pack", metadataElement);

            exporter.addJson(packObject, "pack.mcmeta");
        } catch (Exception e) {
            LOGGER.error("Failed to export datapack!", e);
        }
    }

    private static class DataPackExporter implements AutoCloseable {
        private final ZipOutputStream stream;

        public DataPackExporter(String path) {
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(path);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            this.stream = new ZipOutputStream(fileOutputStream);
        }

        private void addJson(JsonElement json, String parent, ResourceLocation path) throws IOException {
            this.addJson(json, parent, path.getNamespace() + "/" + path.getPath());
        }

        private void addJson(JsonElement json, ResourceLocation path) throws IOException {
            this.addJson(json, "", path);
        }

        private void addJson(JsonElement json, String path) throws IOException {
            this.addJson(json, "", path);
        }

        private void addJson(JsonElement json, String parent, String path) throws IOException {
            ZipEntry entry = new ZipEntry((!parent.isEmpty() ? parent + "/" : "") + path);

            byte[] b = new byte[0];
            try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))
            ) {
                jsonWriter.setSerializeNulls(false);
                jsonWriter.setIndent("  ");
                GsonHelper.writeValue(jsonWriter, json, DataProvider.KEY_COMPARATOR);

                jsonWriter.flush();
                b = outputStream.toByteArray();
            } catch (IOException var10) {
                LOGGER.error("Failed to save JSON file {}", path, var10);
            }

            this.stream.putNextEntry(entry);
            this.stream.write(b);
        }

        @Override
        public void close() throws Exception {
            this.stream.close();
        }
    }
}
