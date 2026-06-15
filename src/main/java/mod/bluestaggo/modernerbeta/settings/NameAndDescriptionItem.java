package mod.bluestaggo.modernerbeta.settings;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;

public interface NameAndDescriptionItem {
    FileToIdConverter TEXTURE_ID_CONVERTER = new FileToIdConverter("textures/gui/moderner_beta_settings_preset", ".png");
    Identifier TEXTURE_PRESET_CUSTOM = TEXTURE_ID_CONVERTER.idToFile(ModernerBeta.createId("custom"));

    Component makeOrGetTitleComponent(Identifier fallbackId);
    Component makeOrGetDescriptionComponent(Identifier fallbackId);

    default Identifier getTextureLocation(Identifier fallbackId) {
        return createItemTextureId(fallbackId);
    }

    private static Identifier createItemTextureId(Identifier id) {
        Identifier idObj = TEXTURE_ID_CONVERTER.idToFile(id);
        return Minecraft.getInstance().getResourceManager().getResource(idObj).isPresent()
                ? idObj : TEXTURE_PRESET_CUSTOM;
    }
}
