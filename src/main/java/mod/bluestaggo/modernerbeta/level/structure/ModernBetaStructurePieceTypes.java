package mod.bluestaggo.modernerbeta.level.structure;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.Locale;

@SuppressWarnings("unused")
public class ModernBetaStructurePieceTypes {
    public static IRegistryHandler<StructurePieceType> registryHandler;

    public static StructurePieceType OCEAN_SHRINE;

    private static StructurePieceType register(StructurePieceType type, String id) {
        return registryHandler.register(ModernerBeta.createId(id.toLowerCase(Locale.ROOT)), type);
    }

    private static StructurePieceType register(StructurePieceType.ContextlessType type, String id) {
        return register((StructurePieceType) type, id);
    }

    private static StructurePieceType register(StructurePieceType.StructureTemplateType type, String id) {
        return register((StructurePieceType) type, id);
    }

    @SuppressWarnings("unchecked")
    public static void register(IRegistryHandler<?> handler) {
        registryHandler = (IRegistryHandler<StructurePieceType>) handler;

        OCEAN_SHRINE = register(OceanShrineStructurePiece::new, "OShrine");
    }
}
