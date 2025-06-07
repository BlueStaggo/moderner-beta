package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Biome.class)
public interface AccessorBiome {
    @Accessor
    Biome.Weather getWeather();
}
