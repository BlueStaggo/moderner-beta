package mod.bluestaggo.modernerbeta.level.biome.injection;

import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.WorldBorderLocation;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldGenerationContext;

import java.util.EnumSet;

public final class BiomeInjectionContext {
    public final ModernBetaChunkGenerator chunkGenerator;
    public final ModernBetaBiomeSource biomeSource;

    public WorldBorderLocation borderLocation;

    public WorldGenerationContext context;
    public int topHeight;
    public int minHeight;

    private ChunkPos chunkPos;
    private int x;
    private int y;
    private int z;

    private Holder<Biome> biome;
    private EnumSet<InjectionNeeds> fulfillableNeeds;

    public BiomeInjectionContext(ModernBetaChunkGenerator chunkGenerator, ModernBetaBiomeSource biomeSource) {
        this.chunkGenerator = chunkGenerator;
        this.biomeSource = biomeSource;
    }

    public BiomeInjectionContext setHeights(int topHeight, int minHeight) {
        this.topHeight = topHeight;
        this.minHeight = minHeight;
        this.y = topHeight;

        return this;
    }

    public BiomeInjectionContext setupWorldGenContext(LevelHeightAccessor accessor) {
        if (this.context == null) {
            this.context = new WorldGenerationContext(this.chunkGenerator, accessor);
        }

        return this;
    }

    public BiomeInjectionContext setChunkPos(ChunkPos chunkPos) {
        this.chunkPos = chunkPos;
        return this;
    }

    public BiomeInjectionContext setPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.chunkPos = new ChunkPos(
            SectionPos.blockToSectionCoord(x),
            SectionPos.blockToSectionCoord(z)
        );

        return this;
    }

    public BiomeInjectionContext setBiome(Holder<Biome> biome) {
        this.biome = biome;
        return this;
    }

    public BiomeInjectionContext setFulfillableNeeds(EnumSet<InjectionNeeds> ableToFulfill) {
        this.fulfillableNeeds = ableToFulfill;
        return this;
    }

    public void setupContext() {
        ModernBetaSettings settingsChunk = this.chunkGenerator.getChunkSettings()
                .mapPreset(this.chunkGenerator.getPresetRegistry(), ModernBetaSettingsPreset::chunkSettings);

        this.borderLocation = settingsChunk.getOrDefault(SettingsComponentTypes.WORLD_BORDER);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public int getMinGenY() {
        return this.context.getMinGenY();
    }

    public int getGenDepth() {
        return this.context.getGenDepth();
    }

    public ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    public Holder<Biome> getBiome() {
        return this.biome;
    }

    public EnumSet<InjectionNeeds> getFulfillableNeeds() {
        return this.fulfillableNeeds;
    }
}
