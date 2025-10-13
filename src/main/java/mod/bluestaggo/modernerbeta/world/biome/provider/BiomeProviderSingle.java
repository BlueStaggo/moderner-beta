package mod.bluestaggo.modernerbeta.world.biome.provider;

import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public class BiomeProviderSingle extends BiomeProvider {
    private final ResourceKey<Biome> biomeKey;
    
    public BiomeProviderSingle(ModernBetaSettings settings, HolderGetter<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);

        this.biomeKey = ResourceKey.create(Registries.BIOME, this.settings.getOrThrow(SettingsComponentTypes.SINGLE_BIOME));
    }

    @Override
    public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        return this.biomeRegistry.getOrThrow(this.biomeKey);
    }
    
    @Override
    public List<Holder<Biome>> getBiomes() {
        return List.of(this.biomeRegistry.getOrThrow(this.biomeKey));
    }
}
