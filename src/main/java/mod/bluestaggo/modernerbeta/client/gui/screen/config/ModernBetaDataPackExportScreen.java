package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaScreen;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaSettingsPresetScreen;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.tags.ModernBetaSettingsPresetCategoryTags;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.util.Locale;

public class ModernBetaDataPackExportScreen extends ModernBetaScreen {
    private static final String TEXT_PRESET_CATEGORY = "createWorld.customize.modern_beta.preset_category";
    private static final String TEXT_PRESET_CATEGORY_NAME = "createWorld.customize.modern_beta.preset_category.name";

    private static final String DATA_PACK_EXPORT_PRESET_ID = "createWorld.customize.modern_beta.settings.data_pack_export.preset_id";
    private static final String DATA_PACK_EXPORT_PRESET_NAME = "createWorld.customize.modern_beta.settings.data_pack_export.preset_name";
    private static final String DATA_PACK_EXPORT_PRESET_DESCRIPTION = "createWorld.customize.modern_beta.settings.data_pack_export.preset_description";

    private static final String DATA_PACK_EXPORT = "createWorld.customize.modern_beta.settings.data_pack_export.export";
    private static final String DATA_PACK_EXPORT_SAVE_AS_TITLE = "createWorld.customize.modern_beta.settings.data_pack_export.save_as_title";

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 8, 40);
    private final Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry;

    private ResourceLocation presetID;
    private String presetName = "";
    private String presetDescription = "";
    private ResourceLocation presetCategory;

    private EditBox idBox;
    private EditBox nameBox;
    private MultiLineEditBox descriptionBox;

    private Button exportButton;

    public ModernBetaDataPackExportScreen(String title, Screen parent, Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry) {
        super(Component.translatable(title), parent);

        this.presetCategoryRegistry = presetCategoryRegistry;
    }

    @Override
    protected void init() {
        super.init();

        assert this.minecraft != null;
        if (this.presetID == null)
            this.presetID = getDefaultPresetID();

        MutableComponent presetText = Component.translatable(TEXT_PRESET_CATEGORY).append(": ");
        presetText.append(this.presetCategory == null ?
            Component.translatable("gui.none").withStyle(ChatFormatting.AQUA) :
            Component.translatable(TEXT_PRESET_CATEGORY_NAME + "." + presetCategory.toLanguageKey()).withStyle(ChatFormatting.YELLOW)
        );

        Component idText = Component.translatable(DATA_PACK_EXPORT_PRESET_ID);
        StringWidget idLabel = new StringWidget(idText, this.font);
        this.idBox = new EditBox(this.minecraft.fontFilterFishy, 0, 0, this.width - 200, 20, Component.empty());
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
        this.nameBox = new EditBox(this.minecraft.fontFilterFishy, 0, 0, this.width - 200, 20, Component.empty());
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
            /*0, 0,*/
            this.width - 200, 60,
            //? if <1.21.6
            /*Component.literal(""),*/
            Component.literal("")
        );
        this.descriptionBox.setValue(this.presetDescription);
        this.descriptionBox.setCharacterLimit(175);
        this.descriptionBox.setValueListener(string -> {
            this.presetDescription = string;
            this.exportButton.active = this.canExport();
        });

        Button categoryButton = Button.builder(
            presetText,
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
                        screen.onClose();
                    },
                false
            ))
        ).bounds(0, 0, BUTTON_LENGTH_PRESET, BUTTON_HEIGHT_PRESET).build();

        this.exportButton = Button.builder(Component.translatable(DATA_PACK_EXPORT), button -> {
            //TODO
            Language language = Language.getInstance();
            String title = language.getOrDefault(DATA_PACK_EXPORT_SAVE_AS_TITLE);

            String writeTo;
            File path = this.minecraft.gameDirectory;

            //TODO: investigate behaviour on other operating systems
            if (Util.getPlatform() == Util.OS.WINDOWS) {
                path = new File(path, presetID.getPath());
            }

            try (MemoryStack stack = MemoryStack.stackPush()) {
                PointerBuffer pointers = stack.mallocPointer(1);
                pointers.put(stack.UTF8("*.zip"));
                pointers.flip();

                writeTo = TinyFileDialogs.tinyfd_saveFileDialog(
                    title,
                    path.toString(),
                    pointers,
                    null
                );
            }

            if (writeTo != null) {

            }
        }).bounds(0, 0, BUTTON_LENGTH, BUTTON_HEIGHT).build();
        this.exportButton.active = false;

        Button widgetCancel = Button.builder(CommonComponents.GUI_CANCEL, button ->
                this.minecraft.setScreen(this.parent)
        ).bounds(0, 0, BUTTON_LENGTH, BUTTON_HEIGHT).build();

        GridLayout gridWidgetMain = this.createGridWidget();
        GridLayout gridWidgetOptions = this.createGridWidget();
        GridLayout gridWidgetFooter = this.createGridWidget();

        GridLayout.RowHelper mainContent = gridWidgetMain.createRowHelper(1);
        GridLayout.RowHelper optionsContent = gridWidgetOptions.createRowHelper(2);
        GridLayout.RowHelper footerContent = gridWidgetFooter.createRowHelper(2);
        optionsContent.defaultCellSetting().alignHorizontallyRight().alignVerticallyMiddle();

        mainContent.addChild(categoryButton);
        mainContent.addChild(gridWidgetOptions);

        optionsContent.addChild(idLabel);
        optionsContent.addChild(idBox);

        optionsContent.addChild(nameLabel);
        optionsContent.addChild(nameBox);

        LayoutSettings layoutSettings = optionsContent.newCellSettings().alignVerticallyTop().paddingTop(6);
        optionsContent.addChild(descriptionLabel, 1, layoutSettings);
        optionsContent.addChild(descriptionBox, 1, layoutSettings.copy().paddingTop(0));

        footerContent.addChild(this.exportButton);
        footerContent.addChild(widgetCancel);

        this.layout.addToContents(gridWidgetMain);
        this.layout.addToFooter(gridWidgetFooter);

        this.layout.arrangeElements();
        this.layout.visitWidgets(this::addRenderableWidget);
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
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
}
