//~registryOr
//~dotLocation
package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaSelectBiomeScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record BiomePickerCallbacks(Consumer<Screen> screenChangeHandler, Screen parentScreen, WorldCreationContext generatorOptionsHolder, boolean allowNone) implements OptionInstance.ValueSet<String> {
    @Override
    public @NotNull Function<OptionInstance<String>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<String> tooltipFactory, Options gameOptions, int x, int y, int width, Consumer<String> changeCallback) {
        return option -> {
            ResourceLocation biomeId = ResourceLocation.tryParse(option.get());
            if (biomeId == null) {
                biomeId = Biomes.PLAINS.location();
            }
            String biomeTranslationKey = biomeId.toLanguageKey("biome");

            return Button.builder(
                "".equals(option.get())
                    ? Component.translatable("gui.none").withStyle(ChatFormatting.ITALIC)
                    : Language.getInstance().has(biomeTranslationKey)
                        ? Component.translatable(biomeTranslationKey)
                        : Component.literal(biomeId.toString()),
                onPress -> {
                    screenChangeHandler.accept(new ModernBetaSelectBiomeScreen(
                        parentScreen,
                        generatorOptionsHolder,
                        biome -> {
                            if (biome != null) {
                                ResourceKey<Biome> key = biome.unwrapKey().orElse(Biomes.PLAINS);
                                option.set(key.location().toString());
                            } else {
                                if (allowNone) {
                                    option.set("");
                                } else {
                                    option.set(Biomes.PLAINS.location().toString());
                                }
                            }
                        },
                        allowNone
                    ));
                }
            ).bounds(x, y, width, 20).build();
        };
    }

    @Override
    public @NotNull Optional<String> validateValue(String value) {
        return (allowNone && "".equals(value)) || generatorOptionsHolder.worldgenLoadContext()
            .lookupOrThrow(Registries.BIOME).containsKey(ResourceLocation.tryParse(value))
            ? Optional.of(value) : Optional.empty();
    }

    @Override
    public @NotNull Codec<String> codec() {
        return Codec.STRING;
    }
}
