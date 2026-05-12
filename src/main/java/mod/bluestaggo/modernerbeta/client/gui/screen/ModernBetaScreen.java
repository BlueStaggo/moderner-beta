package mod.bluestaggo.modernerbeta.client.gui.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public abstract class ModernBetaScreen extends Screen {
    public static final int BUTTON_HEIGHT = 20;
    public static final int BUTTON_LENGTH = 150;
    public static final int BUTTON_LENGTH_PRESET = 200;
    public static final int BUTTON_HEIGHT_PRESET = 20;

    protected final ThreePartLayout layout;
    
    protected final Screen parent;
    
    public ModernBetaScreen(Component title, Screen parent) {
        this(title, parent, 33);
    }

    public ModernBetaScreen(Component title, Screen parent, int headerAndFooterHeight) {
        this(title, parent, headerAndFooterHeight, headerAndFooterHeight);
    }

    public ModernBetaScreen(Component title, Screen parent, int headerHeight, int footerHeight) {
        super(title);

        this.parent = parent;
        this.layout = new ThreePartLayout(this, headerHeight, footerHeight);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    //? if <1.20.5 {
    /*@Override
    public void render(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
    }
    *///? }
    
    @Override
    protected void init() {
        GridLayout header = createGridWidget();
        this.initHeader(header);
        this.layout.addToHeader(header);

        GridLayout content = createGridWidget();
        this.initContent(content);
        this.layout.addToContents(content);

        GridLayout footer = createGridWidget();
        this.initFooter(footer);
        this.layout.addToFooter(footer);

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    protected void initHeader(GridLayout headerLayout) {
        headerLayout.addChild(new StringWidget(this.getTitle(), this.font), 0, 0);
    }

    protected abstract void initContent(GridLayout contentLayout);

    protected abstract void initFooter(GridLayout footerLayout);

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    protected GridLayout createGridWidget() {
        GridLayout gridWidget = new GridLayout();
        gridWidget.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter().alignVerticallyTop();
        
        return gridWidget;
    }
    
    protected void addGridTextButtonPair(GridLayout.RowHelper adder, String text, Button buttonWidget) {
        adder.addChild(new StringWidget(Component.translatable(text), this.font));
        adder.addChild(buttonWidget);
    }

    protected void addGridTextButtonTriplet(GridLayout.RowHelper adder, String text, Button buttonWidget, Button buttonWidget2) {
        adder.addChild(new StringWidget(Component.translatable(text), this.font));
        adder.addChild(buttonWidget);
        adder.addChild(buttonWidget2);
    }

    protected static class ThreePartLayout implements Layout {
        private final FrameLayout headerFrame = new FrameLayout();
        private final FrameLayout footerFrame = new FrameLayout();
        private final FrameLayout contentsFrame = new FrameLayout();

        private final Screen screen;
        private int headerHeight;
        private int footerHeight;
        private int contentMarginTop = 30;

        public ThreePartLayout(Screen screen, int headerHeight, int footerHeight) {
            this.screen = screen;
            this.headerHeight = headerHeight;
            this.footerHeight = footerHeight;
            this.headerFrame.defaultChildLayoutSetting().align(0.5F, 0.5F);
            this.footerFrame.defaultChildLayoutSetting().align(0.5F, 0.5F);
        }

        @Override
        public void setX(int x) {}

        @Override
        public void setY(int y) {}

        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getY() {
            return 0;
        }

        @Override
        public int getWidth() {
            return this.screen.width;
        }

        @Override
        public int getHeight() {
            return this.screen.height;
        }

        public int getFooterHeight() {
            return this.footerHeight;
        }

        public void setFooterHeight(int footerHeight) {
            this.footerHeight = footerHeight;
        }

        public void setHeaderHeight(int headerHeight) {
            this.headerHeight = headerHeight;
        }

        public void setContentMarginTop(int marginTop) {
            this.contentMarginTop = marginTop;
        }

        public int getHeaderHeight() {
            return this.headerHeight;
        }

        public int getContentHeight() {
            return this.screen.height - this.getHeaderHeight() - this.getFooterHeight();
        }

        @Override
        public void visitChildren(Consumer<LayoutElement> visitor) {
            this.headerFrame.visitChildren(visitor);
            this.contentsFrame.visitChildren(visitor);
            this.footerFrame.visitChildren(visitor);
        }

        @Override
        public void arrangeElements() {
            int headHeight = this.getHeaderHeight();
            int footHeight = this.getFooterHeight();

            this.headerFrame.setMinWidth(this.screen.width);
            this.headerFrame.setMinHeight(headHeight);

            this.headerFrame.setPosition(0, 0);
            this.headerFrame.arrangeElements();

            this.footerFrame.setMinWidth(this.screen.width);
            this.footerFrame.setMinHeight(footHeight);

            this.footerFrame.arrangeElements();
            this.footerFrame.setY(this.screen.height - footHeight);

            this.contentsFrame.setMinWidth(this.screen.width);
            this.contentsFrame.arrangeElements();

            int contentMin = headHeight + this.contentMarginTop;
            int contentMax = this.screen.height - footHeight - this.contentsFrame.getHeight();
            this.contentsFrame.setPosition(0, Math.min(contentMin, contentMax));
        }

        //? if >=26.2 {
        /*@Override
        public void removeChildren() {
            this.headerFrame.removeChildren();
            this.footerFrame.removeChildren();
            this.contentsFrame.removeChildren();
        }
        *///? }

        public <T extends LayoutElement> T addToHeader(T child) {
            return this.headerFrame.addChild(child);
        }


        public <T extends LayoutElement> T addToFooter(T child) {
            return this.footerFrame.addChild(child);
        }

        public <T extends LayoutElement> T addToContents(T child) {
            return this.contentsFrame.addChild(child);
        }

        //? if >=1.20.4 {
        public <T extends LayoutElement> T addToHeader(T child, Consumer<LayoutSettings> layoutSettingsFactory) {
            return this.headerFrame.addChild(child, layoutSettingsFactory);
        }

        public <T extends LayoutElement> T addToFooter(T child, Consumer<LayoutSettings> layoutSettingsFactory) {
            return this.footerFrame.addChild(child, layoutSettingsFactory);
        }

        public <T extends LayoutElement> T addToContents(T child, Consumer<LayoutSettings> layoutSettingFactory) {
            return this.contentsFrame.addChild(child, layoutSettingFactory);
        }
        //? } else {
        /*public <T extends LayoutElement> T addToHeader(T child, LayoutSettings layoutSettings) {
            return this.headerFrame.addChild(child, layoutSettings);
        }

        public <T extends LayoutElement> T addToFooter(T child, LayoutSettings layoutSettings) {
            return this.footerFrame.addChild(child, layoutSettings);
        }

        public <T extends LayoutElement> T addToContents(T child, LayoutSettings layoutSetting) {
            return this.contentsFrame.addChild(child, layoutSetting);
        }
        *///? }
    }
}
