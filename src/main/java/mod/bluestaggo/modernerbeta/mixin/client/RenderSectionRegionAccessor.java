package mod.bluestaggo.modernerbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
    //? if >=1.21.6 {
    net.minecraft.client.renderer.chunk.RenderSectionRegion.class
    //?} else {
    /*net.minecraft.client.renderer.chunk.RenderChunkRegion.class
    *///?}
)
public interface RenderSectionRegionAccessor {
    @Accessor
    //? if >=26.1 {
    net.minecraft.client.multiplayer.ClientLevel
    //? } else {
    /*net.minecraft.world.level.Level
    *///? }
    getLevel();
}
