package mod.bluestaggo.modernerbeta.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LevelChunkSection.class)
public interface AccessorChunkSection {
    @Accessor("biomes")
    void setBiomes(PalettedContainerRO<Holder<Biome>> biomes);
}
