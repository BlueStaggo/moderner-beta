package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TrunkPlacerType.class)
public interface AccessorTrunkPlacerType {
    @Invoker("<init>")
    static <P extends TrunkPlacer> TrunkPlacerType<P> create(com.mojang.serialization./*Map*/Codec<P> codec) {
        throw new AssertionError();
    }
}
