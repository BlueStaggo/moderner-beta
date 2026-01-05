package mod.bluestaggo.modernerbeta.settings;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;

public interface NameAndDescriptionItem {
    FileToIdConverter TEXTURE_ID_CONVERTER = new FileToIdConverter("textures/gui/moderner_beta_settings_preset", ".png");
    ResourceLocation TEXTURE_PRESET_CUSTOM = TEXTURE_ID_CONVERTER.idToFile(ModernerBeta.createId("custom"));

    Component makeOrGetTitleComponent(ResourceLocation fallbackId);
    Component makeOrGetDescriptionComponent(ResourceLocation fallbackId);

    default ResourceLocation getTextureLocation(ResourceLocation fallbackId) {
        return createItemTextureId(fallbackId);
    }

    private static ResourceLocation createItemTextureId(ResourceLocation id) {
        ResourceLocation idObj = TEXTURE_ID_CONVERTER.idToFile(id);
        return Minecraft.getInstance().getResourceManager().getResource(idObj).isPresent()
                ? idObj : TEXTURE_PRESET_CUSTOM;
    }
}
