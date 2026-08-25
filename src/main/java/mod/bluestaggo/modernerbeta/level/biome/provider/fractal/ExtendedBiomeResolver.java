//~dotLocation
package mod.bluestaggo.modernerbeta.level.biome.provider.fractal;

import mod.bluestaggo.modernerbeta.registry.ExtendedHolder;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public final class ExtendedBiomeResolver {
    private final HolderGetter<Biome> biomeRegistry;
    private final Map<CacheKey, ExtendedHolder<Biome>> cache = new HashMap<>();

    public ExtendedBiomeResolver(HolderGetter<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
    }

    public ExtendedHolder<Biome> resolve(ExtendedIdentifier identifier) {
        CacheKey cacheKey = new CacheKey(identifier.baseId(), identifier.ext(), identifier.weak());
        return this.cache.computeIfAbsent(cacheKey, key -> new ExtendedHolder<>(
            this.getBiome(identifier).orElseThrow(() -> new NoSuchElementException("Biome \"" + identifier.baseId() + "\" does not exist.")),
            identifier
        ));
    }

    @SuppressWarnings("unchecked")
    private Optional<Holder<Biome>> getBiome(ExtendedIdentifier identifier) {
        ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, identifier.baseId());
        return (Optional<Holder<Biome>>)(Object)this.biomeRegistry.get(key);
    }

    private record CacheKey(Identifier baseId, String ext, boolean weak) {
    }
}
