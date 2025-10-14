package mod.bluestaggo.modernerbeta.world.structure;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class ModernBetaStructureTypes {
    public static IRegistryHandler<StructureType<?>> registryHandler;

    public static StructureType<OceanShrineStructure> OCEAN_SHRINE;

    private static <S extends Structure> StructureType<S> register(String id, com.mojang.serialization.MapCodec<S> codec) {
        return registryHandler.register(ModernerBeta.createId(id), () -> codec);
    }

    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<StructureType<?>>) handler;

        OCEAN_SHRINE = register("ocean_shrine", OceanShrineStructure.CODEC);
    }
}
