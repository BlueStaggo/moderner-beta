package mod.bluestaggo.modernerbeta.tags;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModernBetaBlockTags {
    public static final TagKey<Block> OVERWORLD_CARVER_REPLACEABLES = keyOf("overworld_carver_replaceables");

    private static TagKey<Block> keyOf(String id) {
        return TagKey.of(RegistryKeys.BLOCK, ModernerBeta.createId(id));
    }
}
