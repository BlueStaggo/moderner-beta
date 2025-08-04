package mod.bluestaggo.modernerbeta.world.structure;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.structure.StructurePieceType;

import java.util.Locale;

@SuppressWarnings("unused")
public class ModernBetaStructurePieceTypes {
    public static IRegistryHandler<StructurePieceType> registryHandler;

    public static StructurePieceType OCEAN_SHRINE;

    private static StructurePieceType register(StructurePieceType type, String id) {
        return registryHandler.register(ModernerBeta.createId(id.toLowerCase(Locale.ROOT)), type);
    }

    private static StructurePieceType register(StructurePieceType.Simple type, String id) {
        return register((StructurePieceType) type, id);
    }

    private static StructurePieceType register(StructurePieceType.ManagerAware type, String id) {
        return register((StructurePieceType) type, id);
    }

    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<StructurePieceType>) handler;

        OCEAN_SHRINE = register(OceanShrineGenerator.Piece::new, "OShrine");
    }
}
