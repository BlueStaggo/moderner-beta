package mod.bluestaggo.modernerbeta.compat.client.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public interface ColorModifierHelper {
    int modifyGrass(int original, Holder<Biome> biome, BlockPos pos);
    int modifyFoliage(int original, Holder<Biome> biome, BlockPos pos);
}
