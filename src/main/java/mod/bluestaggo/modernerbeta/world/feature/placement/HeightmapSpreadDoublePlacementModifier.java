package mod.bluestaggo.modernerbeta.world.feature.placement;

import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class HeightmapSpreadDoublePlacementModifier extends PlacementModifier {
    public static final com.mojang.serialization./*Map*/Codec<HeightmapSpreadDoublePlacementModifier> MODIFIER_CODEC = VersionCompat.createMaybeMapCodec(
        instance -> instance.group(
            Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(arg -> arg.heightmap)
        ).apply(instance, HeightmapSpreadDoublePlacementModifier::of));
    
    private final Heightmap.Types heightmap;
    
    private HeightmapSpreadDoublePlacementModifier(Heightmap.Types heightmap) {
        this.heightmap = heightmap;
    }
    
    public static HeightmapSpreadDoublePlacementModifier of(Heightmap.Types heightmap) {
        return new HeightmapSpreadDoublePlacementModifier(heightmap);
    }
    
    @Override
    public @NotNull Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();
        
        int y = context.getHeight(this.heightmap, x, z);
        if (y == context.getMinY()) {
            return Stream.of(new BlockPos[0]);
        }
        
        return Stream.of(new BlockPos(x, context.getMinY() + random.nextInt((y - context.getMinY()) * 2), z));
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return ModernBetaPlacementTypes.HEIGHTMAP_SPREAD_DOUBLE;
    }

}
