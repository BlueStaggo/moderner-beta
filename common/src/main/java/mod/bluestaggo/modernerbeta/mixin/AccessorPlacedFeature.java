package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PlacedFeature.class)
public interface AccessorPlacedFeature {
    @Accessor
    List<PlacementModifier> getPlacementModifiers();
}
