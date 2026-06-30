//~minBuild
package mod.bluestaggo.modernerbeta.level.feature.placement;

//? if >=26.3
//import com.mojang.serialization.MapCodec;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
//? if <26.3
import org.jetbrains.annotations.NotNull;

//? if >=26.3
//import java.util.function.Consumer;
//? if <26.3
import java.util.stream.Stream;

//~ if >=26.3 'extends' -> 'implements'
public class HeightmapSpreadDoublePlacementModifier extends PlacementModifier {
    public static final com.mojang.serialization.MapCodec<HeightmapSpreadDoublePlacementModifier> MODIFIER_CODEC = VersionCompat.createMaybeMapCodec(
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
    //? if >=26.3 {
    /*public void modify(PlacementContext context, RandomSource random, BlockPos pos, Consumer<BlockPos> output) {
    *///? } else {
    public @NotNull Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
    //? }
        int x = pos.getX();
        int z = pos.getZ();
        
        int y = context.getHeight(this.heightmap, x, z);
        if (y == context.getMinY()) {
            return /*? if <26.3 {*/ Stream.of(new BlockPos[0]) /*? }*/;
        }

        //? if >=26.3 {
        /*output.accept(
        *///? } else {
        return Stream.of(
        //? }
            new BlockPos(x, context.getMinY() + random.nextInt((y - context.getMinY()) * 2), z)
        );
    }

    //? if >=26.3 {
    /*@Override
    public MapCodec<? extends PlacementModifier> codec() {
        return MODIFIER_CODEC;
    }
    *///? } else {
    @Override
    public @NotNull net.minecraft.world.level.levelgen.placement.PlacementModifierType<?> type() {
        return ModernBetaPlacementTypes.HEIGHTMAP_SPREAD_DOUBLE;
    }
    //? }
}
