package mod.bluestaggo.modernerbeta.client.color;

import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSamplerSky;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class SkyColorSampler {
    public static final SkyColorSampler INSTANCE = new SkyColorSampler();

    private Optional<ClimateSamplerSky> climateSampler = Optional.empty();

    public Optional<ClimateSamplerSky> getClimateSampler() {
        return this.climateSampler;
    }

    public void setClimateSampler(ClimateSamplerSky climateSampler) {
        this.climateSampler = Optional.ofNullable(climateSampler);
    }

    public Vec3d getSkyColor(Vec3d cameraPos, Vec3d skyColorVec) {
        if (this.useSkyColor()) {
            int x = (int)cameraPos.getX();
            int z = (int)cameraPos.getZ();

            float temp = (float)this.climateSampler.get().sampleSky(x, z);
            temp /= 3F;
            temp = MathHelper.clamp(temp, -1F, 1F);

            return Vec3d.unpackRgb(MathHelper.hsvToRgb(0.6222222F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F));
        }

        return skyColorVec;
    }

    public boolean useSkyColor() {
        return this.climateSampler.isPresent() && this.climateSampler.get().useSkyColor();
    }
}
