package mod.bluestaggo.modernerbeta.client.gui.screen;

import com.google.common.util.concurrent.AtomicDouble;
import it.unimi.dsi.fastutil.ints.Int2IntAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverExtendedId;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverExtendedIdStepped;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverStepped;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class ModernBetaBiomePreviewScreen extends ModernBetaScreen {
    private final BiomeProvider biomeProvider;
    private BiomeDisplayWidget biomeDisplay;

    public ModernBetaBiomePreviewScreen(Text title, Screen parent, GeneratorOptionsHolder generationOptions, ModernBetaSettings biomeSettings) {
        super(title, parent);
        this.biomeProvider = ModernBetaRegistries.BIOME.get(biomeSettings.getProvider())
            .apply(
                biomeSettings,
                generationOptions.getCombinedRegistryManager()
                    //? if >=1.21.2 {
                    .getOrThrow(RegistryKeys.BIOME),
                    //?} else {
                    /*.getWrapperOrThrow(RegistryKeys.BIOME),
                    *///?}
                generationOptions.generatorOptions().getSeed()
            );
    }

    @Override
    protected void init() {
        super.init();

        if (this.biomeDisplay != null) {
            this.biomeDisplay.close();
        }
        this.biomeDisplay = new BiomeDisplayWidget(0, 0, this.width * 3 / 4, this.height * 3 / 4);

        boolean hasSteps = this.biomeProvider instanceof BiomeResolverStepped;

        ButtonWidget buttonZoomOut = ButtonWidget.builder(Text.literal("-"), button ->
            this.biomeDisplay.zoomOut()
        ).dimensions(0, 0, 20, 20).build();
        ButtonWidget buttonZoomIn = ButtonWidget.builder(Text.literal("+"), button ->
            this.biomeDisplay.zoomIn()
        ).dimensions(0, 0, 20, 20).build();
        ButtonWidget buttonBack = ButtonWidget.builder(ScreenTexts.BACK, button ->
            this.client.setScreen(this.parent)
        ).dimensions(0, 0, 100, 20).build();

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
            this.biomeDisplay.step.set(stepCount - 1);

            ButtonWidget buttonPrevStep = ButtonWidget.builder(Text.literal("◀"), button -> {
                this.biomeDisplay.step.getAndUpdate(i -> Math.floorMod(i - 1, stepCount));
                this.biomeDisplay.clear();
            }).dimensions(0, 0, 20, 20).build();
            ButtonWidget buttonNextStep = ButtonWidget.builder(Text.literal("▶"), button -> {
                this.biomeDisplay.step.getAndUpdate(i -> Math.floorMod(i + 1, stepCount));
                this.biomeDisplay.clear();
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
    public void removed() {
        this.biomeDisplay.close();
    }

    class BiomeDisplayWidget extends ClickableWidget implements AutoCloseable {
        static final Identifier TEXTURE_ID = ModernerBeta.createId("biome_preview");
        static final int EMPTY_COLOR = 0x7F000000;

        final TextureManager textureManager;
        final NativeImage image;
        final NativeImageBackedTexture texture;
        final BiomeRenderThread renderThread;
        final AtomicInteger zoom = new AtomicInteger(1);
        final AtomicInteger step = new AtomicInteger();

        int prevMouseX, prevMouseY;
        final AtomicDouble offsetX = new AtomicDouble();
        final AtomicDouble offsetY = new AtomicDouble();

        BiomeDisplayWidget(int x, int y, int width, int height) {
            super(x, y, width, height, Text.empty());
            assert client != null;

            this.image = new NativeImage(width, height, false);
            this.image.fillRect(0, 0, width, height, EMPTY_COLOR);
            this.texture = new NativeImageBackedTexture(
                //? if >=1.21.5
                TEXTURE_ID::toString,
                this.image
            );
            this.texture.upload();
            this.textureManager = client.getTextureManager();
            this.textureManager.registerTexture(TEXTURE_ID, this.texture);
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
            if (this.zoom.get() >= 0x4000_0000) return;
            this.zoom.getAndUpdate(i -> i * 2);
            this.offsetX.getAndUpdate(i -> i / 2);
            this.offsetY.getAndUpdate(i -> i / 2);
            this.clear();
        }

        void zoomIn() {
            if (this.zoom.get() <= 1) return;
            this.zoom.getAndUpdate(i -> i / 2);
            this.offsetX.getAndUpdate(i -> i * 2);
            this.offsetY.getAndUpdate(i -> i * 2);
            this.clear();
        }

        void startRenderThread() {
            this.renderThread.start();
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            int step = this.step.get();

            if (this.renderThread.uploadRequested) {
                synchronized (this.image) {
                    this.texture.upload();
                }
            }

            context.drawTexture(
                //? if >=1.21.2
                RenderLayer::getGuiTextured,
                TEXTURE_ID,
                this.getX(), this.getY(),
                0.0F, 0.0F,
                this.getWidth(), this.getHeight(),
                this.getWidth(), this.getHeight()
            );

            if (biomeProvider instanceof BiomeResolverStepped resolverStepped) {
                MutableText stepName = Text.literal((step + 1) + "/" + resolverStepped.getStepCount() + " - ");
                stepName.append(resolverStepped.getStepName(step));
                context.fill(this.getX(), this.getY(), this.getX() + textRenderer.getWidth(stepName) + 8, this.getY() + 16, 0xAA000000);
                context.drawText(textRenderer, stepName, this.getX() + 4, this.getY() + 4, 0xFFFFFF, false);
            }

            int offsetMouseX = mouseX - this.getX();
            int offsetMouseY = mouseY - this.getY();
            if (offsetMouseX >= 0 && offsetMouseY >= 0 && offsetMouseX < this.width && offsetMouseY < this.height) {
                int sampleX = (offsetMouseX + (int)Math.round(offsetX.get()) - this.width / 2) * this.zoom.get();
                int sampleY = (offsetMouseY + (int)Math.round(offsetY.get()) - this.height / 2) * this.zoom.get();
                Text biomeName = biomeProvider instanceof BiomeResolverStepped resolverStepped
                    ? resolverStepped.getBiomeNameForStep(sampleX, 64, sampleY, step)
                    : biomeProvider.getBiomeName(sampleX, 64, sampleY);
                context.drawTooltip(
                    textRenderer,
                    List.of(
                        Text.literal((sampleX * 4) + ", " + (sampleY * 4)),
                        biomeName
                    ),
                    mouseX,
                    mouseY
                );
            }

            this.prevMouseX = mouseX;
            this.prevMouseY = mouseY;
        }

        @Override
        protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
            assert client != null;
            double prevOffsetX = this.offsetX.getAndAdd(-deltaX);
            double prevOffsetY = this.offsetY.getAndAdd(-deltaY);
            int diffOffsetX = (int)Math.round(this.offsetX.get()) - (int)Math.round(prevOffsetX);
            int diffOffsetY = (int)Math.round(this.offsetY.get()) - (int)Math.round(prevOffsetY);

            synchronized (this.image) {
                int[] pixels =
                    //? if >=1.21.2 {
                    this.image.copyPixelsArgb();
                    //?} else {
                    /*this.image.copyPixelsRgba();
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
                                setColorArgb
                                //?} else {
                                /*setColor
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
        public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
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
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        }

        @Override
        public void close() {
            this.renderThread.stop = true;
            this.texture.close();
            this.textureManager.destroyTexture(TEXTURE_ID);
            synchronized (this) {
                this.notify();
            }
        }

        class BiomeRenderThread extends Thread {
            static final Pattern EXT_INT = Pattern.compile("(\\d)+$");

            volatile boolean stop;
            volatile boolean uploadRequested;

            int genX = -1;
            int genY;
            boolean full;

            Int2IntMap randColors = new Int2IntAVLTreeMap();
            Random random = new Random();

            @Override
            public void run() {
                while (!stop) {
                    this.genX++;
                    if (this.genX >= width) {
                        this.genX = 0;
                        this.genY++;
                    }
                    if (this.genY >= height) {
                        this.genY = 0;
                        if (this.full) {
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
                        this.full = true;
                    }

                    int baseAlpha;
                    synchronized (image) {
                        baseAlpha = image.
                            //? if >=1.21.2 {
                            getColorArgb
                            //?} else {
                            /*getColor
                            *///?}
                            (this.genX, this.genY) >>> 24;
                    }
                    if (baseAlpha == 0xFF) {
                        continue;
                    }
                    this.full = false;

                    int scale = zoom.get();
                    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
                    int gridScale = (int)Math.pow(2, ((int)(Math.log(scale) / Math.log(2)) + 2) / 4 * 4);
                    int intOffX = (int)Math.round(offsetX.get());
                    int intOffY = (int)Math.round(offsetY.get());
                    int sampleX = (this.genX + intOffX - width / 2) * scale;
                    int sampleY = (this.genY + intOffY - height / 2) * scale;

                    int step = BiomeDisplayWidget.this.step.get();
                    RegistryEntry<Biome> biome = biomeProvider instanceof BiomeResolverStepped resolverStepped
                        ? resolverStepped.getBiomeForStep(sampleX, 64, sampleY, step)
                        : biomeProvider.getBiome(sampleX, 64, sampleY);
                    ExtendedBiomeId extendedBiome = biomeProvider instanceof BiomeResolverExtendedId resolverExtendedId
                        ? resolverExtendedId instanceof BiomeResolverExtendedIdStepped resolverExtendedIdStepped
                            ? resolverExtendedIdStepped.getExtendedBiomeIdForStep(sampleX, 64, sampleY, step)
                            : resolverExtendedId.getExtendedBiomeId(sampleX, 64, sampleY)
                        : ExtendedBiomeId.NULL;
                    int color = this.getBiomeColor(biome, extendedBiome.ext(), sampleX, sampleY);
                    if (sampleX % (64 * gridScale) == 0 || sampleY % (64 * gridScale) == 0) {
                        int r = (color >> 16) & 0xFF;
                        int g = (color >> 8) & 0xFF;
                        int b = color & 0xFF;
                        r = MathHelper.lerp(0.5F, r, 0xFF);
                        g = MathHelper.lerp(0.5F, g, 0xFF);
                        b = MathHelper.lerp(0.5F, b, 0xFF);
                        color = r << 16 | g << 8 | b;
                    } else if (sampleX % (4 * gridScale) == 0 || sampleY % (4 * gridScale) == 0) {
                        int r = (color >> 16) & 0xFF;
                        int g = (color >> 8) & 0xFF;
                        int b = color & 0xFF;
                        r = MathHelper.lerp(0.1F, r, 0xFF);
                        g = MathHelper.lerp(0.1F, g, 0xFF);
                        b = MathHelper.lerp(0.1F, b, 0xFF);
                        color = r << 16 | g << 8 | b;
                    }

                    //? if >=1.21.2 {
                    color |= 0xFF000000;
                    synchronized (image) {
                        image.setColorArgb(this.genX, this.genY, color);
                        this.uploadRequested = true;
                    }
                    //?} else {
                    /*color <<= 8;
                    color |= 0xFF;
                    synchronized (image) {
                        image.setColor(this.genX, this.genY, color);
                        this.uploadRequested = true;
                    }
                    *///?}
                }
            }

            private int getBiomeColor(RegistryEntry<Biome> biomeEntry, String ext, int x, int y) {
                String id = biomeEntry.getIdAsString();
                if (ext != null && !ext.isEmpty()) {
                    String extId = id + "*" + ext;
                    Integer registeredExtColor = ModernerBeta.CONFIG.biomePreviewColors.get(extId);
                    if (registeredExtColor != null) {
                        return registeredExtColor;
                    }

                    Matcher extIntMatcher = EXT_INT.matcher(ext);
                    if (extIntMatcher.find()) {
                        try {
                            int extInt = Integer.parseInt(extIntMatcher.group());
                            return extInt >= 256 ? extInt : this.randColors.computeIfAbsent(
                                Integer.parseInt(extIntMatcher.group()),
                                i -> this.random.nextInt(0xFFFFFF)
                            );
                        } catch (Exception ignored) {
                        }
                    }
                }

                Integer registeredColor = ModernerBeta.CONFIG.biomePreviewColors.get(id);
                if (registeredColor != null) {
                    return registeredColor;
                }

                Biome biome = biomeEntry.value();
                boolean watery = biomeEntry.isIn(BiomeTags.IS_OCEAN) || biomeEntry.isIn(BiomeTags.IS_RIVER);
                int color;
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
