package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Biome.class)
public interface AccessorBiome {
    @Accessor
    Biome.ClimateSettings getClimateSettings();

    @Invoker
    float invokeGetTemperature(
        BlockPos pos
        //? if >=1.21.2
        , int seaLevel
    );
}
