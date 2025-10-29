package mod.bluestaggo.modernerbeta.api.world.chunk.surface;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public record SurfaceConfig(SurfaceBlocks normal, SurfaceBlocks beachSand, SurfaceBlocks beachGravel) {
    public static final Codec<SurfaceConfig> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            SurfaceBlocks.CODEC.fieldOf("normal").forGetter(SurfaceConfig::normal),
            SurfaceBlocks.CODEC.fieldOf("beachSand").forGetter(SurfaceConfig::beachSand),
            SurfaceBlocks.CODEC.fieldOf("beachGravel").forGetter(SurfaceConfig::beachGravel)
        ).apply(instance, SurfaceConfig::new)
    );

    public SurfaceConfig(SurfaceBlocks surfaceBlocks) {
        this(surfaceBlocks, surfaceBlocks, surfaceBlocks);
    }
    
    public static final SurfaceConfig DEFAULT = new SurfaceConfig(SurfaceBlocks.GRASS, SurfaceBlocks.SAND, SurfaceBlocks.GRAVEL);

    public static SurfaceConfig getSurfaceConfig(Holder<Biome> biome, HolderLookup<SurfaceConfig> surfaceConfigLookup) {
        if (surfaceConfigLookup == null) {
            return DEFAULT;
        }

        Optional<Holder.Reference<SurfaceConfig>> optionalKey = surfaceConfigLookup./**/listElements()
            .filter(entry -> entry.isBound() && biome.is(
                TagKey.create(Registries.BIOME, VersionCompat.id(
                    entry.key().location().getNamespace()
                    + ":surface_config/"
                    + entry.key().location().getPath()
                ))))
            .findFirst();

        if (optionalKey.isPresent()) {
            return optionalKey.get().value();
        }

        return DEFAULT;
    }
}