package mod.bluestaggo.modernerbeta.client.color;

import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSamplerSky;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class SkyColorSampler {
    public static final SkyColorSampler INSTANCE = new SkyColorSampler();

    private ClimateSamplerSky climateSampler = null;

    public ClimateSamplerSky getClimateSampler() {
        return this.climateSampler;
    }

    public void setClimateSampler(ClimateSamplerSky climateSampler) {
        this.climateSampler = climateSampler;
    }

    public /*? >=1.21.11 {*//*int*//*?} else {*/Vec3/*?}*/ getSkyColor(Vec3 cameraPos) {
        int x = (int)cameraPos.x();
        int z = (int)cameraPos.z();

        float temp = (float)this.climateSampler.sampleSky(x, z);
        temp /= 3F;
        temp = Mth.clamp(temp, -1F, 1F);

        int color = Mth.hsvToRgb(0.6222222F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F);
        //? if >=1.21.11 {
        /*return color;
        *///? } else {
        return Vec3.fromRGB24(color);
        //? }
    }

    public boolean useSkyColor() {
        return this.climateSampler != null && this.climateSampler.useSkyColor();
    }
}
