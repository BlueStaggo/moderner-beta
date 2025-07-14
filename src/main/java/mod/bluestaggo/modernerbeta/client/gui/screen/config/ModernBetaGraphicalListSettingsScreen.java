package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.FloatSliderCallbacks;
import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.BiomePickerCallbacks;
import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.TextFieldCallbacks;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class ModernBetaGraphicalListSettingsScreen extends ModernBetaGraphicalSettingsScreen<NbtList> {
    public ModernBetaGraphicalListSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        NbtList settings,
        Consumer<NbtList> onDone
    ) {
        super(title, parent, generatorOptionsHolder, "list", settings, onDone);
    }

    protected abstract List<SimpleOption<?>> getOptions(int i);

    protected abstract NbtElement getDefaultElement();

    @Override
    protected void addOptions(OptionListWidget list) {
        for (int i = 0; i < this.settings.size(); i++) {
            final int finalI = i;

            List<SimpleOption<?>> options = new ArrayList<>(this.getOptions(i));
            if (options.isEmpty()) {
                continue;
            }

            SimpleOption<?> removeButton = this.customButton(
                this.getText("remove"),
                () -> {
                    this.settings.remove(finalI);
                    this.clearAndInit();
                }
            );

            options.add(removeButton);
            if (options.size() % 2 == 1) {
                options.add(null);
            }

            for (int j = 0; j < options.size(); j += 2) {
                SimpleOption<?> left = options.get(j);
                SimpleOption<?> right = options.get(j + 1);

                if (right != null) {
                    list.addAll(new SimpleOption[] {left, right});
                } else {
                    list.addSingleOptionEntry(left);
                }
            }
        }

        list.addSingleOptionEntry(this.headerOption(Text.empty()));
        list.addSingleOptionEntry(this.customButton(
            this.getText("add"),
            () -> {
                this.settings.add(getDefaultElement());
                this.clearAndInit();
            }
        ));
    }

    protected SimpleOption<?> biomeOption(int i, boolean allowNone) {
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(i));
        return new SimpleOption<>(
            "",
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.of(stringSupplier.get()),
            new BiomePickerCallbacks(this.client::setScreen, this, this.generatorOptionsHolder, allowNone),
            stringSupplier.get(),
            value -> {
                settings.remove(i);
                settings.add(i, NbtString.of(value));
                this.clearAndInit();
            }
        );
    }

    protected SimpleOption<?> biomeSubOption(int i, String subKey, boolean allowNone) {
        Supplier<NbtCompound> compoundSupplier = () -> VersionCompat.unwrap(settings.getCompound(i));
        Supplier<String> stringSupplier = () -> VersionCompat.unwrapOrElse(compoundSupplier.get().getString(subKey), "");
        return new SimpleOption<>(
            "",
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.of(stringSupplier.get()),
            new BiomePickerCallbacks(this.client::setScreen, this, this.generatorOptionsHolder, allowNone),
            stringSupplier.get(),
            value -> {
                compoundSupplier.get().put(subKey, NbtString.of(value));
                this.clearAndInit();
            }
        );
    }

    protected SimpleOption<Float> floatRangeSubOption(int i, String subKey, float min, float max) {
        Supplier<NbtCompound> compoundSupplier = () -> VersionCompat.unwrap(settings.getCompound(i));
        return new SimpleOption<>(
            this.getTextKey(subKey),
            SimpleOption.emptyTooltip(),
            (optionText, value) -> GameOptions.getGenericValueText(this.getText(subKey), Text.literal("%.3f".formatted(value))),
            new FloatSliderCallbacks(min, max),
            VersionCompat.unwrapOrElse(compoundSupplier.get().getFloat(subKey), 0.0F),
            value -> compoundSupplier.get().putFloat(subKey, value)
        );
    }

    protected List<SimpleOption<?>> extendedBiomeIdOption(int i) {
        Supplier<String> stringSupplier = () -> VersionCompat.unwrapOrElse(settings.getString(i), "");
        return List.of(
            new SimpleOption<>(
                "",
                SimpleOption.emptyTooltip(),
                (optionText, value) -> Text.of(stringSupplier.get()),
                new TextFieldCallbacks(string -> ExtendedBiomeId.validate(string).error().isEmpty()),
                ExtendedBiomeId.of(stringSupplier.get()).toString(),
                value -> {
                    settings.add(i, NbtString.of(value));
                    this.clearAndInit();
                }
            )
        );
    }

    protected List<SimpleOption<?>> voronoiPointBiomeOption(int i) {
        ArrayList<SimpleOption<?>> list = new ArrayList<>(List.of(
            this.headerOption(Text.translatable(STRING_PREFIX + "climate_mappings.biome")),
            this.biomeSubOption(i, "biome", false),
            this.headerOption(Text.translatable(STRING_PREFIX + "climate_mappings.oceanBiome")),
            this.biomeSubOption(i, "oceanBiome", false),
            this.headerOption(Text.translatable(STRING_PREFIX + "climate_mappings.deepOceanBiome")),
            this.biomeSubOption(i, "deepOceanBiome", false),
            this.floatRangeSubOption(i, "temp", 0.0F, 1.0F),
            this.floatRangeSubOption(i, "rain", 0.0F, 1.0F),
            this.floatRangeSubOption(i, "weird", 0.0F, 1.0F)
        ));
        list.add(null);
        return list;
    }

    protected List<SimpleOption<?>> voronoiPointCaveBiomeOption(int i) {
        ArrayList<SimpleOption<?>> list = new ArrayList<>(List.of(
            this.headerOption(Text.translatable(STRING_PREFIX + "climate_mappings.biome")),
            this.biomeSubOption(i, "biome", true),
            this.floatRangeSubOption(i, "temp", 0.0F, 1.0F),
            this.floatRangeSubOption(i, "rain", 0.0F, 1.0F),
            this.floatRangeSubOption(i, "depth", 0.0F, 1.0F)
        ));
        list.add(null);
        return list;
    }

    @FunctionalInterface
    public interface Constructor {
        ModernBetaGraphicalListSettingsScreen create(
            String title,
            Screen parent,
            GeneratorOptionsHolder generatorOptionsHolder,
            NbtList settings,
            Consumer<NbtList> onDone
        );
    }
}
