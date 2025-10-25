package mod.bluestaggo.modernerbeta.settings;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface NameAndDescriptionItem {
    Component makeOrGetTitleComponent(ResourceLocation fallbackId);
    Component makeOrGetDescriptionComponent(ResourceLocation fallbackId);
}
