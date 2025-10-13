package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FoliagePlacerType.class)
public interface AccessorFoliagePlacerType {
    @Invoker("<init>")
    static <P extends FoliagePlacer> FoliagePlacerType<P> create(com.mojang.serialization./*Map*/Codec<P> codec) {
        throw new AssertionError();
    }
}
