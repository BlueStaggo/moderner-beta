package mod.bluestaggo.modernerbeta.client.gui.screen.config.graphical;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.*;
import mod.bluestaggo.modernerbeta.util.ExtendedIdentifier;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.function.FloatSupplier;
import mod.bluestaggo.modernerbeta.level.biome.HeightConfig;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Tuple;

import java.util.Arrays;
import java.util.List;
import java.util.function.*;

public abstract class ModernBetaGraphicalCompoundSettingsScreen extends ModernBetaGraphicalSettingsScreen<CompoundTag> {
    public ModernBetaGraphicalCompoundSettingsScreen(
        String title,
        Screen parent,
        WorldCreationContext context,
        String type,
        CompoundTag settings,
        Consumer<CompoundTag> onDone
    ) {
        super(title, parent, context, type, settings, onDone);
    }

    protected Tuple<CompoundTag, String> resolveSettings(String key) {
        String[] subKeys = key.split("\\.");
        var defaultPair = new Tuple<>(this.settings, key);
        if (subKeys.length <= 1) {
            return defaultPair;
        }

        Tag element = this.settings;
        for (int i = 0; i < subKeys.length - 1; i++) {
            if (element instanceof ListTag list) {
                try {
                    element = list.get(Integer.parseInt(subKeys[i]));
                } catch (NumberFormatException ignored) {
                    return defaultPair;
                }
            } else if (element instanceof CompoundTag compound) {
                element = compound.get(subKeys[i]);
            } else {
                return defaultPair;
            }
        }

        if (element instanceof CompoundTag compound) {
            return new Tuple<>(compound, subKeys[subKeys.length - 1]);
        }
        return defaultPair;
    }

    public OptionInstance<ResourceLocation> primarySelectionOption(String key, ResourceLocation... options) {
        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        String textKey = this.getTextKey(key);
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(subKey));

