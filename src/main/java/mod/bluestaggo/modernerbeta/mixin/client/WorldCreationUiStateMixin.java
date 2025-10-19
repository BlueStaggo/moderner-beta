package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaLevelScreen;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaLevelScreenProvider;
import mod.bluestaggo.modernerbeta.world.preset.ModernBetaLevelPresets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(WorldCreationUiState.class)
public abstract class WorldCreationUiStateMixin {
    @Inject(method = "getPresetEditor", at = @At("RETURN"), cancellable = true)
    public void injectGetPresetEditor(CallbackInfoReturnable<PresetEditor> info) {
        Holder<WorldPreset> preset = this.getWorldType().preset();
        ResourceKey<WorldPreset> modernBeta = ModernBetaLevelPresets.MODERN_BETA;
        
        if (preset != null && preset.unwrapKey().isPresent() && preset.unwrapKey().get().equals(modernBeta)) {
            info.setReturnValue(
                (parent, generatorOptionsHolder) -> new ModernBetaLevelScreen(
                    parent,
                    generatorOptionsHolder,
                    (settingsChunk, settingsBiome, settingsCaveBiome) -> parent.getUiState().updateDimensions(
                        ModernBetaLevelScreenProvider.createModifier(
                            settingsChunk,
                            settingsBiome,
                            settingsCaveBiome
                        )
                    )
                )
            );
        }
    }
    
    @Shadow
    public abstract WorldCreationUiState.WorldTypeEntry getWorldType();
}
