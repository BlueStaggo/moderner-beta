package mod.bluestaggo.modernerbeta.mixin.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import mod.bluestaggo.modernerbeta.level.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.level.chunk.ModernBetaNoiseGeneratorSettings;
import mod.bluestaggo.modernerbeta.level.preset.ModernBetaWorldPresets;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(targets = "net/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData")
public abstract class WorldDimensionDataMixin {
    @SuppressWarnings("LoggerInitializedWithForeignClass")
    @Unique private final static Logger modernBeta$LOGGER = LoggerFactory.getLogger(DedicatedServerProperties.class.getName());
    @Shadow public abstract JsonObject generatorSettings();

    @Inject(method = "create", at = @At("TAIL"), cancellable = true)
    private void injectCustomSettings(
        net.minecraft.core./*? >=1.21.2 {*/HolderLookup.Provider/*?} else {*//*RegistryAccess*//*?}*/ registries,
        CallbackInfoReturnable<WorldDimensions> cir,
        @Local Holder<WorldPreset> presetHolder,
        @Local WorldDimensions worldDimensions
    ) {
        if (presetHolder.is(ModernBetaWorldPresets.MODERN_BETA)) {
            RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, registries);
            Optional<ModernBetaSettingsPreset> optional = ModernBetaSettingsPreset.SETTINGS_TEXT_CODEC
                    .parse(new Dynamic<>(registryOps, this.generatorSettings()))
                    .resultOrPartial(modernBeta$LOGGER::error);

            optional.ifPresent(settingsPreset -> {
                HolderGetter<ModernBetaSettingsPreset> presetRegistry = registries.lookupOrThrow(ModernBetaResourceKeys.SETTINGS_PRESET);

                cir.setReturnValue(worldDimensions.replaceOverworldGenerator(registries,
                    new ModernBetaChunkGenerator(
                        new ModernBetaBiomeSource(
                            registries.lookupOrThrow(Registries.BIOME),
                            presetRegistry,
                            settingsPreset.biomeSettings(),
                            settingsPreset.caveBiomeSettings()
                        ),
                        presetRegistry,
                        registries.lookupOrThrow(ModernBetaResourceKeys.SURFACE_CONFIG),
                        settingsPreset.chunkSettings()
                    )
                ));
            });
        }
    }
}
