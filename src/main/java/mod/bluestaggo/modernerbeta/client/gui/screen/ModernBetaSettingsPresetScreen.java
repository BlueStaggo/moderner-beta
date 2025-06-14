package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ModernBetaSettingsPresetScreen extends ModernBetaScreen {
    private static final String TEXT_TITLE = "createWorld.customize.modern_beta.title.preset";
    private static final String TEXT_PRESET_NAME = "createWorld.customize.modern_beta.preset.name";
    private static final String TEXT_PRESET_DESC = "createWorld.customize.modern_beta.preset.desc";
    private static final String TEXT_PRESET_CATEGORY_NAME = "createWorld.customize.modern_beta.preset_category.name";
    private static final String TEXT_PRESET_CATEGORY_DESC = "createWorld.customize.modern_beta.preset_category.desc";
    
    private static final Identifier TEXTURE_PRESET_CUSTOM = createTextureId(ModernerBeta.createId("custom"));
    
    private final ModernBetaWorldScreen worldScreen;
    private final List<Identifier> presets;
    private final boolean displayCategories;

    private final Registry<ModernBetaSettingsPreset> presetRegistry;
    private final Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry;

    private ModernBetaSettingsPreset preset;
    private PresetsListWidget listWidget;
    private ButtonWidget selectPresetButton;

    public ModernBetaSettingsPresetScreen(
        ModernBetaScreen parent,
        Registry<ModernBetaSettingsPreset> presetRegistry,
        Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry,
        List<Identifier> presets,
        ModernBetaSettingsPreset preset,
        boolean displayCategories
    ) {
        super(Text.translatable(TEXT_TITLE), parent);

        this.presets = presets;
        this.preset = preset;
        this.displayCategories = displayCategories;

        this.presetRegistry = presetRegistry;
        this.presetCategoryRegistry = presetCategoryRegistry;

        Screen worldScreen = parent;
        while (!(worldScreen instanceof ModernBetaWorldScreen)) {
            if (!(worldScreen instanceof ModernBetaScreen modernBetaScreen)) {
                worldScreen = null;
                break;
            }
            worldScreen = modernBetaScreen.parent;
        }
        this.worldScreen = (ModernBetaWorldScreen)worldScreen;
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.listWidget = new PresetsListWidget(this.presets);
        this.addSelectableChild(this.listWidget);

        this.selectPresetButton = this.addDrawableChild(ButtonWidget.builder(
            Text.translatable("createWorld.customize.presets.select"),
            onPress -> {
                this.worldScreen.setPreset(this.preset);
                this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 154, this.height - 26, 150, 20).build());
        this.selectPresetButton.active = !this.displayCategories;
        this.addDrawableChild(ButtonWidget.builder(
            this.displayCategories ? ScreenTexts.CANCEL : ScreenTexts.BACK,
            button -> this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 4, this.height - 26, 150, 20).build());

        this.updateSelectButton(this.listWidget.getSelectedOrNull() instanceof PresetsListWidget.PresetEntry);
    }

    //? if <1.20.5 {
    /*@Override
    public void renderBackground(DrawContext context) {
    }
    *///?}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //? if <1.20.5
        /*this.listWidget.render(context, mouseX, mouseY, delta);*/
        super.render(context, mouseX, mouseY, delta);
        //? if >=1.20.5
        this.listWidget.render(context, mouseX, mouseY, delta);
    }

    private void updateSelectButton(boolean hasSelected) {
        this.selectPresetButton.active = hasSelected;
    }
    
    private static Identifier createTextureId(Identifier id) {
        return id.withPath("textures/gui/moderner_beta_settings_preset/" + id.getPath() + ".png");
    }

    private static Identifier createPresetTextureId(Identifier id) {
        Identifier idObj = createTextureId(id);
        return MinecraftClient.getInstance().getResourceManager().getResource(idObj).isPresent()
            ? idObj : TEXTURE_PRESET_CUSTOM;
    }

    private class PresetsListWidget extends AlwaysSelectedEntryListWidget<PresetsListWidget.AbstractPresetEntry> {
        private static final int ITEM_HEIGHT = 60;
        private static final int ICON_SIZE = 56;

        public PresetsListWidget(List<Identifier> presets) {
            //? if >=1.20.3 {
            super(
                ModernBetaSettingsPresetScreen.this.client,
                ModernBetaSettingsPresetScreen.this.width,
                ModernBetaSettingsPresetScreen.this.height - 64,
                32,
                ITEM_HEIGHT
            );
            //?} else {
            /*super(
                ModernBetaSettingsPresetScreen.this.client,
                ModernBetaSettingsPresetScreen.this.width,
                ModernBetaSettingsPresetScreen.this.height,
                32,
                ModernBetaSettingsPresetScreen.this.height - 32,
                ITEM_HEIGHT
            );
            *///?}

            if (ModernBetaSettingsPresetScreen.this.displayCategories) {
                presets.forEach(key -> {
                    this.addEntry(new PresetCategoryEntry(
                        key,
                        presetCategoryRegistry.get(key)
                    ));
                });
            } else {
                presets.forEach(key -> {
                    this.addEntry(new PresetEntry(key));
                });
            }
        }
        
        @Override
        public void setSelected(AbstractPresetEntry entry) {
            super.setSelected(entry);

            ModernBetaSettingsPresetScreen.this.updateSelectButton(entry instanceof PresetEntry);
        }

        private static final int SCROLLBAR_X_OFFSET = 30;
        //? if >=1.20.5 {
        @Override
        protected int getScrollbarX() {
            return super.getScrollbarX() + SCROLLBAR_X_OFFSET;
        }
        //?} else {
        /*@Override
        protected int getScrollbarPositionX() {
            return super.getScrollbarPositionX() + SCROLLBAR_X_OFFSET;
        }
        *///?}

        @Override
        public int getRowWidth() {
            return super.getRowWidth() + 85;
        }
        
        private abstract class AbstractPresetEntry extends AlwaysSelectedEntryListWidget.Entry<AbstractPresetEntry> {
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
            private final MutableText presetName;
            private final MutableText presetDesc;

            private long time;
            
            public AbstractPresetEntry(Identifier presetName) {
                this.presetTexture = this.getPresetTexture(presetName);
                this.presetName = this.getPresetName(presetName);
                this.presetDesc = this.getPresetDesc(presetName);
            }

            protected abstract void setPreset();

            protected abstract void selectPreset();

            protected Identifier getPresetTexture(Identifier presetName) {
                return createPresetTextureId(presetName);
            }

            protected MutableText getPresetName(Identifier presetName) {
                return Text.translatable(TEXT_PRESET_NAME + "." + presetName.toTranslationKey());
            }

            protected MutableText getPresetDesc(Identifier presetName) {
                return Text.translatable(TEXT_PRESET_DESC + "." + presetName.toTranslationKey());
            }

            protected Formatting getTextFormatting() {
                return Formatting.YELLOW;
            }

            @Override
            public Text getNarration() {
                return Text.empty();
            }

            @Override
            public void render(DrawContext context,int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                MutableText presetNameText = this.presetName.formatted(this.getTextFormatting());
                
                List<OrderedText> presetDescTexts = this.splitText(textRenderer, this.presetDesc);

                int textStartX = x + ICON_SIZE + 3;
                int textStartY = 1;
                
                context.drawText(textRenderer, presetNameText, textStartX, y + textStartY, Colors.WHITE, false);
                
                int descSpacing = TEXT_SPACING + textStartY + 1;
                for (OrderedText line : presetDescTexts) {
                    context.drawText(textRenderer, line, textStartX, y + descSpacing, Colors.GRAY, false);
                    descSpacing += TEXT_SPACING;
                }

                this.draw(context, x, y, this.presetTexture);

                if (client != null && client.options.getTouchscreen().getValue() || hovered) {
                    boolean isMouseHovering = (mouseX - x) < ICON_SIZE;

                    context.fill(x, y, x + ICON_SIZE, y + ICON_SIZE, -1601138544);
                    //? if >=1.20.2 {
                    Identifier texture = isMouseHovering ? TEXTURE_JOIN_HIGHLIGHTED : TEXTURE_JOIN;
                    context.drawGuiTexture(
                        //? if >=1.21.6 {
                        /*net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED,*/
                        //?} else if >=1.21.2 {
                        net.minecraft.client.render.RenderLayer::getGuiTextured,
                        //?}
                        texture,
                        x,
                        y,
                        ICON_SIZE,
                        ICON_SIZE
                    );
                    //?} else {
                    /*float v = isMouseHovering ? TEXTURE_WORLD_SELECT_SIZE : 0;
                    context.drawTexture(
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
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button != 0) {
                    return false;
                }
                
                this.setPreset();
                
                if (mouseX - PresetsListWidget.this.getRowLeft() <= ICON_SIZE) {
                    this.selectPreset();
                }
                
                if (Util.getMeasuringTimeMs() - this.time < 250L) {
                    this.selectPreset();
                }
                
                this.time = Util.getMeasuringTimeMs();
                
                return true;
            }

            private void draw(DrawContext context, int x, int y, Identifier textureId) {
                context.drawTexture(
                    //? if >= 1.21.6 {
                    /*net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED,*/
                    //?} else if >=1.21.2 {
                    net.minecraft.client.render.RenderLayer::getGuiTextured,
                    //?}
                    textureId,
                    x, y,
                    0.0f, 0.0f,
                    ICON_SIZE, ICON_SIZE,
                    ICON_SIZE, ICON_SIZE
                );
            }
            
            private List<OrderedText> splitText(TextRenderer textRenderer, Text text) {
                return textRenderer.wrapLines(text, TEXT_LENGTH);
            }
        }

        private class PresetEntry extends AbstractPresetEntry {
            private final Identifier key;

            public PresetEntry(Identifier presetName) {
                super(presetName);
                this.key = presetName;
            }

            @Override
            protected void setPreset() {
                PresetsListWidget.this.setSelected(this);
                ModernBetaSettingsPresetScreen.this.preset = ModernBetaSettingsPreset.referenced(this.key);
            }

            @Override
            protected void selectPreset() {
                ModernBetaSettingsPresetScreen presetScreen = ModernBetaSettingsPresetScreen.this;
                MinecraftClient minecraftClient = presetScreen.client;

                minecraftClient.getSoundManager().play(
                    PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f)
                );

            presetScreen.worldScreen.setPreset(ModernBetaSettingsPreset.referenced(this.key));

                while (minecraftClient.currentScreen instanceof ModernBetaSettingsPresetScreen subPresetScreen) {
                    minecraftClient.setScreen(subPresetScreen.parent);
                }
            }
        }

        private class PresetCategoryEntry extends AbstractPresetEntry {
            private final ModernBetaSettingsPresetCategory presetCategory;

            public PresetCategoryEntry(Identifier presetName, ModernBetaSettingsPresetCategory presetCategory) {
                super(presetName);
                this.presetCategory = presetCategory;
            }

            @Override
            protected Identifier getPresetTexture(Identifier presetName) {
                presetName = presetCategoryRegistry.get(presetName).defaultIcon();
                return super.getPresetTexture(presetName);
            }

            @Override
            protected MutableText getPresetName(Identifier presetName) {
                return Text.translatable(TEXT_PRESET_CATEGORY_NAME + "." + presetName.toTranslationKey());
            }

            @Override
            protected MutableText getPresetDesc(Identifier presetName) {
                return Text.translatable(TEXT_PRESET_CATEGORY_DESC + "." + presetName.toTranslationKey());
            }

            @Override
            protected Formatting getTextFormatting() {
                return Formatting.AQUA;
            }

            @Override
            protected void setPreset() {
                PresetsListWidget.this.setSelected(this);
            }

            @Override
            protected void selectPreset() {
                assert client != null;

                client.getSoundManager().play(
                    PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f)
                );

                client.setScreen(new ModernBetaSettingsPresetScreen(
                    ModernBetaSettingsPresetScreen.this,
                    presetRegistry,
                    presetCategoryRegistry,
                    presetCategory.presets(),
                    preset,
                    false
                ));
            }
        }
    }
}
