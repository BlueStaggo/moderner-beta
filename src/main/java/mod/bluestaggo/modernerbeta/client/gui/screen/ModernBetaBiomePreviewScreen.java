//~dotLocation
package mod.bluestaggo.modernerbeta.client.gui.screen;

import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.ints.Int2IntAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.mixin.client.ScreenshotAccessor;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverExtendedId;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverExtendedIdStepped;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverStepped;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.layers.LayerRandom;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.RandomSupport;
import org.slf4j.event.Level;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModernBetaBiomePreviewScreen extends ModernBetaScreen {
    private final BiomeProvider biomeProvider;
    private final HolderLookup<SurfaceConfig> surfaceConfigLookup;
    private BiomeDisplayWidget biomeDisplay;
    private volatile String exceptionMessage;

    public ModernBetaBiomePreviewScreen(Component title, Screen parent, WorldCreationContext context, ModernBetaSettings biomeSettings) {
        super(title, parent);

        BiomeProvider biomeProvider = null;
        HolderLookup<SurfaceConfig> surfaceConfigLookup = null;
        try {
            biomeProvider = ModernBetaRegistries.BIOME
                //? if >=1.21.2 {
                .getValue
                //? } else {
                /*.get
                *///? }
                    (biomeSettings.getProvider())
                .apply(
                    biomeSettings,
                    context.worldgenLoadContext().lookupOrThrow(Registries.BIOME),
                    context.options().seed()
                );
            surfaceConfigLookup = context.worldgenLoadContext().lookupOrThrow(ModernBetaResourceKeys.SURFACE_CONFIG);
        } catch (Exception exception) {
            exception.printStackTrace();
            this.exceptionMessage = exception.getLocalizedMessage();
        }

        this.biomeProvider = biomeProvider;
        this.surfaceConfigLookup = surfaceConfigLookup;
    }

    @Override
    protected void init() {
        super.init();

        if (this.biomeDisplay != null) {
            this.biomeDisplay.close();
        }
        this.biomeDisplay = new BiomeDisplayWidget(0, 0, this.width * 3 / 4, this.height * 3 / 4);

        boolean hasSteps = this.biomeProvider instanceof BiomeResolverStepped;

        Button buttonZoomOut = Button.builder(Component.literal("-"), button ->
            this.biomeDisplay.zoomOut()
        ).bounds(0, 0, 20, 20).build();
        Button buttonZoomIn = Button.builder(Component.literal("+"), button ->
            this.biomeDisplay.zoomIn()
        ).bounds(0, 0, 20, 20).build();
        Button buttonScreenshot = Button.builder(Component.translatable("createWorld.customize.modern_beta.settings.screenshot"), button ->
            this.biomeDisplay.saveScreenshot()
        ).bounds(0, 0, 100, 20).build();
        Button buttonBack = Button.builder(CommonComponents.GUI_BACK, button ->
            this.minecraft.setScreen(this.parent)
        ).bounds(0, 0, 100, 20).build();

        GridLayout gridWidgetMain = this.createGridWidget();
        GridLayout gridWidgetButtons = this.createGridWidget();
        gridWidgetMain.defaultCellSetting().alignHorizontallyCenter().alignVerticallyMiddle();

        GridLayout.RowHelper gridAdderMain = gridWidgetMain.createRowHelper(1);
        GridLayout.RowHelper gridAdderButtons = gridWidgetButtons.createRowHelper(hasSteps ? 6 : 4);

        gridAdderMain.addChild(this.biomeDisplay);
        gridAdderMain.addChild(gridWidgetButtons);

        gridAdderButtons.addChild(buttonZoomOut);
        gridAdderButtons.addChild(buttonZoomIn);

        if (hasSteps) {
            BiomeResolverStepped stepResolver = (BiomeResolverStepped) this.biomeProvider;
            int stepCount = stepResolver.getStepCount();
            this.biomeDisplay.step.set(stepCount - 1);

            Button buttonPrevStep = Button.builder(Component.literal("◀"), button -> {
                this.biomeDisplay.step.getAndUpdate(i -> Math.floorMod(i - 1, stepCount));
                this.biomeDisplay.clear();
            }).bounds(0, 0, 20, 20).build();
            Button buttonNextStep = Button.builder(Component.literal("▶"), button -> {
                this.biomeDisplay.step.getAndUpdate(i -> Math.floorMod(i + 1, stepCount));
                this.biomeDisplay.clear();
            }).bounds(0, 0, 20, 20).build();

            gridAdderButtons.addChild(buttonPrevStep);
            gridAdderButtons.addChild(buttonNextStep);
        }

        gridAdderButtons.addChild(buttonScreenshot);
        gridAdderButtons.addChild(buttonBack);

        gridWidgetMain.arrangeElements();
        FrameLayout.alignInRectangle(gridWidgetMain, 0, 0, this.width, this.height, 0.5f, 1.0f);
        gridWidgetMain.visitWidgets(this::addRenderableWidget);

        this.biomeDisplay.startRenderThread();
    }

    @Override
    public void removed() {
        this.biomeDisplay.close();
    }

    class BiomeDisplayWidget extends AbstractWidget implements AutoCloseable {
        static final ResourceLocation TEXTURE_ID = ModernerBeta.createId("biome_preview");
        static final int EMPTY_COLOR = 0x7F000000;

        final TextureManager textureManager;
        final NativeImage image;
        final DynamicTexture texture;
        final BiomeRenderThread renderThread;
        final AtomicInteger zoomOut = new AtomicInteger(1);
        final AtomicInteger zoomIn = new AtomicInteger(1);
        final AtomicInteger step = new AtomicInteger();

        int prevMouseX, prevMouseY;
        final AtomicDouble offsetX = new AtomicDouble();
        final AtomicDouble offsetY = new AtomicDouble();

        BiomeDisplayWidget(int x, int y, int width, int height) {
            super(x, y, width, height, Component.empty());
            assert minecraft != null;

            this.image = new NativeImage(width, height, false);
            this.image.fillRect(0, 0, width, height, EMPTY_COLOR);
            this.texture = new DynamicTexture(
                //? if >=1.21.5
                TEXTURE_ID::toString,
                this.image
            );
            this.texture.upload();
            this.textureManager = minecraft.getTextureManager();
            this.textureManager.register(TEXTURE_ID, this.texture);
            this.renderThread = new BiomeRenderThread();
        }

        void clear() {
            synchronized (this.image) {
                this.image.fillRect(0, 0, width, height, EMPTY_COLOR);
            }
            synchronized (this) {
                this.notify();
            }
        }

        void zoomOut() {
            if (this.zoomIn.get() > 1) {
                this.zoomIn.getAndUpdate(i -> i / 2);
            } else if (this.zoomOut.get() < 0x1_00_00_00) {
                this.zoomOut.getAndUpdate(i -> i * 2);
            } else {
                return;
            }

            this.offsetX.getAndUpdate(i -> i / 2);
            this.offsetY.getAndUpdate(i -> i / 2);
            this.clear();
        }

        void zoomIn() {
            if (this.zoomOut.get() <= 1) {
                if (this.zoomIn.get() >= 0x1_00_00_00) return;
                this.zoomIn.getAndUpdate(i -> i * 2);
            } else {
                this.zoomOut.getAndUpdate(i -> i / 2);
            }

            this.offsetX.getAndUpdate(i -> i * 2);
            this.offsetY.getAndUpdate(i -> i * 2);
            this.clear();
        }

        void startRenderThread() {
            this.renderThread.start();
        }

        void saveScreenshot() {
            assert minecraft != null;
            File screenshotDirectory = new File(minecraft.gameDirectory, "screenshots");
            screenshotDirectory.mkdir();
            File screenshotPath = ScreenshotAccessor.invokeGetFile(screenshotDirectory);

            Util.ioPool().execute(() -> {
                try {
                    image.writeToFile(screenshotPath);
                } catch (Exception exception) {
                    ModernerBeta.log(Level.WARN, "Couldn't save screenshot: " + exception);
                }
            });
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float deltaTicks) {
            int step = this.step.get();

            if (this.renderThread.uploadRequested) {
                synchronized (this.image) {
                    this.texture.upload();
                }
            }

            graphics.blit(
                //? if >= 1.21.6 {
                net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
                //?} else if >=1.21.2 {
                /*net.minecraft.client.renderer.RenderType::guiTextured,
                *///?}
                TEXTURE_ID,
                this.getX(), this.getY(),
                0.0F, 0.0F,
                this.getWidth(), this.getHeight(),
                this.getWidth(), this.getHeight()
            );

            if (biomeProvider instanceof BiomeResolverStepped resolverStepped) {
                MutableComponent stepName = Component.literal((step + 1) + "/" + resolverStepped.getStepCount() + " - ");
                stepName.append(resolverStepped.getStepName(step));
                graphics.fill(this.getX(), this.getY(), this.getX() + font.width(stepName) + 8, this.getY() + 16, 0xAA000000);
                graphics.drawString(font, stepName, this.getX() + 4, this.getY() + 4, 0xFFFFFFFF, false);
            }

            double zoomLevel = (double)this.zoomOut.get() * 4.0 / (double)this.zoomIn.get();
            Component zoomLabel = Component.literal("1:" + (zoomLevel % 1.0 == 0.0 ? Integer.toString((int)zoomLevel) : Double.toString(zoomLevel)));
            int zoomLabelWidth = font.width(zoomLabel);
            graphics.fill(this.getX() + this.getWidth() - zoomLabelWidth - 8, this.getY(), this.getX() + this.getWidth(), this.getY() + 16, 0xAA000000);
            graphics.drawString(font, zoomLabel, this.getX() + this.getWidth() - zoomLabelWidth - 4, this.getY() + 4, 0xFFFFFFFF, false);

            if (exceptionMessage != null) {
                graphics.drawCenteredString(
                    font,
                    Component.literal(exceptionMessage)
                        .withStyle(ChatFormatting.RED),
                    this.getX() + this.getWidth() / 2,
                    this.getY() + this.getHeight() / 2 - 4,
                    0xFFFFFFFF
                );
            }

            int offsetMouseX = mouseX - this.getX();
            int offsetMouseY = mouseY - this.getY();
            if (biomeProvider != null && offsetMouseX >= 0 && offsetMouseY >= 0 && offsetMouseX < this.width && offsetMouseY < this.height) {
                int sampleX = (offsetMouseX + (int)Math.round(offsetX.get()) - this.width / 2) * this.zoomOut.get() / this.zoomIn.get();
                int sampleY = (offsetMouseY + (int)Math.round(offsetY.get()) - this.height / 2) * this.zoomOut.get() / this.zoomIn.get();
                Component biomeName = biomeProvider instanceof BiomeResolverStepped resolverStepped
                    ? resolverStepped.getBiomeNameForStep(sampleX, 64, sampleY, step)
                    : biomeProvider.getBiomeName(sampleX, 64, sampleY);
                graphics.
                //? if >=1.21.6 {
                setComponentTooltipForNextFrame
                //? } else {
                /*renderComponentTooltip
                *///? }
                (
                    font,
                    List.of(
                        Component.literal((sampleX * 4) + ", " + (sampleY * 4)),
                        biomeName
                    ),
                    mouseX,
                    mouseY
                );

                //? if >=1.21.9
                /*graphics.requestCursor(com.mojang.blaze3d.platform.cursor.CursorTypes.RESIZE_ALL);*/
            }

            this.prevMouseX = mouseX;
            this.prevMouseY = mouseY;
        }

        @Override
        protected void onDrag(/*? if <1.21.9 {*/ double mouseX, double mouseY, /*?} else {*/ /*net.minecraft.client.input.MouseButtonEvent click, *//*?}*/ double deltaX, double deltaY) {
            assert minecraft != null;
            double prevOffsetX = this.offsetX.getAndAdd(-deltaX);
            double prevOffsetY = this.offsetY.getAndAdd(-deltaY);
            int diffOffsetX = (int)Math.round(this.offsetX.get()) - (int)Math.round(prevOffsetX);
            int diffOffsetY = (int)Math.round(this.offsetY.get()) - (int)Math.round(prevOffsetY);

            synchronized (this.image) {
                int[] pixels =
                    //? if >=1.21.2 {
                    this.image.getPixels();
                    //?} else {
                    /*this.image.getPixelsRGBA();
                    *///?}
                int i = 0;
                this.image.fillRect(0, 0, this.width, this.height, 0x7F000000);
                for (int srcY = 0; srcY < this.height; srcY++) {
                    for (int srcX = 0; srcX < this.width; srcX++) {
                        int dstX = srcX - diffOffsetX;
                        int dstY = srcY - diffOffsetY;
                        if (dstX >= 0 && dstX < this.width && dstY >= 0 && dstY < this.height) {
                            this.image.
                                //? if >=1.21.2 {
                                setPixel
                                //?} else {
                                /*setPixelRGBA
                                *///?}
                                (dstX, dstY, pixels[i]);
                        }
                        i++;
                    }
                }
            }
            synchronized (this) {
                this.notify();
            }
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY,
                                     //? if >=1.20.2
                                     double horizontalAmount,
                                     double verticalAmount) {
            if (verticalAmount < 0.0) {
                this.zoomOut();
                return true;
            }
            if (verticalAmount > 0.0) {
                this.zoomIn();
                return true;
            }
            return false;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput builder) {
        }

        @Override
        public void close() {
            this.renderThread.stop = true;
            this.texture.close();
            this.textureManager.release(TEXTURE_ID);
            synchronized (this) {
                this.notify();
            }
        }

        class BiomeRenderThread extends Thread {
            static final Pattern EXT_INT = Pattern.compile("(\\d)+$");

            volatile boolean stop;
            volatile boolean uploadRequested;

            @Override
            public void run() {
                if (biomeProvider == null) {
                    return;
                }

                int genX = -1;
                int genY = 0;
                boolean full = true;
                Int2IntMap randColors = new Int2IntAVLTreeMap();
                Random random = new Random();
                LayerRandom voronoiRandom = new LayerRandom(RandomSupport.generateUniqueSeed());

                try {
                    while (!stop) {
                        genX++;
                        if (genX >= width) {
                            genX = 0;
                            genY++;
                        }
                        if (genY >= height) {
                            genY = 0;
                            if (full) {
                                synchronized (BiomeDisplayWidget.this) {
                                    try {
                                        BiomeDisplayWidget.this.wait();
                                    } catch (InterruptedException e) {
                                        break;
                                    }
                                }
                                if (this.stop) {
                                    break;
                                }
                            }
                            full = true;
                        }

                        int baseAlpha;
                        synchronized (image) {
                            baseAlpha = image.
                                //? if >=1.21.2 {
                                getPixel
                                //?} else {
                                /*getPixelRGBA
                                *///?}
                                (genX, genY) >>> 24;
                        }
                        if (baseAlpha == 0xFF) {
                            continue;
                        }
                        full = false;

                        int scale = zoomOut.get();
                        int voronoiZoom = zoomIn.get();
                        @SuppressWarnings("IntegerDivisionInFloatingPointContext")
                        int gridScale = (int)Math.pow(2, ((int)(Math.log(scale) / Math.log(2)) + 2) / 4 * 4) * voronoiZoom;
                        int intOffX = (int)Math.round(offsetX.get());
                        int intOffY = (int)Math.round(offsetY.get());
                        int sampleX = (genX + intOffX - width / 2) * scale;
                        int sampleY = (genY + intOffY - height / 2) * scale;
                        int gridSampleX = sampleX;
                        int gridSampleY = sampleY;

                        if (voronoiZoom > 1) {
                            float voronoiFactor = voronoiZoom * 0.9F;

                            int scaledSampleX = Math.floorDiv(sampleX, voronoiZoom);
                            int scaledSampleY = Math.floorDiv(sampleY, voronoiZoom);

                            int subSampleX = Math.floorMod(sampleX, voronoiZoom);
                            int subSampleY = Math.floorMod(sampleY, voronoiZoom);

                            voronoiRandom.init(scaledSampleX, scaledSampleY);
                            float n00x = (voronoiRandom.nextInt(1024) / 1024.0F - 0.5F) * voronoiFactor;
                            float n00y = (voronoiRandom.nextInt(1024) / 1024.0F - 0.5F) * voronoiFactor;
                            voronoiRandom.init(scaledSampleX + 1, scaledSampleY);
                            float n10x = (voronoiRandom.nextInt(1024) / 1024.0F - 0.5F) * voronoiFactor + voronoiZoom;
                            float n10y = (voronoiRandom.nextInt(1024) / 1024.0F - 0.5F) * voronoiFactor;
                            voronoiRandom.init(scaledSampleX, scaledSampleY + 1);
                            float n01x = (voronoiRandom.nextInt(1024) / 1024.0F - 0.5F) * voronoiFactor;
                            float n01y = (voronoiRandom.nextInt(1024) / 1024.0F - 0.5F) * voronoiFactor + voronoiZoom;
                            voronoiRandom.init(scaledSampleX + 1, scaledSampleY + 1);
                            float n11x = (voronoiRandom.nextInt(1024) / 1024.0F - 0.5F) * voronoiFactor + voronoiZoom;
                            float n11y = (voronoiRandom.nextInt(1024) / 1024.0F - 0.5F) * voronoiFactor + voronoiZoom;

                            sampleX = scaledSampleX;
                            sampleY = scaledSampleY;

                            float dist00 = Mth.square(subSampleX - n00x) + Mth.square(subSampleY - n00y);
                            float dist10 = Mth.square(subSampleX - n10x) + Mth.square(subSampleY - n10y);
                            float dist01 = Mth.square(subSampleX - n01x) + Mth.square(subSampleY - n01y);
                            float dist11 = Mth.square(subSampleX - n11x) + Mth.square(subSampleY - n11y);

                            if (dist11 < dist10 && dist11 < dist01 && dist11 < dist00) {
                                sampleX++;
                                sampleY++;
                            } else if (dist10 < dist00 && dist10 < dist01 && dist10 < dist11) {
                                sampleX++;
                            } else if (dist01 < dist00 && dist01 < dist10 && dist01 < dist11) {
                                sampleY++;
                            }
                        }

                        int step = BiomeDisplayWidget.this.step.get();
                        Holder<Biome> biome = biomeProvider instanceof BiomeResolverStepped resolverStepped
                            ? resolverStepped.getBiomeForStep(sampleX, 64, sampleY, step)
                            : biomeProvider.getBiome(sampleX, 64, sampleY);
                        ExtendedBiomeId extendedBiome = biomeProvider instanceof BiomeResolverExtendedId resolverExtendedId
                            ? resolverExtendedId instanceof BiomeResolverExtendedIdStepped resolverExtendedIdStepped
                                ? resolverExtendedIdStepped.getExtendedBiomeIdForStep(sampleX, 64, sampleY, step)
                                : resolverExtendedId.getExtendedBiomeId(sampleX, 64, sampleY)
                            : ExtendedBiomeId.NULL;

                        int color = this.getBiomeColor(biome, extendedBiome.ext(), sampleX, sampleY, randColors, random);

                        if (gridSampleX % (64 * gridScale) == 0 || gridSampleY % (64 * gridScale) == 0) {
                            int r = (color >> 16) & 0xFF;
                            int g = (color >> 8) & 0xFF;
                            int b = color & 0xFF;
                            r = Mth.lerpInt(1.0F / 3.0F, r, 0xFF);
                            g = Mth.lerpInt(1.0F / 3.0F, g, 0xFF);
                            b = Mth.lerpInt(1.0F / 3.0F, b, 0xFF);
                            color = r << 16 | g << 8 | b;
                        } else if (gridSampleX % (4 * gridScale) == 0 || gridSampleY % (4 * gridScale) == 0) {
                            int r = (color >> 16) & 0xFF;
                            int g = (color >> 8) & 0xFF;
                            int b = color & 0xFF;
                            r = Mth.lerpInt(0.1F, r, 0xFF);
                            g = Mth.lerpInt(0.1F, g, 0xFF);
                            b = Mth.lerpInt(0.1F, b, 0xFF);
                            color = r << 16 | g << 8 | b;
                        }

                        //? if >=1.21.2 {
                        color |= 0xFF000000;
                        synchronized (image) {
                            image.setPixel(genX, genY, color);
                            this.uploadRequested = true;
                        }
                        //?} else {
                        /*int r = (color >> 16) & 0xFF;
                        int g = (color >> 8) & 0xFF;
                        int b = color & 0xFF;
                        color = r | g << 8 | b << 16 | 0xFF << 24;
                        synchronized (image) {
                            image.setPixelRGBA(genX, genY, color);
                            this.uploadRequested = true;
                        }
                        *///?}
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    exceptionMessage = exception.getLocalizedMessage();
                }
            }

            private int getBiomeColor(Holder<Biome> biomeEntry, String ext, int x, int y, Int2IntMap randColors, Random random) {
                String id = biomeEntry.unwrapKey().map(key -> key.location().toString()).orElse("[unregistered]");
                if (ext != null && !ext.isEmpty()) {
                    String extId = id + "*" + ext;
                    Integer registeredExtColor = ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_BIOME_PREVIEW_COLORS).get(extId);
                    if (registeredExtColor != null) {
                        return registeredExtColor;
                    }

                    Matcher extIntMatcher = EXT_INT.matcher(ext);
                    if (extIntMatcher.find()) {
                        try {
                            int extInt = Integer.parseInt(extIntMatcher.group());
                            return extInt >= 256 ? extInt : randColors.computeIfAbsent(
                                Integer.parseInt(extIntMatcher.group()),
                                i -> random.nextInt(0xFFFFFF)
                            );
                        } catch (Exception ignored) {
                        }
                    }
                }

                Integer registeredColor = ModernerBeta.config.getOrDefault(SettingsComponentTypes.CONFIG_BIOME_PREVIEW_COLORS).get(id);
                if (registeredColor != null) {
                    return registeredColor;
                }

                Biome biome = biomeEntry.value();
                boolean watery = biomeEntry.is(BiomeTags.IS_OCEAN) || biomeEntry.is(BiomeTags.IS_RIVER);
                int color;
                if (watery) {
                    color = biome.getWaterColor();
                    if (biomeEntry.is(BiomeTags.IS_OCEAN)) {
                        color = (color & 0xFEFEFE) >> 1;
                        if (biomeEntry.is(BiomeTags.IS_DEEP_OCEAN)) {
                            color = (color & 0xFEFEFE) >> 1;
                        }
                    }
                } else {
                    SurfaceConfig surfaceConfig = SurfaceConfig.getSurfaceConfig(biomeEntry, ModernBetaBiomePreviewScreen.this.surfaceConfigLookup);
                    BlockState topBlock = surfaceConfig.normal().topBlock();
                    if (topBlock.is(Blocks.GRASS_BLOCK)) {
                        color = biome.getGrassColor(x, y);
                    } else {
                        color = topBlock.getMapColor(null, new BlockPos(x, 64, y)).col;
                    }
                }

                float contrast = 2.0F;
                float[] hsb = new float[3];
                Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, hsb);
                hsb[1] = Mth.clamp(hsb[1] * contrast, 0.0F, 1.0F);
                hsb[2] = Mth.clamp((hsb[2] - 0.5F) * contrast + 0.5F, 0.0F, 1.0F);

                if (biome.getBaseTemperature() < 0.15F && !biomeEntry.is(BiomeTags.IS_DEEP_OCEAN)) {
                    hsb[1] *= 0.1F;
                    hsb[2] = hsb[2] * 0.25F + 0.75F;
                }

                return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
            }
        }
    }
}
