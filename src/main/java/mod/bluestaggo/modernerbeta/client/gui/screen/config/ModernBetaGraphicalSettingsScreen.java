package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.*;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaClearableWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public abstract class ModernBetaGraphicalSettingsScreen<T extends NbtElement> extends GameOptionsScreen {
    protected static final String STRING_PREFIX = "createWorld.customize.modern_beta.settings.";

    protected final T settings;
    protected final GeneratorOptionsHolder generatorOptionsHolder;
    protected final Consumer<T> onDone;
    protected final String type;

    private double prevScroll = -1.0D;

    //? if <1.21
    /*protected OptionListWidget body;*/
    //? if <1.20.5
    /*public final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);*/

    public ModernBetaGraphicalSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        String type,
        T settings,
        Consumer<T> onDone
    ) {
        super(parent, null, Text.translatable(title));

        this.onDone = onDone;
        this.type = type;
        this.settings = settings;
        this.generatorOptionsHolder = generatorOptionsHolder;
    }

    //? if <1.20.5 {
    /*@Override
    protected void init() {
        this.initHeader();
        this.initBody();
        this.initFooter();
        this.layout.forEachChild(this::addDrawableChild);
        this.initTabNavigation();
    }
    *///?}

    protected abstract void addOptions(OptionListWidget list);

    protected void addOptions() {
    }

    @Override
    public void removed() {
    }

    @Override
    public void close() {
        this.client.setScreen(null);
    }

    @Override
    protected void clearChildren() {
        if (this.body != null) {
            this.prevScroll =
                //? if >=1.21.4 {
                this.body.getScrollY();
                //?} else {
                /*this.body.getScrollAmount();
                *///?}
        }

        super.clearChildren();

        ((ModernBetaClearableWidget)this.layout).modernBeta$clear();
    }

    protected void initHeader() {
    }

    protected void initBody() {
        this.body =
            //? if >=1.20.5 {
            this.layout.addBody(
                //? if >=1.21 {
                new OptionListWidget(this.client, this.width, this)
                //?} else {
                /*new OptionListWidget(this.client, this.width, 0, this)
                *///?}
            );
            //?} else {
            /*new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
            *///?}
        this.addOptions(this.body);
        //? if <1.21
        /*this.addSelectableChild(this.body);*/

        if (this.prevScroll >= 0.0D && this.body != null) {
            //? if >=1.21.4 {
            this.body.setScrollY
            //?} else {
            /*this.body.setScrollAmount
            *///?}
                (this.prevScroll);
        }

        //? if <1.21 {
        /*this.addDrawableChild(ButtonWidget.builder(
            Text.translatable("createWorld.customize.modern_beta.settings.save"),
            onPress -> {
                this.onDone.accept(this.getResult());
                this.client.setScreen(this.parent);
            }
        ).dimensions(this.width / 2 - 155, this.height - 28, 150, 20).build());

        this.addDrawableChild(ButtonWidget.builder(
            ScreenTexts.CANCEL,
            onPress -> this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 5, this.height - 28, 150, 20).build());
        *///?}
    }

    protected void initFooter() {
        GridWidget gridWidget = new GridWidget().setColumnSpacing(8);
        GridWidget.Adder gridWidgetAdder = gridWidget.createAdder(2);

        gridWidgetAdder.add(ButtonWidget.builder(
            Text.translatable("createWorld.customize.modern_beta.settings.save"),
            onPress -> {
                this.onDone.accept(this.getResult());
                this.client.setScreen(this.parent);
            }
        ).dimensions(this.width / 2 - 155, this.height - 28, 150, 20).build());

        gridWidgetAdder.add(ButtonWidget.builder(
            ScreenTexts.CANCEL,
            onPress -> this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 5, this.height - 28, 150, 20).build());

        this.layout.addFooter(gridWidget);
    }

    //? if <1.21 {
    /*@Override
    protected void render(DrawContext context, OptionListWidget optionButtons, int mouseX, int mouseY, float tickDelta) {
        super.renderBackground(context);
        this.body.render(context, mouseX, mouseY, tickDelta);
        super.render(context, optionButtons, mouseX, mouseY, tickDelta);
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

    protected String getTextKey(String key, Identifier subKey) {
        return getTextKey(key, subKey != null ? subKey.getPath() : null);
    }

    public MutableText getText(String key) {
        return this.getText(key, (String) null);
    }

    public MutableText getText(String key, String subKey) {
        return Text.translatable(getTextKey(key, subKey));
    }

    public MutableText getText(String key, Identifier subKey) {
        return Text.translatable(getTextKey(key, subKey));
    }

    public SimpleOption<Void> headerOption(Text text) {
        return this.headerOption(text, 0.5F);
    }

    public SimpleOption<Void> headerOption(Text text, float alignment) {
        return new SimpleOption<>(
            "",
            SimpleOption.emptyTooltip(),
            (optionText, value) -> text,
            new TextLabelCallbacks(text, alignment),
            null,
            value -> {}
        );
    }

    public SimpleOption<Void> placeholderOption(String key) {
        return this.headerOption(this.getText(key).formatted(Formatting.RED, Formatting.ITALIC));
    }

    public SimpleOption<Void> customButton(Text text, Runnable onPress) {
        return new SimpleOption<>(
            "",
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.empty(),
            new CustomButtonCallbacks(text, onPress),
            null,
            value -> {}
        );
    }
}
