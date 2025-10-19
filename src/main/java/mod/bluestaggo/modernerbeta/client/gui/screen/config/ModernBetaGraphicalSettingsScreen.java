package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.*;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaClearableWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.OptionInstance;
//? if <1.21
/*import net.minecraft.client.gui.GuiGraphics;*/
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.layouts.GridLayout;
//? if <1.20.5
/*import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;*/
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.locale.Language;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public abstract class ModernBetaGraphicalSettingsScreen<T extends Tag> extends OptionsSubScreen {
    protected static final String STRING_PREFIX = "createWorld.customize.modern_beta.settings.";

    protected final T settings;
    protected final WorldCreationContext context;
    protected final Consumer<T> onDone;
    protected final String type;

    private double prevScroll = -1.0D;

    //? if <1.21
    /*protected OptionsList list;*/
    //? if <1.20.5
    /*public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);*/

    public ModernBetaGraphicalSettingsScreen(
        String title,
        Screen parent,
        WorldCreationContext context,
        String type,
        T settings,
        Consumer<T> onDone
    ) {
        super(parent, null, Component.translatable(title));

        this.onDone = onDone;
        this.type = type;
        this.settings = settings;
        this.context = context;
    }

    //? if <1.20.5 {
    /*@Override
    protected void init() {
        this.addHeader();
        this.addContents();
        this.addFooter();
        this.layout.visitWidgets(this::addRenderableWidget);
    }
    *///?}

    protected abstract void addOptions(OptionsList list);

    protected void addOptions() {
    }

    @Override
    public void removed() {
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
    }

    @Override
    protected void clearWidgets() {
        if (this.list != null) {
            this.prevScroll =
                //? if >=1.21.4 {
                this.list.scrollAmount();
                //?} else {
                /*this.list.getScrollAmount();
                *///?}
        }

        super.clearWidgets();

        ((ModernBetaClearableWidget)this.layout).modernBeta$clear();
    }

    //? if <1.20.5 {
    /*protected void addHeader() {
    }
    *///?}

    //? if >=1.20.5
    @Override
    protected void addContents() {
        this.list =
            //? if >=1.20.5 {
            this.layout.addToContents(
                //? if >=1.21 {
                new OptionsList(this.minecraft, this.width, this)
                //?} else {
                /*new OptionsList(this.minecraft, this.width, 0, this)
                *///?}
            );
            //?} else {
            /*new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
            *///?}
        this.addOptions(this.list);
        //? if <1.21
        /*this.addWidget(this.list);*/

        if (this.prevScroll >= 0.0D && this.list != null) {
            //? if >=1.21.4 {
            this.list.setScrollAmount
            //?} else {
            /*this.list.setScrollAmount
            *///?}
                (this.prevScroll);
        }

        //? if <1.21 {
        /*this.addRenderableWidget(Button.builder(
            Component.translatable("createWorld.customize.modern_beta.settings.save"),
            onPress -> {
                this.onDone.accept(this.getResult());
                this.minecraft.setScreen(this.lastScreen);
            }
        ).bounds(this.width / 2 - 155, this.height - 28, 150, 20).build());

        this.addRenderableWidget(Button.builder(
            CommonComponents.GUI_CANCEL,
            onPress -> this.minecraft.setScreen(this.lastScreen)
        ).bounds(this.width / 2 + 5, this.height - 28, 150, 20).build());
        *///?}
    }

    //? if >=1.20.5
    @Override
    protected void addFooter() {
        GridLayout gridWidget = new GridLayout().columnSpacing(8);
        GridLayout.RowHelper gridWidgetAdder = gridWidget.createRowHelper(2);

        gridWidgetAdder.addChild(Button.builder(
            Component.translatable("createWorld.customize.modern_beta.settings.save"),
            onPress -> {
                this.onDone.accept(this.getResult());
                this.minecraft.setScreen(this.lastScreen);
            }
        ).bounds(this.width / 2 - 155, this.height - 28, 150, 20).build());

        gridWidgetAdder.addChild(Button.builder(
            CommonComponents.GUI_CANCEL,
            onPress -> this.minecraft.setScreen(this.lastScreen)
        ).bounds(this.width / 2 + 5, this.height - 28, 150, 20).build());

        this.layout.addToFooter(gridWidget);
    }

    //? if <1.21 {
    /*@Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float tickDelta) {
        this.basicListRender(graphics, this.list, mouseX, mouseY, tickDelta);
    }
    *///?}

    protected T getResult() {
        return this.settings;
    }

    protected String getTextKey(String key) {
        return getTextKey(key, (String) null);
    }

    protected String getTextKey(String key, String subKey) {
        String text = STRING_PREFIX;
        if (this.type != null) {
            text += type + ".";
        }
        text += key;
        if (subKey != null) {
            text += "." + subKey;
        }
        return text;
    }

    protected String getTextKey(String key, ResourceLocation subKey) {
        return getTextKey(key, subKey != null ? subKey.getPath() : null);
    }

    protected static <T> OptionInstance.TooltipSupplier<T> getTooltip(String key) {
        key += ".desc";
        if (!Language.getInstance().has(key)) {
            return OptionInstance.noTooltip();
        }
        return OptionInstance.cachedConstantTooltip(Component.translatable(key));
    }

    public MutableComponent getText(String key) {
        return this.getText(key, (String) null);
    }

    public MutableComponent getText(String key, String subKey) {
        return Component.translatable(getTextKey(key, subKey));
    }

    public MutableComponent getText(String key, ResourceLocation subKey) {
        return Component.translatable(getTextKey(key, subKey));
    }

    public OptionInstance<Void> headerOption(Component text) {
        return this.headerOption(text, 0.5F);
    }

    public OptionInstance<Void> headerOption(Component text, float alignment) {
        return new OptionInstance<>(
            "",
            OptionInstance.noTooltip(),
            (optionText, value) -> text,
            new TextLabelCallbacks(text, alignment),
            null,
            value -> {}
        );
    }

    public OptionInstance<Void> placeholderOption(String key) {
        return this.headerOption(this.getText(key).withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
    }

    public OptionInstance<Void> customButton(Component text, Runnable onPress) {
        return new OptionInstance<>(
            "",
            OptionInstance.noTooltip(),
            (optionText, value) -> Component.empty(),
            new CustomButtonCallbacks(text, onPress),
            null,
            value -> {}
        );
    }
}
