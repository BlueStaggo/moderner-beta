package mod.bluestaggo.modernerbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

@Mixin(PlacedFeature.class)
public interface AccessorPlacedFeature {
    @Accessor
    List<PlacementModifier> getPlacement();
}
