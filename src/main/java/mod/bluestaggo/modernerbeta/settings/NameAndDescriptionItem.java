package mod.bluestaggo.modernerbeta.settings;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface NameAndDescriptionItem {
    ResourceLocation TEXTURE_PRESET_CUSTOM = createTextureId(ModernerBeta.createId("custom"));

    Component makeOrGetTitleComponent(ResourceLocation fallbackId);
    Component makeOrGetDescriptionComponent(ResourceLocation fallbackId);

    default ResourceLocation getTextureLocation(ResourceLocation fallbackId) {
        return createItemTextureId(fallbackId);
    }

    private static ResourceLocation createItemTextureId(ResourceLocation id) {
        ResourceLocation idObj = createTextureId(id);
        return Minecraft.getInstance().getResourceManager().getResource(idObj).isPresent()
                ? idObj : TEXTURE_PRESET_CUSTOM;
    }

    private static ResourceLocation createTextureId(ResourceLocation id) {
        return id.withPath("textures/gui/moderner_beta_settings_preset/" + id.getPath() + ".png");
    }
}
