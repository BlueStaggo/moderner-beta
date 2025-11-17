package mod.bluestaggo.modernerbeta.client;

import net.minecraft.util.Mth;

public class FogUtils {
    public static float calculateFogWeight(int renderDistance /*? >=1.21.11 {*//*, net.minecraft.client.Camera camera, float partialTick*//*?}*/) {
        // Old fog formula with old render distance: weight = 1.0F / (float)(4 - renderDistance)
        // where renderDistance is 0-3, 0 being 'Far' and 3 being 'Very Short'

        int clampedDistance = Mth.clamp(renderDistance, 4, 16);
        clampedDistance -= 4;
        clampedDistance /= 4;

        float oldRenderDistance = Math.abs(clampedDistance - 3);
        //? if >=1.21.11 {
        /*oldRenderDistance = Math.min(camera.attributeProbe()
                .getValue(net.minecraft.world.attribute.EnvironmentAttributes.SKY_FOG_END_DISTANCE, partialTick) / 16.0F, oldRenderDistance);
        *///? }
        
        return 1.0F / (4 - oldRenderDistance);
    }
}
