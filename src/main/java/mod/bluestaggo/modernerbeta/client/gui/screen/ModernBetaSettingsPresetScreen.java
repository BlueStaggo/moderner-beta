//~dotLocation
package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.settings.NameAndDescriptionItem;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
//? if <1.21.9
//import net.minecraft.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModernBetaSettingsPresetScreen extends ModernBetaScreen {
    private static final String TEXT_TITLE = "createWorld.customize.modern_beta.title.preset";
    private static final Component SEARCH_HINT = Component.translatable("createWorld.customize.modern_beta.search")
        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
    private static final Component NO_RESULTS = Component.translatable("createWorld.customize.modern_beta.preset.no_results");

    private final List<Item> items;
    private final List<Item> searchItems;
    private final boolean enableSelect;

    private PresetsListWidget listWidget;
    private Button selectPresetButton;
    private String search = "";

    public ModernBetaSettingsPresetScreen(
        ModernBetaScreen parent,
        List<Item> items,
        List<Item> searchItems,
        boolean enableSelect
    ) {
        super(Component.translatable(TEXT_TITLE), parent, 40, 33);

        this.items = items;
        this.searchItems = searchItems;
        this.enableSelect = enableSelect;
    }

    public static <T extends NameAndDescriptionItem> ModernBetaSettingsPresetScreen fromHolders(
        ModernBetaScreen parent,
        List<Holder<T>> items,
        BiConsumer<ModernBetaSettingsPresetScreen, Holder<T>> onSelected,
        boolean enableSelect
    ) {
        List<Item> listItems = items.stream()
            .map(holder -> item(holder, enableSelect, screen -> onSelected.accept(screen, holder)))
            .toList();

        return new ModernBetaSettingsPresetScreen(
            parent,
            listItems,
            listItems,
            enableSelect
        );
    }

    public static <T extends NameAndDescriptionItem> Item item(
        Holder<T> display,
        boolean selectable,
        Consumer<ModernBetaSettingsPresetScreen> onSelected
    ) {
        Identifier id = display.unwrapKey().orElseThrow().identifier();
        return new Item(id, display.value(), selectable, onSelected);
    }

    @Override
    protected void initHeader(GridLayout headerLayout) {
        headerLayout.columnSpacing(3);
        headerLayout.defaultCellSetting().paddingVertical(3);

        super.initHeader(headerLayout);

        EditBox searchBox = headerLayout.addChild(new EditBox(this.font, 0, 0, 200, 15, Component.empty()), 1, 0);
        searchBox.setHint(SEARCH_HINT);
        searchBox.setValue(this.search);
        searchBox.setResponder(search -> {
            this.search = search;
            this.listWidget.filterEntries(search);
        });
    }
    
    @Override
    protected void init() {
        this.listWidget = new PresetsListWidget();

        super.init();

        this.updateSelectButton(this.listWidget.getSelected() instanceof PresetsListWidget.PresetEntry);
    }

    @Override
    protected void initContent(GridLayout contentLayout) {
        //? if >=1.20.2 {
        this.layout.addToContents(this.listWidget);
        //? } else {
        /*this.addRenderableWidget(this.listWidget);
        *///? }
    }

    @Override
    protected void initFooter(GridLayout footerLayout) {
        GridLayout.RowHelper row = footerLayout.createRowHelper(2);

        this.selectPresetButton = Button.builder(
            Component.translatable("createWorld.customize.presets.select"),
            onPress -> {
                PresetsListWidget.PresetEntry entry = this.listWidget.getSelected();

                if (entry != null) {
                    entry.item.onSelect().accept(this);
                }
            }
        ).size(150, 20).build();

        Button cancelButton = Button.builder(
            !this.enableSelect ? CommonComponents.GUI_CANCEL : CommonComponents.GUI_BACK,
            button -> {
                if (this.minecraft != null) {
                    this.minecraft.setScreen(this.parent);
                }
            }
        ).size(150, 20).build();

        this.selectPresetButton.active = this.enableSelect;

        row.addChild(this.selectPresetButton);
        row.addChild(cancelButton);
    }

    //? if <1.20.5 {
    /*@Override
    public void renderBackground(GuiGraphicsExtractor graphics) {
    }
    *///?}

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        //? if <1.20.5
        //this.listWidget.extractRenderState(graphics, mouseX, mouseY, delta);
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        //? if >=1.20.5
        this.listWidget.extractRenderState(graphics, mouseX, mouseY, delta);

        if (this.listWidget.children().isEmpty()) {
            graphics.centeredText(
                this.font,
                NO_RESULTS,
                this.width / 2,
                this.layout.getHeaderHeight() + this.layout.getContentHeight() / 2 - 4,
                CommonColors.GRAY
            );
        }
    }

    @Override
    protected void repositionElements() {
        super.repositionElements();
        //? if >=1.20.3 {
        this.listWidget.setSize(this.width, this.layout.getContentHeight());
        this.listWidget.setPosition(0, this.layout.getHeaderHeight());
        //? } else {
        /*int y0 = this.layout.getY() + this.layout.getHeaderHeight();
        int y1 = y0 + this.layout.getContentHeight();

        this.listWidget.updateSize(this.width, this.height, y0, y1);
        *///? }
        //? if >=1.21.4 {
        this.listWidget.refreshScrollAmount();
        //? } else {
        /*this.listWidget.setScrollAmount(this.listWidget.getScrollAmount());
         *///? }
    }

    private void updateSelectButton(boolean hasSelected) {
        this.selectPresetButton.active = hasSelected;
    }

    public record Item(
        Identifier id,
        NameAndDescriptionItem display,
        boolean selectable,
        Consumer<ModernBetaSettingsPresetScreen> onSelect
    ) {}

    private class PresetsListWidget extends ObjectSelectionList<PresetsListWidget.PresetEntry> {
        private static final int ITEM_HEIGHT = 60;
        private static final int ICON_SIZE = 56;

        public PresetsListWidget() {
            //? if >=1.20.2 {
            super(
                ModernBetaSettingsPresetScreen.this.minecraft,
                ModernBetaSettingsPresetScreen.this.width,
                ModernBetaSettingsPresetScreen.this.layout.getContentHeight(),
                ModernBetaSettingsPresetScreen.this.layout.getHeaderHeight(),
                ITEM_HEIGHT
            );
            //?} else {
            /*super(
                ModernBetaSettingsPresetScreen.this.minecraft,
                ModernBetaSettingsPresetScreen.this.width,
                ModernBetaSettingsPresetScreen.this.height,
                ModernBetaSettingsPresetScreen.this.layout.getHeaderHeight(),
                ModernBetaSettingsPresetScreen.this.height - ModernBetaSettingsPresetScreen.this.layout.getHeaderHeight(),
                ITEM_HEIGHT
            );
            *///?}

            this.filterEntries(ModernBetaSettingsPresetScreen.this.search);
        }

        private void filterEntries(String filter) {
            Item selectedItem = this.getSelected() == null ? null : this.getSelected().item;
            String query = filter.trim().toLowerCase(Locale.ROOT);

            this.clearEntries();
            List<Item> source = query.isEmpty()
                ? ModernBetaSettingsPresetScreen.this.items
                : ModernBetaSettingsPresetScreen.this.searchItems;

            source.stream()
                .map(PresetEntry::new)
                .filter(entry -> entry.matches(query))
                .forEach(this::addEntry);

            if (selectedItem != null) {
                this.setSelected(this.children().stream()
                    .filter(entry -> Objects.equals(entry.item, selectedItem))
                    .findFirst()
                    .orElse(null));
            }

            this.setScrollAmount(0);
        }
        
        @Override
        public void setSelected(PresetEntry entry) {
            super.setSelected(entry);

            ModernBetaSettingsPresetScreen.this.updateSelectButton(entry != null && entry.item.selectable());
        }

        private static final int SCROLLBAR_X_OFFSET = 30;
        //? if >=1.21.4 {
        @Override
        protected int scrollBarX() {
            return super.scrollBarX() + SCROLLBAR_X_OFFSET;
        }
        //?} else {
        /*@Override
        protected int getScrollbarPosition() {
            return super.getScrollbarPosition() + SCROLLBAR_X_OFFSET;
        }
        *///?}

        @Override
        public int getRowWidth() {
            return super.getRowWidth() + 85;
        }
        
        private class PresetEntry extends ObjectSelectionList.Entry<PresetEntry> {
            //? if >=1.20.2 {
            private static final Identifier TEXTURE_JOIN = VersionCompat.vanillaId("world_list/join");
            private static final Identifier TEXTURE_JOIN_HIGHLIGHTED =  VersionCompat.vanillaId("world_list/join_highlighted");
            //?} else {
            /*private static final Identifier TEXTURE_WORLD_SELECT = new Identifier("textures/gui/world_selection.png");
            private static final int TEXTURE_WORLD_SELECT_ATLAS_SIZE = 256;
            private static final int TEXTURE_WORLD_SELECT_SIZE= 32;
            *///?}
            
            private static final int TEXT_SPACING = 11;
            private static final int TEXT_LENGTH = 240;
            
            private final Identifier presetTexture;
            private final Component presetTitle;
            private final Component presetDesc;
            private final String searchText;

            protected final Item item;

            //? if <1.21.9
            //private long time;
            
            public PresetEntry(Item item) {
                this.item = item;
                this.presetTexture = item.display().getTextureLocation(item.id());
                this.presetTitle = item.display().makeOrGetTitleComponent(item.id());
                this.presetDesc = item.display().makeOrGetDescriptionComponent(item.id());
                this.searchText = (this.presetTitle.getString() + " " + item.id())
                    .toLowerCase(Locale.ROOT);
            }

            private boolean matches(String query) {
                return query.isEmpty() || this.searchText.contains(query);
            }

            @Override
            public @NotNull Component getNarration() {
                return Component.empty();
            }

            @Override
            public void
            //? if >=26.1 {
            extractContent
            //? } else if >=1.21.9 {
            /*renderContent
            *///? } else {
            /*render
            *///? }
            (GuiGraphicsExtractor graphics,
                //? if <1.21.9
                //int index, int y, int x, int entryWidth, int entryHeight,
                int mouseX, int mouseY, boolean hovered, float tickDelta) {
                //? if >=1.21.9 {
                int x = this.getContentX();
                int y = this.getContentY();
                //?}

                List<FormattedCharSequence> presetDescTexts = this.splitText(font, this.presetDesc);

                int textStartX = x + ICON_SIZE + 3;
                int textStartY = 1;
                
                graphics.text(font, this.presetTitle, textStartX, y + textStartY, CommonColors.WHITE, false);
                
                int descSpacing = TEXT_SPACING + textStartY + 1;
                for (FormattedCharSequence line : presetDescTexts) {
                    graphics.text(font, line, textStartX, y + descSpacing, CommonColors.GRAY, false);
                    descSpacing += TEXT_SPACING;
                }

                this.draw(graphics, x, y, this.presetTexture);

                if (/*? <26.2 {*/minecraft.options.touchscreen().get() || /*?}*/ hovered) {
                    boolean isMouseHovering = (mouseX - x) < ICON_SIZE;

                    graphics.fill(x, y, x + ICON_SIZE, y + ICON_SIZE, -1601138544);
                    //? if >=1.20.2 {
                    Identifier texture = isMouseHovering ? TEXTURE_JOIN_HIGHLIGHTED : TEXTURE_JOIN;
                    graphics.blitSprite(
                        //? if >=1.21.6 {
                        net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
                        //?} else if >=1.21.2 {
                        /*net.minecraft.client.renderer.RenderType::guiTextured,
                        *///?}
                        texture,
                        x,
                        y,
                        ICON_SIZE,
                        ICON_SIZE
                    );
                    //?} else {
                    /*float v = isMouseHovering ? TEXTURE_WORLD_SELECT_SIZE : 0;
                    graphics.blit(
                        TEXTURE_WORLD_SELECT,
                        x,
                        y,
                        ICON_SIZE,
                        ICON_SIZE,
                        0.0f,
                        v,
                        TEXTURE_WORLD_SELECT_SIZE,
                        TEXTURE_WORLD_SELECT_SIZE,
                        TEXTURE_WORLD_SELECT_ATLAS_SIZE,
                        TEXTURE_WORLD_SELECT_ATLAS_SIZE
                    );
                    *///?}
                }
            }
            
            @Override
            public boolean mouseClicked(/*? if <1.21.9 {*/ /*double mouseX, double mouseY, int button *//*?} else {*/ net.minecraft.client.input.MouseButtonEvent click, boolean doubleClick /*?}*/) {
                //? if >=1.21.9 {
                double mouseX = click.x();
                double mouseY = click.y();

                int button = click.button();
                //?}

                if (button != 0) {
                    return false;
                }

                PresetsListWidget.this.setSelected(this);
                
                if (mouseX - PresetsListWidget.this.getRowLeft() <= ICON_SIZE ||
                        /*? >=1.21.9 {*/ doubleClick /*?} else {*/ /*Util.getMillis() - this.time < 250L *//*?}*/) {
                    minecraft.getSoundManager().play(
                        SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f)
                    );

                    this.item.onSelect().accept(ModernBetaSettingsPresetScreen.this);
                }

                //? if <1.21.9
                //this.time = Util.getMillis();
                
                return true;
            }

            @Override
            public boolean keyPressed(/*? >=1.21.9 {*/ net.minecraft.client.input.KeyEvent event /*? } else {*/ /*int keyCode, int scanCode, int modifiers *//*? }*/) {
                if (/*? >=1.21.9 {*/ event.isSelection() /*? } else {*/ /*net.minecraft.client.gui.navigation.CommonInputs.selected(keyCode) *//*? }*/) {
                    minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                    this.item.onSelect().accept(ModernBetaSettingsPresetScreen.this);
                }

                return super.keyPressed(/*? >=1.21.9 {*/ event /*? } else {*/ /*keyCode, scanCode, modifiers *//*? }*/);
            }


            private void draw(GuiGraphicsExtractor graphics, int x, int y, Identifier textureId) {
                graphics.blit(
                    //? if >= 1.21.6 {
                    net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
                    //?} else if >=1.21.2 {
                    /*net.minecraft.client.renderer.RenderType::guiTextured,
                    *///?}
                    textureId,
                    x, y,
                    0.0f, 0.0f,
                    ICON_SIZE, ICON_SIZE,
                    ICON_SIZE, ICON_SIZE
                );
            }
            
            private List<FormattedCharSequence> splitText(Font textRenderer, Component text) {
                return textRenderer.split(text, TEXT_LENGTH);
            }
        }
    }
}
