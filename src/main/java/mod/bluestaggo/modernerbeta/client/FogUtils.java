package mod.bluestaggo.modernerbeta.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class FogUtils {
    public static float calculateFogWeight(int renderDistance) {
        // Old fog formula with old render distance: weight = 1.0F / (float)(4 - renderDistance)
        // where renderDistance is 0-3, 0 being 'Far' and 3 being 'Very Short'

        int clampedDistance = Mth.clamp(renderDistance, 4, 16);
        clampedDistance -= 4;
        clampedDistance /= 4;

        int oldRenderDistance = Math.abs(clampedDistance - 3);

        float weight = 1.0F / (float)(4 - oldRenderDistance);
        weight = 1.0F - (float)Math.pow(weight, 0.25);

        return weight;
    }
}
