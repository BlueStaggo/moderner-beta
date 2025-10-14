package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
//? if <1.21.9
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ModernBetaSettingsPresetScreen extends ModernBetaScreen {
    private static final String TEXT_TITLE = "createWorld.customize.modern_beta.title.preset";
    private static final String TEXT_PRESET_NAME = "createWorld.customize.modern_beta.preset.name";
    private static final String TEXT_PRESET_DESC = "createWorld.customize.modern_beta.preset.desc";
    private static final String TEXT_PRESET_CATEGORY_NAME = "createWorld.customize.modern_beta.preset_category.name";
    private static final String TEXT_PRESET_CATEGORY_DESC = "createWorld.customize.modern_beta.preset_category.desc";
    
    private static final ResourceLocation TEXTURE_PRESET_CUSTOM = createTextureId(ModernerBeta.createId("custom"));
    
    private final ModernBetaWorldScreen worldScreen;
    private final List<ResourceLocation> presets;
    private final boolean displayCategories;

    private final Registry<ModernBetaSettingsPreset> presetRegistry;
    private final Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry;

    private ModernBetaSettingsPreset preset;
    private PresetsListWidget listWidget;
    private Button selectPresetButton;

    public ModernBetaSettingsPresetScreen(
        ModernBetaScreen parent,
        Registry<ModernBetaSettingsPreset> presetRegistry,
        Registry<ModernBetaSettingsPresetCategory> presetCategoryRegistry,
        List<ResourceLocation> presets,
        ModernBetaSettingsPreset preset,
        boolean displayCategories
    ) {
        super(Component.translatable(TEXT_TITLE), parent);

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
        this.addWidget(this.listWidget);

        this.selectPresetButton = this.addRenderableWidget(Button.builder(
            Component.translatable("createWorld.customize.presets.select"),
            onPress -> {
                this.worldScreen.setPreset(this.preset);
                this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 - 154, this.height - 26, 150, 20).build());
        this.selectPresetButton.active = !this.displayCategories;
        this.addRenderableWidget(Button.builder(
            this.displayCategories ? CommonComponents.GUI_CANCEL : CommonComponents.GUI_BACK,
            button -> this.minecraft.setScreen(this.parent)
        ).bounds(this.width / 2 + 4, this.height - 26, 150, 20).build());

        this.updateSelectButton(this.listWidget.getSelected() instanceof PresetsListWidget.PresetEntry);
    }

    //? if <1.20.5 {
    /*@Override
    public void renderBackground(GuiGraphics context) {
    }
    *///?}

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        //? if <1.20.5
        /*this.listWidget.render(context, mouseX, mouseY, delta);*/
        super.render(context, mouseX, mouseY, delta);
        //? if >=1.20.5
        this.listWidget.render(context, mouseX, mouseY, delta);
    }

    private void updateSelectButton(boolean hasSelected) {
        this.selectPresetButton.active = hasSelected;
    }
    
    private static ResourceLocation createTextureId(ResourceLocation id) {
        return id.withPath("textures/gui/moderner_beta_settings_preset/" + id.getPath() + ".png");
    }

    private static ResourceLocation createPresetTextureId(ResourceLocation id) {
        ResourceLocation idObj = createTextureId(id);
        return Minecraft.getInstance().getResourceManager().getResource(idObj).isPresent()
            ? idObj : TEXTURE_PRESET_CUSTOM;
    }

    private class PresetsListWidget extends ObjectSelectionList<PresetsListWidget.AbstractPresetEntry> {
        private static final int ITEM_HEIGHT = 60;
        private static final int ICON_SIZE = 56;

        public PresetsListWidget(List<ResourceLocation> presets) {
            //? if >=1.20.3 {
            super(
                ModernBetaSettingsPresetScreen.this.minecraft,
                ModernBetaSettingsPresetScreen.this.width,
                ModernBetaSettingsPresetScreen.this.height - 64,
                32,
                ITEM_HEIGHT
            );
            //?} else {
            /*super(
                ModernBetaSettingsPresetScreen.this.minecraft,
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
                        presetCategoryRegistry.getValue(key)
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
        
        private abstract class AbstractPresetEntry extends ObjectSelectionList.Entry<AbstractPresetEntry> {
            //? if >=1.20.2 {
            private static final ResourceLocation TEXTURE_JOIN = VersionCompat.vanillaId("world_list/join");
            private static final ResourceLocation TEXTURE_JOIN_HIGHLIGHTED =  VersionCompat.vanillaId("world_list/join_highlighted");
            //?} else {
            /*private static final ResourceLocation TEXTURE_WORLD_SELECT = new ResourceLocation("textures/gui/world_selection.png");
            private static final int TEXTURE_WORLD_SELECT_ATLAS_SIZE = 256;
            private static final int TEXTURE_WORLD_SELECT_SIZE= 32;
            *///?}
            
            private static final int TEXT_SPACING = 11;
            private static final int TEXT_LENGTH = 240;
            
            private final ResourceLocation presetTexture;
            private final MutableComponent presetName;
            private final MutableComponent presetDesc;

            //? if <1.21.9
            private long time;
            
            public AbstractPresetEntry(ResourceLocation presetName) {
                this.presetTexture = this.getPresetTexture(presetName);
                this.presetName = this.getPresetName(presetName);
                this.presetDesc = this.getPresetDesc(presetName);
            }

            protected abstract void setPreset();

            protected abstract void selectPreset();

            protected ResourceLocation getPresetTexture(ResourceLocation presetName) {
                return createPresetTextureId(presetName);
            }

            protected MutableComponent getPresetName(ResourceLocation presetName) {
                return Component.translatable(TEXT_PRESET_NAME + "." + presetName.toLanguageKey());
            }

            protected MutableComponent getPresetDesc(ResourceLocation presetName) {
                return Component.translatable(TEXT_PRESET_DESC + "." + presetName.toLanguageKey());
            }

            protected ChatFormatting getTextFormatting() {
                return ChatFormatting.YELLOW;
            }

            @Override
            public Component getNarration() {
                return Component.empty();
            }

            @Override
            public void render(GuiGraphics context,
                //? if <1.21.9
                int index, int y, int x, int entryWidth, int entryHeight,
                int mouseX, int mouseY, boolean hovered, float tickDelta) {
                //? if >=1.21.9 {
                /*int x = this.getContentX();
                int y = this.getContentY();
                *///?}

                MutableComponent presetNameText = this.presetName.withStyle(this.getTextFormatting());
                
                List<FormattedCharSequence> presetDescTexts = this.splitText(font, this.presetDesc);

                int textStartX = x + ICON_SIZE + 3;
                int textStartY = 1;
                
                context.drawString(font, presetNameText, textStartX, y + textStartY, CommonColors.WHITE, false);
                
                int descSpacing = TEXT_SPACING + textStartY + 1;
                for (FormattedCharSequence line : presetDescTexts) {
                    context.drawString(font, line, textStartX, y + descSpacing, CommonColors.GRAY, false);
                    descSpacing += TEXT_SPACING;
                }

                this.draw(context, x, y, this.presetTexture);

                if (minecraft != null && minecraft.options.touchscreen().get() || hovered) {
                    boolean isMouseHovering = (mouseX - x) < ICON_SIZE;

                    context.fill(x, y, x + ICON_SIZE, y + ICON_SIZE, -1601138544);
                    //? if >=1.20.2 {
                    ResourceLocation texture = isMouseHovering ? TEXTURE_JOIN_HIGHLIGHTED : TEXTURE_JOIN;
                    context.blitSprite(
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
                    context.blit(
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
            public boolean mouseClicked(/*? if <1.21.9 {*/ double mouseX, double mouseY, int button /*?} else {*/ /*net.minecraft.client.input.MouseButtonEvent click, boolean doubleClick *//*?}*/) {
                //? if >=1.21.9 {
                /*double mouseX = click.x();
                double mouseY = click.y();

                int button = click.button();
                *///?}

                if (button != 0) {
                    return false;
                }
                
                this.setPreset();
                
                if (mouseX - PresetsListWidget.this.getRowLeft() <= ICON_SIZE) {
                    this.selectPreset();
                }
                
                if (/*? >=1.21.9 {*/ /*doubleClick *//*?} else {*/ Util.getMillis() - this.time < 250L /*?}*/) {
                    this.selectPreset();
                }

                //? if <1.21.9
                this.time = Util.getMillis();
                
                return true;
            }

            private void draw(GuiGraphics context, int x, int y, ResourceLocation textureId) {
                context.blit(
                    //? if >= 1.21.6 {
                    net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
                    //?} else if >=1.21.2 {
                    /*net.minecraft.client.render.RenderLayer::getGuiTextured,
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

        private class PresetEntry extends AbstractPresetEntry {
            private final ResourceLocation key;

            public PresetEntry(ResourceLocation presetName) {
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
                Minecraft minecraftClient = presetScreen.minecraft;

                minecraftClient.getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f)
                );

            presetScreen.worldScreen.setPreset(ModernBetaSettingsPreset.referenced(this.key));

                while (minecraftClient.screen instanceof ModernBetaSettingsPresetScreen subPresetScreen) {
                    minecraftClient.setScreen(subPresetScreen.parent);
                }
            }
        }

        private class PresetCategoryEntry extends AbstractPresetEntry {
            private final ModernBetaSettingsPresetCategory presetCategory;

            public PresetCategoryEntry(ResourceLocation presetName, ModernBetaSettingsPresetCategory presetCategory) {
                super(presetName);
                this.presetCategory = presetCategory;
            }

            @Override
            protected ResourceLocation getPresetTexture(ResourceLocation presetName) {
                presetName = presetCategoryRegistry.getValue(presetName).defaultIcon();
                return super.getPresetTexture(presetName);
            }

            @Override
            protected MutableComponent getPresetName(ResourceLocation presetName) {
                return Component.translatable(TEXT_PRESET_CATEGORY_NAME + "." + presetName.toLanguageKey());
            }

            @Override
            protected MutableComponent getPresetDesc(ResourceLocation presetName) {
                return Component.translatable(TEXT_PRESET_CATEGORY_DESC + "." + presetName.toLanguageKey());
            }

            @Override
            protected ChatFormatting getTextFormatting() {
                return ChatFormatting.AQUA;
            }

            @Override
            protected void setPreset() {
                PresetsListWidget.this.setSelected(this);
            }

            @Override
            protected void selectPreset() {
                assert minecraft != null;

                minecraft.getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f)
                );

                minecraft.setScreen(new ModernBetaSettingsPresetScreen(
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
