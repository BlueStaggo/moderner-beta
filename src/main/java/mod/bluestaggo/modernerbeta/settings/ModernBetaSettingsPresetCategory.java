package mod.bluestaggo.modernerbeta.settings;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public record ModernBetaSettingsPresetCategory(
    ResourceLocation defaultIcon,
    Optional<Component> categoryName,
    Optional<Component> categoryDescription,
    List<ResourceLocation> presets
) implements NameAndDescriptionItem {
    public static final Codec<ModernBetaSettingsPresetCategory> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("defaultIcon").forGetter(ModernBetaSettingsPresetCategory::defaultIcon),
            net.minecraft.network.chat.ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(ModernBetaSettingsPresetCategory::categoryName),
            net.minecraft.network.chat.ComponentSerialization.CODEC.optionalFieldOf("description").forGetter(ModernBetaSettingsPresetCategory::categoryDescription),
            ResourceLocation.CODEC.listOf().fieldOf("presets").forGetter(ModernBetaSettingsPresetCategory::presets)
        ).apply(instance, ModernBetaSettingsPresetCategory::new)
    );

    public ModernBetaSettingsPresetCategory(ResourceLocation defaultIcon, List<ResourceLocation> presets) {
        this(
            defaultIcon,
            Optional.empty(),
            Optional.empty(),
            presets
        );
    }

    public ModernBetaSettingsPresetCategory(
            ResourceLocation defaultIcon,
            ResourceLocation id,
            List<ResourceLocation> presets
    ) {
        this(
            defaultIcon,
            Optional.of(makeTitleComponent(id)),
            Optional.of(makeDescriptionComponent(id)),
            presets
        );
    }

    public Component makeOrGetTitleComponent(ResourceLocation fallbackId) {
        return categoryName.orElseGet(() -> makeTitleComponent(fallbackId));
    }

    public Component makeOrGetDescriptionComponent(ResourceLocation fallbackId) {
        return categoryDescription.orElseGet(() -> makeDescriptionComponent(fallbackId));
    }

    private static Component makeTitleComponent(ResourceLocation id) {
        return Component.translatable("createWorld.customize.modern_beta.preset_category.name." + id.toLanguageKey()).withStyle(ChatFormatting.AQUA);
    }

    private static Component makeDescriptionComponent(ResourceLocation id) {
        return Component.translatable("createWorld.customize.modern_beta.preset_category.desc." + id.toLanguageKey());
    }
}
