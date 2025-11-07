package mod.bluestaggo.modernerbeta.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Optional;

public record ModernBetaSettingsPresetCategory(
    ResourceLocation defaultIcon,
    Optional<Component> categoryName,
    Optional<Component> categoryDescription,
    TagKey<ModernBetaSettingsPreset> presetTag
) implements NameAndDescriptionItem {
    public static final Codec<ModernBetaSettingsPresetCategory> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("defaultIcon").forGetter(ModernBetaSettingsPresetCategory::defaultIcon),
            net.minecraft.network.chat.ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(ModernBetaSettingsPresetCategory::categoryName),
            net.minecraft.network.chat.ComponentSerialization.CODEC.optionalFieldOf("description").forGetter(ModernBetaSettingsPresetCategory::categoryDescription),
            TagKey.codec(ModernBetaResourceKeys.SETTINGS_PRESET).fieldOf("presetTag").forGetter(ModernBetaSettingsPresetCategory::presetTag)
        ).apply(instance, ModernBetaSettingsPresetCategory::new)
    );

    public ModernBetaSettingsPresetCategory(ResourceLocation defaultIcon, TagKey<ModernBetaSettingsPreset> presetTag) {
        this(
            defaultIcon,
            Optional.empty(),
            Optional.empty(),
            presetTag
        );
    }

    public ModernBetaSettingsPresetCategory(
            ResourceLocation defaultIcon,
            ResourceLocation id,
            TagKey<ModernBetaSettingsPreset> presetTag
    ) {
        this(
            defaultIcon,
            Optional.of(makeTitleComponent(id)),
            Optional.of(makeDescriptionComponent(id)),
            presetTag
        );
    }

    @Override
    public Component makeOrGetTitleComponent(ResourceLocation fallbackId) {
        return categoryName.orElseGet(() -> makeTitleComponent(fallbackId));
    }

    @Override
    public Component makeOrGetDescriptionComponent(ResourceLocation fallbackId) {
        return categoryDescription.orElseGet(() -> makeDescriptionComponent(fallbackId));
    }

    @Override
    public ResourceLocation getTextureLocation(ResourceLocation fallbackId) {
        return NameAndDescriptionItem.super.getTextureLocation(this.defaultIcon);
    }

    private static Component makeTitleComponent(ResourceLocation id) {
        return Component.translatable("createWorld.customize.modern_beta.preset_category.name." + id.toLanguageKey()).withStyle(ChatFormatting.AQUA);
    }

    private static Component makeDescriptionComponent(ResourceLocation id) {
        return Component.translatable("createWorld.customize.modern_beta.preset_category.desc." + id.toLanguageKey());
    }
}
