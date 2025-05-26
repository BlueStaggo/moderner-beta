package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaBuiltInRegistries;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverExtendedId;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverExtendedIdStepped;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverStepped;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsBiome;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ModernBetaBiomePreviewScreen extends ModernBetaScreen {
    private final BiomeProvider biomeProvider;
    private BiomeDisplayWidget biomeDisplay;

    public ModernBetaBiomePreviewScreen(Text title, Screen parent, GeneratorOptionsHolder generationOptions, ModernBetaSettingsBiome biomeSettings) {
        super(title, parent);
        this.biomeProvider = ModernBetaBuiltInRegistries.BIOME.get(biomeSettings.biomeProvider)
            .apply(
                biomeSettings.toCompound(),
                generationOptions.getCombinedRegistryManager().getOrThrow(RegistryKeys.BIOME),
                generationOptions.generatorOptions().getSeed()
            );
    }

    @Override
    protected void init() {
        super.init();

        this.biomeDisplay = new BiomeDisplayWidget(0, 0, this.width * 3 / 4, this.height * 3 / 4);

        boolean hasSteps = this.biomeProvider instanceof BiomeResolverStepped;

        ButtonWidget buttonZoomOut = ButtonWidget.builder(Text.of("-"), button -> {
            this.biomeDisplay.zoom *= 2;
        }).dimensions(0, 0, 20, 20).build();
        ButtonWidget buttonZoomIn = ButtonWidget.builder(Text.of("+"), button -> {
            if (this.biomeDisplay.zoom > 1) {
                this.biomeDisplay.zoom /= 2;
            }
        }).dimensions(0, 0, 20, 20).build();
        ButtonWidget buttonBack = ButtonWidget.builder(ScreenTexts.BACK, button -> {
            this.client.setScreen(this.parent);
        }).dimensions(0, 0, 100, 20).build();

        GridWidget gridWidgetMain = this.createGridWidget();
        GridWidget gridWidgetButtons = this.createGridWidget();
        gridWidgetMain.getMainPositioner().alignHorizontalCenter().alignVerticalCenter();

        GridWidget.Adder gridAdderMain = gridWidgetMain.createAdder(1);
        GridWidget.Adder gridAdderButtons = gridWidgetButtons.createAdder(hasSteps ? 5 : 3);

        gridAdderMain.add(this.biomeDisplay);
        gridAdderMain.add(gridWidgetButtons);

        gridAdderButtons.add(buttonZoomOut);
        gridAdderButtons.add(buttonZoomIn);

        if (hasSteps) {
            BiomeResolverStepped stepResolver = (BiomeResolverStepped) this.biomeProvider;
            int stepCount = stepResolver.getStepCount();
            this.biomeDisplay.step = stepCount - 1;

            ButtonWidget buttonPrevStep = ButtonWidget.builder(Text.of("◀"), button -> {
                int step = this.biomeDisplay.step - 1;
                if (step < 0) {
                    step = stepCount - 1;
                }
                this.biomeDisplay.step = step;
            }).dimensions(0, 0, 20, 20).build();
            ButtonWidget buttonNextStep = ButtonWidget.builder(Text.of("▶"), button -> {
                int step = this.biomeDisplay.step + 1;
                if (step >= stepCount) {
                    step = 0;
                }
                this.biomeDisplay.step = step;
            }).dimensions(0, 0, 20, 20).build();

            gridAdderButtons.add(buttonPrevStep);
            gridAdderButtons.add(buttonNextStep);
        }

        gridAdderButtons.add(buttonBack);

        gridWidgetMain.refreshPositions();
        SimplePositioningWidget.setPos(gridWidgetMain, 0, 0, this.width, this.height, 0.5f, 1.0f);
        gridWidgetMain.forEachChild(this::addDrawableChild);

        this.biomeDisplay.startRenderThread();
    }

    @Override
    protected void clearChildren() {
        this.biomeDisplay.close();
        super.clearChildren();
    }

    class BiomeDisplayWidget extends ClickableWidget implements AutoCloseable {
        static final Identifier TEXTURE_ID = ModernerBeta.createId("biome_preview");

        final TextureManager textureManager;
        final NativeImage image;
        final NativeImageBackedTexture texture;
        final BiomeRenderThread renderThread;
        volatile int zoom = 1;
        volatile int step = 0;

        int prevMouseX, prevMouseY;
        final AtomicInteger offsetX = new AtomicInteger();
        final AtomicInteger offsetY = new AtomicInteger();

        BiomeDisplayWidget(int x, int y, int width, int height) {
            super(x, y, width, height, Text.empty());

            assert ModernBetaBiomePreviewScreen.this.client != null;
            this.textureManager = ModernBetaBiomePreviewScreen.this.client.getTextureManager();
            this.image = new NativeImage(width, height, false);
            this.texture = new NativeImageBackedTexture(TEXTURE_ID::toString, this.image);
            this.textureManager.registerTexture(TEXTURE_ID, this.texture);

            this.image.fillRect(0, 0, width, height, 0xFF000000);
            this.texture.upload();

            this.renderThread = new BiomeRenderThread();
        }

        void startRenderThread() {
            this.renderThread.start();
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            if (this.renderThread.uploadRequested) {
                synchronized (this.image) {
                    this.texture.upload();
                }
            }

            context.drawTexture(
                RenderLayer::getGuiTextured, TEXTURE_ID,
                this.getX(), this.getY(),
                0.0F, 0.0F,
                this.getWidth(), this.getHeight(),
                this.getWidth(), this.getHeight()
            );

            if (biomeProvider instanceof BiomeResolverStepped resolverStepped) {
                Text stepName = resolverStepped.getStepName(this.step);
                context.fill(this.getX(), this.getY(), this.getX() + textRenderer.getWidth(stepName) + 8, this.getY() + 16, 0xAA000000);
                context.drawText(textRenderer, stepName, this.getX() + 4, this.getY() + 4, 0xFFFFFF, false);
            }

            int offsetMouseX = mouseX - this.getX();
            int offsetMouseY = mouseY - this.getY();
            if (offsetMouseX >= 0 && offsetMouseY >= 0 && offsetMouseX < this.width && offsetMouseY < this.height) {
                int sampleX = (offsetMouseX + offsetX.get() - this.width / 2) * this.zoom;
                int sampleY = (offsetMouseY + offsetY.get() - this.height / 2) * this.zoom;
                Text biomeName = biomeProvider instanceof BiomeResolverStepped resolverStepped
                    ? resolverStepped.getBiomeNameForStep(sampleX, 64, sampleY, step)
                    : biomeProvider.getBiomeName(sampleX, 64, sampleY);
                context.drawTooltip(textRenderer, biomeName, mouseX, mouseY);
            }

            this.prevMouseX = mouseX;
            this.prevMouseY = mouseY;
        }

        @Override
        protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
            this.offsetX.getAndAdd((int)Math.round(-deltaX));
            this.offsetY.getAndAdd((int)Math.round(-deltaY));
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        }

        @Override
        public void close() {
            this.renderThread.stop = true;
            this.texture.close();
            this.textureManager.destroyTexture(TEXTURE_ID);
        }

        class BiomeRenderThread extends Thread {
            volatile boolean stop;
            volatile boolean uploadRequested;
            volatile boolean clearRequested;

            int genX = -1;
            int genY;

            @Override
            public void run() {
                while (!stop) {
                    if (clearRequested) {
                        synchronized (image) {
                            image.fillRect(0, 0, width, height, 0x7F000000);
                        }
                        this.genX = -1;
                        this.genY = 0;
                        this.uploadRequested = true;
                    }

                    this.genX++;
                    if (this.genX >= width) {
                        this.genX = 0;
                        this.genY++;
                    }
                    if (this.genY >= height) {
                        this.genY = 0;
                    }

                    int scale = BiomeDisplayWidget.this.zoom;
                    int sampleX = (this.genX + offsetX.get() - width / 2) * scale;
                    int sampleY = (this.genY + offsetY.get() - height / 2) * scale;

                    RegistryEntry<Biome> biome = biomeProvider instanceof BiomeResolverStepped resolverStepped
                        ? resolverStepped.getBiomeForStep(sampleX, 64, sampleY, step)
                        : biomeProvider.getBiome(sampleX, 64, sampleY);
                    ExtendedBiomeId extendedBiome = biomeProvider instanceof BiomeResolverExtendedId resolverExtendedId
                        ? resolverExtendedId instanceof BiomeResolverExtendedIdStepped resolverExtendedIdStepped
                            ? resolverExtendedIdStepped.getExtendedBiomeIdForStep(sampleX, 64, sampleY, step)
                            : resolverExtendedId.getExtendedBiomeId(sampleX, 64, sampleY)
                        : ExtendedBiomeId.NULL;
                    int color = 0xFF000000 | getBiomeColor(biome, extendedBiome.ext(), sampleX, sampleY);

                    synchronized (image) {
                        image.setColorArgb(this.genX, this.genY, color);
                        this.uploadRequested = true;
                    }
                }
            }

            private static int getBiomeColor(RegistryEntry<Biome> biomeEntry, String ext, int x, int y) {
                String id = biomeEntry.getIdAsString();
                if (ext != null && !ext.isEmpty()) {
                    String extId = id + "*" + ext;
                    Integer registeredExtColor = ModernerBeta.CONFIG.biomePreviewColors.get(extId);
                    if (registeredExtColor != null) {
                        return registeredExtColor;
                    }
                }

                Integer registeredColor = ModernerBeta.CONFIG.biomePreviewColors.get(id);
                if (registeredColor != null) {
                    return registeredColor;
                }

                Biome biome = biomeEntry.value();
                boolean watery = biomeEntry.isIn(BiomeTags.IS_OCEAN) || biomeEntry.isIn(BiomeTags.IS_RIVER);
                int color = -1;
                if (watery) {
                    color = biome.getWaterColor();
                    if (biomeEntry.isIn(BiomeTags.IS_OCEAN)) {
                        color = (color & 0xFEFEFE) >> 1;
                        if (biomeEntry.isIn(BiomeTags.IS_DEEP_OCEAN)) {
                            color = (color & 0xFEFEFE) >> 1;
                        }
                    }
                } else {
                    SurfaceConfig surfaceConfig = SurfaceConfig.getSurfaceConfig(biomeEntry);
                    BlockState topBlock = surfaceConfig.normal().topBlock();
                    if (topBlock.isOf(Blocks.GRASS_BLOCK)) {
                        color = biome.getGrassColorAt(x, y);
                    } else {
                        color = topBlock.getMapColor(null, new BlockPos(x, 64, y)).color;
                    }
                }

                float contrast = 2.0F;
                float[] hsb = new float[3];
                Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, hsb);
                hsb[1] = MathHelper.clamp(hsb[1] * contrast, 0.0F, 1.0F);
                hsb[2] = MathHelper.clamp((hsb[2] - 0.5F) * contrast + 0.5F, 0.0F, 1.0F);

                if (biome.getTemperature() < 0.15F && !biomeEntry.isIn(BiomeTags.IS_DEEP_OCEAN)) {
                    hsb[1] *= 0.1F;
                    hsb[2] = hsb[2] * 0.25F + 0.75F;
                }

                return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
            }
        }
    }
}
