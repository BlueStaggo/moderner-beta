package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.*;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.util.function.FloatSupplier;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.List;
import java.util.function.*;

@Environment(EnvType.CLIENT)
public abstract class ModernBetaGraphicalCompoundSettingsScreen extends ModernBetaGraphicalSettingsScreen<NbtCompound> {
    public ModernBetaGraphicalCompoundSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        String type,
        NbtCompound settings,
        Consumer<NbtCompound> onDone
    ) {
        super(title, parent, generatorOptionsHolder, type, settings, onDone);
    }

    protected Pair<NbtCompound, String> resolveSettings(String key) {
        String[] subKeys = key.split("\\.");
        var defaultPair = new Pair<>(this.settings, key);
        if (subKeys.length <= 1) {
            return defaultPair;
        }

        NbtElement element = this.settings;
        for (int i = 0; i < subKeys.length - 1; i++) {
            if (element instanceof NbtList list) {
                try {
                    element = list.get(Integer.parseInt(subKeys[i]));
                } catch (NumberFormatException ignored) {
                    return defaultPair;
                }
            } else if (element instanceof NbtCompound compound) {
                element = compound.get(subKeys[i]);
            } else {
                return defaultPair;
            }
        }

        if (element instanceof NbtCompound compound) {
            return new Pair<>(compound, subKeys[subKeys.length - 1]);
        }
        return defaultPair;
    }

    public SimpleOption<Identifier> primarySelectionOption(String key, Identifier... options) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        String textKey = this.getTextKey(key);
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(subKey));

        return new SimpleOption<>(
            textKey,
            getTooltip(textKey),
            (optionText, value) -> Text.translatable(textKey + "." + value),
            new SimpleOption.LazyCyclingCallbacks<>(
                () -> Arrays.stream(options).toList(),
                value -> Arrays.stream(options).filter(value::equals).findFirst(),
                Identifier.CODEC
            ),
            VersionCompat.id(stringSupplier.get()),
            value -> {
                settings.putString(subKey, value.toString());
                this.clearAndInit();
            }
        );
    }

    public SimpleOption<String> selectionOption(String key, Supplier<StringIdentifiable[]> options) {
        return this.selectionOption(key,
            Arrays.stream(options.get())
                .map(StringIdentifiable::asString)
                .toArray(String[]::new));
    }

    public SimpleOption<String> selectionOption(String key, String... options) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        String textKey = this.getTextKey(key);
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(subKey));

        return new SimpleOption<>(
            textKey,
            getTooltip(textKey),
            (optionText, value) -> Text.translatable(textKey + "." + value),
            new SimpleOption.LazyCyclingCallbacks<>(
                () -> Arrays.stream(options).toList(),
                value -> Arrays.stream(options).filter(value::equals).findFirst(),
                Codec.STRING
            ),
            stringSupplier.get(),
            value -> settings.putString(subKey, value)
        );
    }

    public SimpleOption<Boolean> booleanOption(String key) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        BooleanSupplier booleanSupplier = () -> VersionCompat.unwrapOrElse(settings.getBoolean(subKey), false);

        return SimpleOption.ofBoolean(
            this.getTextKey(key),
            booleanSupplier.getAsBoolean(),
            value -> settings.putBoolean(subKey, value)
        );
    }

    public SimpleOption<Integer> intRangeOption(String key, int min, int max) {
        String textKey = this.getTextKey(key);
        return this.intRangeOption(key, new SimpleOption.ValidatingIntSliderCallbacks(min, max),
            (optionText, value) -> GameOptions.getGenericValueText(Text.translatable(textKey), value));
    }

    public SimpleOption<Integer> intRangeOption(String key, int min, int max, int multiple) {
        String textKey = this.getTextKey(key);
        return this.intRangeOption(key, new ValidatingIntMultipleSliderCallbacks(min, max, multiple),
            (optionText, value) -> GameOptions.getGenericValueText(Text.translatable(textKey), value));
    }

    public SimpleOption<Integer> intRangeOption(String key, SimpleOption.IntSliderCallbacks intSliderCallbacks) {
        String textKey = this.getTextKey(key);
        return this.intRangeOption(key, intSliderCallbacks,
            (optionText, value) -> GameOptions.getGenericValueText(Text.translatable(textKey), value));
    }

    public SimpleOption<Integer> intRangeOption(String key, SimpleOption.IntSliderCallbacks intSliderCallbacks, SimpleOption.ValueTextGetter<Integer> valueTextGetter) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        IntSupplier intSupplier = () -> VersionCompat.unwrapOrElse(settings.getInt(subKey), 0);

        return new SimpleOption<>(
            this.getTextKey(key),
            getTooltip(this.getTextKey(key)),
            valueTextGetter,
            intSliderCallbacks,
            intSupplier.getAsInt(),
            value -> settings.putInt(subKey, value)
        );
    }

    public SimpleOption<Integer> intFieldOption(String key) {
        return this.intFieldOption(key, "");
    }

    public SimpleOption<Integer> intFieldOption(String key, String prefix) {
        return this.intFieldOption(key, prefix, Integer::toString, Integer::parseInt);
    }

    public SimpleOption<Integer> rgbFieldOption(String key, String prefix) {
        return this.intFieldOption(key, prefix, i -> String.format("%06X", i), s -> Integer.parseInt(s, 16));
    }

    public SimpleOption<Integer> intFieldOption(String key, String prefix, IntFunction<String> serializer, ToIntFunction<String> deserializer) {
        if (prefix == null) {
            prefix = "";
        } else if (!prefix.isEmpty()) {
            prefix += ": ";
        }

        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        IntSupplier intSupplier = () -> VersionCompat.unwrapOrElse(settings.getInt(subKey), 0);

        return new SimpleOption<>(
            this.getTextKey(key),
            getTooltip(this.getTextKey(key)),
            (optionText, value) -> Text.of(Integer.toString(intSupplier.getAsInt())),
            new IntegerFieldCallbacks(prefix, serializer, deserializer),
            intSupplier.getAsInt(),
            value -> settings.putInt(subKey, value)
        );
    }

    public SimpleOption<Integer> intFieldOptionFromString(String key, String prefix) {
        return this.intFieldOptionFromString(key, prefix, Integer::toString, Integer::parseInt);
    }

    public SimpleOption<Integer> intFieldOptionFromString(String key, String prefix, IntFunction<String> serializer, ToIntFunction<String> deserializer) {
        if (prefix == null) {
            prefix = "";
        } else if (!prefix.isEmpty()) {
            prefix += ": ";
        }

        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        Supplier<String> stringSupplier = () -> VersionCompat.unwrapOrElse(settings.getString(subKey), "0");

        int defaultValue = 0;
        try {
            defaultValue = Integer.parseInt(stringSupplier.get());
        } catch (NumberFormatException ignored) {
        }

        return new SimpleOption<>(
            this.getTextKey(key),
            getTooltip(this.getTextKey(key)),
            (optionText, value) -> Text.of(stringSupplier.get()),
            new IntegerFieldCallbacks(prefix, serializer, deserializer),
            defaultValue,
            value -> settings.putString(subKey, Integer.toString(value))
        );
    }

    public SimpleOption<Float> floatRangeOption(String key, float min, float max) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        String textKey = this.getTextKey(key);
        FloatSupplier floatSupplier = () -> VersionCompat.unwrapOrElse(settings.getFloat(subKey), 0.0F);

        return new SimpleOption<>(
            textKey,
            getTooltip(textKey),
            (optionText, value) -> GameOptions.getGenericValueText(Text.translatable(textKey), Text.literal("%.3f".formatted(value))),
            new FloatSliderCallbacks(min, max),
            floatSupplier.getAsFloat(),
            value -> settings.putFloat(subKey, value)
        );
    }

    public SimpleOption<String> stringOption(String key) {
        return this.stringOption(key, TextFieldCallbacks.NO_VALIDATION);
    }

    public SimpleOption<String> stringOption(String key, TextFieldCallbacks callbacks) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(subKey));

        return new SimpleOption<>(
            this.getTextKey(key),
            getTooltip(this.getTextKey(key)),
            (optionText, value) -> Text.of(stringSupplier.get()),
            callbacks,
            stringSupplier.get(),
            value -> settings.putString(subKey, value)
        );
    }

    public SimpleOption<String> blockOption(String key) {
        return this.stringOption(key, new TextFieldCallbacks(
            value -> Registries.BLOCK.containsId(Identifier.tryParse(value)),
            value -> Identifier.validate(value).error().isEmpty()
        ));
    }

    public SimpleOption<String> biomeOption(String key, boolean allowNone) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(subKey));

        return new SimpleOption<>(
            this.getTextKey(key),
            getTooltip(this.getTextKey(key)),
            (optionText, value) -> Text.of(stringSupplier.get()),
            new BiomePickerCallbacks(this.client::setScreen, this, this.generatorOptionsHolder, allowNone),
            stringSupplier.get(),
            value -> {
                settings.putString(subKey, value);
                this.clearAndInit();
            }
        );
    }

    public SimpleOption<?> extendedBiomeIdOption(String key) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(subKey));

        return new SimpleOption<>(
            "",
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.of(stringSupplier.get()),
            new TextFieldCallbacks(string -> ExtendedBiomeId.validate(string).error().isEmpty()),
            ExtendedBiomeId.of(stringSupplier.get()).toString(),
            value -> settings.putString(subKey, value)
        );
    }

    public List<SimpleOption<?>> heightConfigOption(String key) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(subKey));
        Supplier<String> defaultedStringSupplier = () -> VersionCompat.unwrapOrElse(settings.getString(subKey), "");

        return List.of(
            new SimpleOption<>(
                "createWorld.customize.modern_beta.settings.heightConfig.depth",
                getTooltip("createWorld.customize.modern_beta.settings.heightConfig.depth.desc"),
                (optionText, value) -> GameOptions.getGenericValueText(
                    Text.translatable("createWorld.customize.modern_beta.settings.heightConfig.depth"),
                    Text.literal(String.format("%.2f", HeightConfig.parse(stringSupplier.get(), HeightConfig.DEFAULT).depth()))
                ),
                new FloatSliderCallbacks(-2.0F, 2.0F),
                HeightConfig.parse(stringSupplier.get(), HeightConfig.DEFAULT).depth(),
                value -> {
                    String replacedString = defaultedStringSupplier.get();
                    float replacedScale = HeightConfig.parse(replacedString, HeightConfig.DEFAULT).scale();
                    settings.putString(subKey, HeightConfig.makeString(MathHelper.floor(value * 100.0F) / 100.0F, replacedScale));
                }
            ),
            new SimpleOption<>(
                "createWorld.customize.modern_beta.settings.heightConfig.scale",
                getTooltip("createWorld.customize.modern_beta.settings.heightConfig.scale.desc"),
                (optionText, value) -> GameOptions.getGenericValueText(
                    Text.translatable("createWorld.customize.modern_beta.settings.heightConfig.scale"),
                    Text.literal(String.format("%.2f", HeightConfig.parse(stringSupplier.get(), HeightConfig.DEFAULT).scale()))
                ),
                new FloatSliderCallbacks(0.0F, 5.0F),
                HeightConfig.parse(stringSupplier.get(), HeightConfig.DEFAULT).scale(),
                value -> {
                    String replacedString = defaultedStringSupplier.get();
                    float replacedDepth = HeightConfig.parse(replacedString, HeightConfig.DEFAULT).depth();
                    settings.putString(subKey, HeightConfig.makeString(replacedDepth, MathHelper.floor(value * 100.0F) / 100.0F));
                }
            )
        );
    }

    public SimpleOption<Void> listEditButton(
        Text text, String key, int type,
        ModernBetaGraphicalListSettingsScreen.Constructor listSettingsScreenConstructor
    ) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        Supplier<NbtList> listSupplier = () -> VersionCompat.unwrap(settings.getList(
            subKey
            //? if <1.21.5
            /*, type*/
        ));

        return this.customButton(
            Text.translatable(STRING_PREFIX + "list.button", text.getString()),
            () -> this.client.setScreen(listSettingsScreenConstructor.create(
                Text.translatable(STRING_PREFIX + "list.title", text.getString()).getString(),
                this,
                this.generatorOptionsHolder,
                listSupplier.get().copy(),
                list -> settings.put(subKey, list)
            ))
        );
    }

    public SimpleOption<Void> mapEditButton(
        Text text, String key,
        ModernBetaGraphicalMapSettingsScreen.Constructor mapSettingsScreenConstructor
    ) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();
        Supplier<NbtCompound> compoundSupplier = () -> VersionCompat.unwrap(settings.getCompound(subKey));

        return this.customButton(
            Text.translatable(STRING_PREFIX + "list.button", text.getString()),
            () -> this.client.setScreen(mapSettingsScreenConstructor.create(
                Text.translatable(STRING_PREFIX + "list.titleMap", text.getString()).getString(),
                this,
                this.generatorOptionsHolder,
                compoundSupplier.get(),
                compound -> settings.put(subKey, compound)
            ))
        );
    }
}
