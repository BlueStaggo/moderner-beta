package mod.bluestaggo.modernerbeta.forgelike.mixin;

import net.minecraft.resources.RegistryDataLoader;
//? if neoforge {
import net.neoforged.neoforge.registries.DataPackRegistriesHooks;
//? } else {
/*import net.minecraftforge.registries.DataPackRegistriesHooks;
*///? }
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@Mixin(value = DataPackRegistriesHooks.class, remap = false)
public interface DataPackRegistriesHooksAccessor {
    @Accessor(value = "DATA_PACK_REGISTRIES", remap = false)
    static List<RegistryDataLoader.RegistryData<?>> getDataPackRegistries() {
        throw new AssertionError("Mixin failed");
    }
}
