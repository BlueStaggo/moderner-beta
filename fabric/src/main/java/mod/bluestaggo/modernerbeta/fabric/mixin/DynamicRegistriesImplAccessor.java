package mod.bluestaggo.modernerbeta.fabric.mixin;

import net.fabricmc.fabric.impl.registry.sync.DynamicRegistriesImpl;
import net.minecraft.resources.RegistryDataLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@Mixin(value = DynamicRegistriesImpl.class, remap = false)
public interface DynamicRegistriesImplAccessor {
    @Accessor(value = "DYNAMIC_REGISTRIES", remap = false)
    static List<RegistryDataLoader.RegistryData<?>> getDynamicRegistries() {
        throw new AssertionError("Mixin failed");
    }
}
