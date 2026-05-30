package mod.bluestaggo.modernerbeta.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

import java.util.Optional;

public record ModernBetaSettingsPresetCategory(
    Identifier defaultIcon,
    Optional<Component> categoryName,
    Optional<Component> categoryDescription,
    TagKey<ModernBetaSettingsPreset> presetTag
) implements NameAndDescriptionItem {
    public static final Codec<ModernBetaSettingsPresetCategory> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Identifier.CODEC.fieldOf("defaultIcon").forGetter(ModernBetaSettingsPresetCategory::defaultIcon),
            net.minecraft.network.chat.ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(ModernBetaSettingsPresetCategory::categoryName),
            net.minecraft.network.chat.ComponentSerialization.CODEC.optionalFieldOf("description").forGetter(ModernBetaSettingsPresetCategory::categoryDescription),
            TagKey.codec(ModernBetaResourceKeys.SETTINGS_PRESET).fieldOf("presetTag").forGetter(ModernBetaSettingsPresetCategory::presetTag)
        ).apply(instance, ModernBetaSettingsPresetCategory::new)
    );

    public ModernBetaSettingsPresetCategory(Identifier defaultIcon, TagKey<ModernBetaSettingsPreset> presetTag) {
        this(
            defaultIcon,
            Optional.empty(),
            Optional.empty(),
            presetTag
        );
    }

    public ModernBetaSettingsPresetCategory(
            Identifier defaultIcon,
            Identifier id,
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
    public Component makeOrGetTitleComponent(Identifier fallbackId) {
        return categoryName.orElseGet(() -> makeTitleComponent(fallbackId));
    }

    @Override
    public Component makeOrGetDescriptionComponent(Identifier fallbackId) {
        return categoryDescription.orElseGet(() -> makeDescriptionComponent(fallbackId));
    }

    @Override
    public Identifier getTextureLocation(Identifier fallbackId) {
        return NameAndDescriptionItem.super.getTextureLocation(this.defaultIcon);
    }

    private static Component makeTitleComponent(Identifier id) {
        return Component.translatable("createWorld.customize.modern_beta.preset_category.name." + id.toLanguageKey()).withStyle(ChatFormatting.AQUA);
    }

    private static Component makeDescriptionComponent(Identifier id) {
        return Component.translatable("createWorld.customize.modern_beta.preset_category.desc." + id.toLanguageKey());
    }
}
