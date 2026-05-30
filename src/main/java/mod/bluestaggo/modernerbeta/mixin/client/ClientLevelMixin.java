//? if <1.21.11 || >=26.2 {
/*package mod.bluestaggo.modernerbeta.mixin.client;

//? if >=26.2 {
/^import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaLevel;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
^///? } else if <1.21.11 {
/^import mod.bluestaggo.modernerbeta.client.color.SkyColorSampler;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
^///? }
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ClientLevel.class, priority = 1)
public abstract class ClientLevelMixin {
    //? <1.21.11 {
    /^@ModifyVariable(method = "getSkyColor", at = @At("STORE"), ordinal = 1)
    private Vec3 injectGetSkyColor(Vec3 originalColor, Vec3 pos, float f2) {
        SkyColorSampler sampler = SkyColorSampler.INSTANCE;

        if (sampler.useSkyColor())
            return SkyColorSampler.INSTANCE.getSkyColor(pos);

        return originalColor;
    }
    ^///? }

    //? if >=26.2 {
    /^@WrapOperation(
        method = "getPrecipitationAt",
        at = @At(
            value = "INVOKE",
            target = VersionCompat.BIOME_GET_PRECIPITATION_TARGET
        )
    )
    public Biome.Precipitation modifyTickPrecipitation(Biome biome, BlockPos blockPos, int seaLevel,
                                                       Operation<Biome.Precipitation> original) {
        ModernBetaLevel modernBetaLevel = (ModernBetaLevel) this;

        if (!modernBetaLevel.modernerBeta$isModded()) {
            return original.call(biome, blockPos, seaLevel);
        }

        return modernBetaLevel.modernerBeta$samplePrecipitation(biome, blockPos);
    }
    ^///? }
}
*///? }