package mod.bluestaggo.modernerbeta.tags;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModernBetaBlockTags {
    public static final TagKey<Block> OVERWORLD_CARVER_REPLACEABLES = keyOf("overworld_carver_replaceables");

    private static TagKey<Block> keyOf(String id) {
        return TagKey.create(Registries.BLOCK, ModernerBeta.createId(id));
    }
}
