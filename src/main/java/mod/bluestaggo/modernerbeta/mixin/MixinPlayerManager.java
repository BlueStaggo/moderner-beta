package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.network.BiomeProviderInfoPayload;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
    @SuppressWarnings("DiscouragedShift")
    @Inject(
            method = "sendWorldInfo",
            at = @At(
                    value = "INVOKE",
                    //? if >=1.20.5 {
                    target = "Lnet/minecraft/server/ServerTickManager;sendPackets(Lnet/minecraft/server/network/ServerPlayerEntity;)V",
                    //?} else {
                    /*target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V",
                    ordinal = 0,
                    *///?}
                    shift = At.Shift.BEFORE
            )
    )
    private void sendBiomeProviderInfo(ServerPlayerEntity player, ServerWorld world, CallbackInfo ci) {
        if (ModernerBeta.networkHelper == null)
            throw new RuntimeException("Lousy porter did NOT make a network helper!");

        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
        BiomeSource biomeSource = chunkGenerator.getBiomeSource();

        boolean isModernBeta = chunkGenerator instanceof ModernBetaChunkGenerator || biomeSource instanceof ModernBetaBiomeSource;

        BiomeProviderInfoPayload payload;
        if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            //FIXME: I hate this
            BiomeProvider provider = modernBetaBiomeSource.getBiomeProvider();
            Identifier id = ModernBetaRegistries.BIOME.getEntrySet().stream()
                    .filter(c -> c.getValue().providerClass() == provider.getClass())
                    .findFirst().orElseThrow().getKey().getValue();

            payload = new BiomeProviderInfoPayload(
                    true,
                    true,
                    Optional.of(world.getSeed()),
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
