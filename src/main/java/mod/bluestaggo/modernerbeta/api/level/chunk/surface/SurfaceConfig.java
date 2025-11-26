//~dotLocation
package mod.bluestaggo.modernerbeta.api.level.chunk.surface;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public record SurfaceConfig(SurfaceBlocks normal, SurfaceBlocks beachSand, SurfaceBlocks beachGravel, TagKey<Biome> biomeTag) {
    public static final Codec<SurfaceConfig> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            SurfaceBlocks.CODEC.fieldOf("normal").forGetter(SurfaceConfig::normal),
            SurfaceBlocks.CODEC.fieldOf("beachSand").forGetter(SurfaceConfig::beachSand),
            SurfaceBlocks.CODEC.fieldOf("beachGravel").forGetter(SurfaceConfig::beachGravel),
            TagKey.codec(Registries.BIOME).fieldOf("biomeTag").forGetter(SurfaceConfig::biomeTag)
        ).apply(instance, SurfaceConfig::new)
    );

    public SurfaceConfig(SurfaceBlocks surfaceBlocks, TagKey<Biome> biomeTag) {
        this(surfaceBlocks, surfaceBlocks, surfaceBlocks, biomeTag);
    }
    
    public static final SurfaceConfig DEFAULT = new SurfaceConfig(SurfaceBlocks.GRASS, SurfaceBlocks.SAND, SurfaceBlocks.GRAVEL, null);

    public static SurfaceConfig getSurfaceConfig(Holder<Biome> biome, HolderLookup<SurfaceConfig> surfaceConfigLookup) {
        if (surfaceConfigLookup == null) {
            return DEFAULT;
        }

        Optional<Holder.Reference<SurfaceConfig>> optionalKey = surfaceConfigLookup./**/listElements()
            .filter(entry -> entry.isBound() &&
                    entry.value().biomeTag != null && biome.is(entry.value().biomeTag))
            .findFirst();

        return optionalKey.map(Holder.Reference::value).orElse(DEFAULT);
    }
}