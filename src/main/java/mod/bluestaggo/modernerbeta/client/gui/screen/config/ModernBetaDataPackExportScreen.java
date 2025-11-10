package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaScreen;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaSettingsPresetScreen;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.tags.ModernBetaSettingsPresetCategoryTags;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class ModernBetaDataPackExportScreen extends ModernBetaScreen {
    private static final String TEXT_PRESET_CATEGORY = "createWorld.customize.modern_beta.preset_category";
    private static final String TEXT_PRESET_CATEGORY_NAME = "createWorld.customize.modern_beta.preset_category.name";

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 8, 40);

    private ResourceLocation presetID;
    private String presetName = "";
    private String presetDescription = "";

    private ResourceLocation presetCategory;

    private boolean canExport;

    private final Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry;

    public ModernBetaDataPackExportScreen(String title, Screen parent, Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry) {
        super(Component.translatable(title), parent);

        this.presetCategoryRegistry = presetCategoryRegistry;
    }

    @Override
    protected void init() {
        super.init();

        if (this.presetID == null)
            this.presetID = getDefaultPresetID();

        MutableComponent presetText = Component.translatable(TEXT_PRESET_CATEGORY).append(": ");
        presetText.append(this.presetCategory == null ?
            Component.translatable("gui.none").withStyle(ChatFormatting.AQUA) :
            Component.translatable(TEXT_PRESET_CATEGORY_NAME + "." + presetCategory.toLanguageKey()).withStyle(ChatFormatting.YELLOW)
        );

        Component idText = Component.translatable("Preset ID:");
        StringWidget idLabel = new StringWidget(idText, this.font);
        //noinspection ExtractMethodRecommender
        EditBox idBox = new EditBox(this.minecraft.fontFilterFishy, this.width - 200, 20, Component.empty());
        idBox.setValue(this.presetID.toString());
        idBox.setResponder(string -> {
            ResourceLocation parsed = ResourceLocation.tryParse(string);

            if (parsed != null) {
                this.presetID = parsed;
                this.canExport = true;
                idBox.setTextColor(EditBox.DEFAULT_TEXT_COLOR);
            } else {
                this.canExport = false;
                //noinspection DataFlowIssue
                idBox.setTextColor(ChatFormatting.RED.getColor() | 0xFF000000);
            }
        });

        Component nameText = Component.translatable("Preset Name:");
        StringWidget nameLabel = new StringWidget(nameText, this.font);
        EditBox nameBox = new EditBox(this.minecraft.fontFilterFishy, this.width - 200, 20, Component.empty());
        nameBox.setValue(this.presetName);
        nameBox.setResponder(string -> {
            this.canExport = string.isEmpty();
            this.presetName = string;
        });

        Component descriptionText = Component.translatable("Preset Description:");
        StringWidget descriptionLabel = new StringWidget(descriptionText, this.font);
        //? if >=1.21.6 {
        MultiLineEditBox descriptionBox = MultiLineEditBox.builder().build(
        //?} else {
        /*MultiLineEditBox descriptionBox = new MultiLineEditBox(
         *///?}
            this.font,
            //? if <1.21.6
            /*0, 0,*/
            this.width - 200, 60,
            //? if <1.21.6
            /*Component.literal(""),*/
            Component.literal("")
        );
        descriptionBox.setValue(this.presetDescription);
        descriptionBox.setCharacterLimit(175);
        descriptionBox.setValueListener(string -> {
            this.canExport = string.isEmpty();
            this.presetDescription = string;
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

        Button widgetDone = Button.builder(Component.translatable("Export"), button -> {
            //TODO
        }).bounds(0, 0, BUTTON_LENGTH, BUTTON_HEIGHT).build();

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

        footerContent.addChild(widgetDone);
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
        String playerName = this.minecraft.getUser().getName().toLowerCase(Locale.ROOT);
        return VersionCompat.id(playerName, "custom_preset");
    }
}
