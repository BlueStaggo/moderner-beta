package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.network.BiomeProviderInfoPayload;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(method = "sendLevelInfo", at = @At("HEAD"))
    private void sendBiomeProviderInfo(ServerPlayer player, ServerLevel level, CallbackInfo ci) {
        if (ModernerBeta.networkHelper == null)
            throw new RuntimeException("Lousy porter did NOT make a network helper!");

        ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();
        BiomeSource biomeSource = chunkGenerator.getBiomeSource();

        boolean isModernBeta = chunkGenerator instanceof ModernBetaChunkGenerator || biomeSource instanceof ModernBetaBiomeSource;

        BiomeProviderInfoPayload payload;
        if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            //FIXME: I hate this
            BiomeProvider provider = modernBetaBiomeSource.getBiomeProvider();
            ResourceLocation id = ModernBetaRegistries.BIOME.entrySet().stream()
                    .filter(c -> c.getValue().providerClass() == provider.getClass())
                    .findFirst().orElseThrow().getKey().location();

            payload = new BiomeProviderInfoPayload(
                    true,
                    true,
                    Optional.of(level.getSeed()),
                    Optional.of(id),
                    Optional.of(provider.getSettings().toCompound())
            );
        } else {
            payload = new BiomeProviderInfoPayload(
                    isModernBeta,
                    false,
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
            );
        }
        ModernerBeta.networkHelper.sendToPlayer(player, payload);
    }
}