        return new OptionInstance<>(
            textKey,
            getTooltip(textKey),
            (optionText, value) -> Component.translatable(textKey + "." + value),
            new OptionInstance.LazyEnum<>(
                () -> Arrays.stream(options).toList(),
                value -> Arrays.stream(options).filter(value::equals).findFirst(),
                ResourceLocation.CODEC
            ),
            VersionCompat.id(stringSupplier.get()),
            value -> {
                settings.putString(subKey, value.toString());
                this.rebuildWidgets();
            }
        );
    }

    public OptionInstance<String> selectionOption(String key, Supplier<StringRepresentable[]> options) {
        return this.selectionOption(key,
            Arrays.stream(options.get())
                .map(StringRepresentable::getSerializedName)
                .toArray(String[]::new));
    }

    public OptionInstance<String> selectionOption(String key, String... options) {
        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        String textKey = this.getTextKey(key);
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(subKey));

        return new OptionInstance<>(
            textKey,
            getTooltip(textKey),
            (optionText, value) -> Component.translatable(textKey + "." + value),
            new OptionInstance.LazyEnum<>(
                () -> Arrays.stream(options).toList(),
                value -> Arrays.stream(options).filter(value::equals).findFirst(),
                Codec.STRING
            ),
            stringSupplier.get(),
            value -> settings.putString(subKey, value)
        );
    }

    public OptionInstance<Boolean> booleanOption(String key) {
        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        BooleanSupplier booleanSupplier = () -> VersionCompat.unwrapOrElse(settings.getBoolean(subKey), false);

        return OptionInstance.createBoolean(
            this.getTextKey(key),
            booleanSupplier.getAsBoolean(),
            value -> settings.putBoolean(subKey, value)
        );
    }

    public OptionInstance<Integer> intRangeOption(String key, int min, int max) {
        String textKey = this.getTextKey(key);
        return this.intRangeOption(key, new OptionInstance.IntRange(min, max),
            (optionText, value) -> Options.genericValueLabel(Component.translatable(textKey), value));
    }

    public OptionInstance<Integer> intRangeOption(String key, int min, int max, int multiple) {
        String textKey = this.getTextKey(key);
        return this.intRangeOption(key, new ValidatingIntMultipleSliderCallbacks(min, max, multiple),
            (optionText, value) -> Options.genericValueLabel(Component.translatable(textKey), value));
    }

    public OptionInstance<Integer> intRangeOption(String key, OptionInstance.IntRangeBase intSliderCallbacks) {
        String textKey = this.getTextKey(key);
        return this.intRangeOption(key, intSliderCallbacks,
            (optionText, value) -> Options.genericValueLabel(Component.translatable(textKey), value));
    }

    public OptionInstance<Integer> intRangeOption(String key, OptionInstance.IntRangeBase intSliderCallbacks, OptionInstance.CaptionBasedToString<Integer> valueTextGetter) {
        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        IntSupplier intSupplier = () -> VersionCompat.unwrapOrElse(settings.getInt(subKey), 0);

        return new OptionInstance<>(
            this.getTextKey(key),
            getTooltip(this.getTextKey(key)),
            valueTextGetter,
            intSliderCallbacks,
            intSupplier.getAsInt(),
            value -> settings.putInt(subKey, value)
        );
    }

    public OptionInstance<Integer> intFieldOption(String key) {
        return this.intFieldOption(key, "");
    }

    public OptionInstance<Integer> intFieldOption(String key, String prefix) {
        return this.intFieldOption(key, prefix, Integer::toString, Integer::parseInt);
    }

    public OptionInstance<Integer> rgbFieldOption(String key, String prefix) {
        return this.intFieldOption(key, prefix, i -> String.format("%06X", i), s -> Integer.parseInt(s, 16));
    }

    public OptionInstance<Integer> intFieldOption(String key, String prefix, IntFunction<String> serializer, ToIntFunction<String> deserializer) {
        if (prefix == null) {
            prefix = "";
        } else if (!prefix.isEmpty()) {
            prefix += ": ";
        }

        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        IntSupplier intSupplier = () -> VersionCompat.unwrapOrElse(settings.getInt(subKey), 0);

        return new OptionInstance<>(
            this.getTextKey(key),
            getTooltip(this.getTextKey(key)),
            (optionText, value) -> Component.nullToEmpty(Integer.toString(intSupplier.getAsInt())),
            new IntegerFieldCallbacks(prefix, serializer, deserializer),
            intSupplier.getAsInt(),
            value -> settings.putInt(subKey, value)
        );
    }

    public OptionInstance<Integer> intFieldOptionFromString(String key, String prefix) {
        return this.intFieldOptionFromString(key, prefix, Integer::toString, Integer::parseInt);
    }

    public OptionInstance<Integer> intFieldOptionFromString(String key, String prefix, IntFunction<String> serializer, ToIntFunction<String> deserializer) {
        if (prefix == null) {
            prefix = "";
        } else if (!prefix.isEmpty()) {
            prefix += ": ";
        }

        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        Supplier<String> stringSupplier = () -> VersionCompat.unwrapOrElse(settings.getString(subKey), "0");

        int defaultValue = 0;
        try {
            defaultValue = Integer.parseInt(stringSupplier.get());
        } catch (NumberFormatException ignored) {
        }

        return new OptionInstance<>(
            this.getTextKey(key),
            getTooltip(this.getTextKey(key)),
            (optionText, value) -> Component.nullToEmpty(stringSupplier.get()),
            new IntegerFieldCallbacks(prefix, serializer, deserializer),
            defaultValue,
            value -> settings.putString(subKey, Integer.toString(value))
        );
    }

    public OptionInstance<Float> floatRangeOption(String key, float min, float max) {
        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        String textKey = this.getTextKey(key);
        FloatSupplier floatSupplier = () -> VersionCompat.unwrapOrElse(settings.getFloat(subKey), 0.0F);

        return new OptionInstance<>(
            textKey,
            getTooltip(textKey),
            (optionText, value) -> Options.genericValueLabel(Component.translatable(textKey), Component.literal("%.3f".formatted(value))),
            new FloatSliderCallbacks(min, max),
            floatSupplier.getAsFloat(),
            value -> settings.putFloat(subKey, value)
        );
    }

    public OptionInstance<String> stringOption(String key) {
        return this.stringOption(key, TextFieldCallbacks.NO_VALIDATION);
    }

    public OptionInstance<String> stringOption(String key, TextFieldCallbacks callbacks) {
        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(subKey));

        return new OptionInstance<>(
            this.getTextKey(key),
            getTooltip(this.getTextKey(key)),
            (optionText, value) -> Component.nullToEmpty(stringSupplier.get()),
            callbacks,
            stringSupplier.get(),
            value -> settings.putString(subKey, value)
        );
    }

    public OptionInstance<String> blockOption(String key) {
        return this.stringOption(key, new TextFieldCallbacks(
            value -> BuiltInRegistries.BLOCK.containsKey(ResourceLocation.tryParse(value)),
            value -> ResourceLocation.read(value).error().isEmpty()
        ));
    }

    public OptionInstance<String> biomeOption(String key, boolean allowNone) {
        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(subKey));

        return new OptionInstance<>(
            this.getTextKey(key),
            getTooltip(this.getTextKey(key)),
            (optionText, value) -> Component.nullToEmpty(stringSupplier.get()),
            new BiomePickerCallbacks(this.minecraft::setScreen, this, this.context, allowNone),
            stringSupplier.get(),
            value -> {
                settings.putString(subKey, value);
                this.rebuildWidgets();
            }
        );
    }

    public OptionInstance<?> extendedIdOption(String key) {
        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(subKey));

        return new OptionInstance<>(
            "",
            OptionInstance.noTooltip(),
            (optionText, value) -> Component.nullToEmpty(stringSupplier.get()),
            new TextFieldCallbacks(string -> ExtendedIdentifier.validate(string).error().isEmpty()),
            ExtendedIdentifier.of(stringSupplier.get()).toString(),
            value -> settings.putString(subKey, value)
        );
    }

    public List<OptionInstance<?>> heightConfigOption(String key) {
        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(subKey));
        Supplier<String> defaultedStringSupplier = () -> VersionCompat.unwrapOrElse(settings.getString(subKey), "");

        return List.of(
            new OptionInstance<>(
                "createWorld.customize.modern_beta.settings.heightConfig.depth",
                getTooltip("createWorld.customize.modern_beta.settings.heightConfig.depth.desc"),
                (optionText, value) -> Options.genericValueLabel(
                    Component.translatable("createWorld.customize.modern_beta.settings.heightConfig.depth"),
                    Component.literal(String.format("%.2f", HeightConfig.parse(stringSupplier.get(), HeightConfig.DEFAULT).depth()))
                ),
                new FloatSliderCallbacks(-2.0F, 2.0F),
                HeightConfig.parse(stringSupplier.get(), HeightConfig.DEFAULT).depth(),
                value -> {
                    String replacedString = defaultedStringSupplier.get();
                    float replacedScale = HeightConfig.parse(replacedString, HeightConfig.DEFAULT).scale();
                    settings.putString(subKey, HeightConfig.makeString(Mth.floor(value * 100.0F) / 100.0F, replacedScale));
                }
            ),
            new OptionInstance<>(
                "createWorld.customize.modern_beta.settings.heightConfig.scale",
                getTooltip("createWorld.customize.modern_beta.settings.heightConfig.scale.desc"),
                (optionText, value) -> Options.genericValueLabel(
                    Component.translatable("createWorld.customize.modern_beta.settings.heightConfig.scale"),
                    Component.literal(String.format("%.2f", HeightConfig.parse(stringSupplier.get(), HeightConfig.DEFAULT).scale()))
                ),
                new FloatSliderCallbacks(0.0F, 5.0F),
                HeightConfig.parse(stringSupplier.get(), HeightConfig.DEFAULT).scale(),
                value -> {
                    String replacedString = defaultedStringSupplier.get();
                    float replacedDepth = HeightConfig.parse(replacedString, HeightConfig.DEFAULT).depth();
                    settings.putString(subKey, HeightConfig.makeString(replacedDepth, Mth.floor(value * 100.0F) / 100.0F));
                }
            )
        );
    }

    public OptionInstance<Void> listEditButton(
        Component text, String key, int type,
        ModernBetaGraphicalListSettingsScreen.Constructor listSettingsScreenConstructor
    ) {
        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        Supplier<ListTag> listSupplier = () -> VersionCompat.unwrap(settings.getList(
            subKey
            //? if <1.21.5
            //, type
        ));

        return this.customButton(
            Component.translatable(STRING_PREFIX + "list.button", text.getString()),
            () -> this.minecraft.setScreen(listSettingsScreenConstructor.create(
                Component.translatable(STRING_PREFIX + "list.title", text.getString()).getString(),
                this,
                this.context,
                listSupplier.get().copy(),
                list -> settings.put(subKey, list)
            ))
        );
    }

    public OptionInstance<Void> mapEditButton(
        Component text, String key,
        ModernBetaGraphicalMapSettingsScreen.Constructor mapSettingsScreenConstructor
    ) {
        Tuple<CompoundTag, String> resolvedSettings = this.resolveSettings(key);
        CompoundTag settings = resolvedSettings.getA();
        String subKey = resolvedSettings.getB();
        Supplier<CompoundTag> compoundSupplier = () -> VersionCompat.unwrap(settings.getCompound(subKey));

        return this.customButton(
            Component.translatable(STRING_PREFIX + "list.button", text.getString()),
            () -> this.minecraft.setScreen(mapSettingsScreenConstructor.create(
                Component.translatable(STRING_PREFIX + "list.titleMap", text.getString()).getString(),
                this,
                this.context,
                compoundSupplier.get(),
                compound -> settings.put(subKey, compound)
            ))
        );
    }
}
