package mod.bluestaggo.modernerbeta.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
//? if >=1.21.6 {
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
//?} else {
/*import net.minecraft.client.renderer.chunk.RenderChunkRegion;
*///?}
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(
    //? if >=1.21.6 {
    RenderSectionRegion.class
    //?} else {
    /*RenderChunkRegion.class
    *///?}
)
public interface RenderSectionRegionAccessor {
    @Accessor
    Level getLevel();
}
