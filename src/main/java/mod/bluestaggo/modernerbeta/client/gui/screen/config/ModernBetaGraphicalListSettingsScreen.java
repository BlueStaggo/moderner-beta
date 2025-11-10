package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.FloatSliderCallbacks;
import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.BiomePickerCallbacks;
import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.TextFieldCallbacks;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import mod.bluestaggo.modernerbeta.level.biome.provider.fractal.ExtendedBiomeId;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ModernBetaGraphicalListSettingsScreen extends ModernBetaGraphicalSettingsScreen<ListTag> {
    public ModernBetaGraphicalListSettingsScreen(
        String title,
        Screen parent,
        WorldCreationContext context,
        ListTag settings,
        Consumer<ListTag> onDone
    ) {
        super(title, parent, context, "list", settings, onDone);
    }

    protected abstract List<OptionInstance<?>> getOptions(int i);

    protected abstract Tag getDefaultElement();

    @Override
    protected void addOptions(OptionsList list) {
        for (int i = 0; i < this.settings.size(); i++) {
            final int finalI = i;

            List<OptionInstance<?>> options = new ArrayList<>(this.getOptions(i));
            if (options.isEmpty()) {
                continue;
            }

            OptionInstance<?> removeButton = this.customButton(
                this.getText("remove"),
                () -> {
                    this.settings.remove(finalI);
                    this.rebuildWidgets();
                }
            );

            options.add(removeButton);
            if (options.size() % 2 == 1) {
                options.add(null);
            }

            for (int j = 0; j < options.size(); j += 2) {
                OptionInstance<?> left = options.get(j);
                OptionInstance<?> right = options.get(j + 1);

                if (right != null) {
                    list.addSmall(new OptionInstance[] {left, right});
                } else {
                    list.addBig(left);
                }
            }
        }

        list.addBig(this.headerOption(Component.empty()));
        list.addBig(this.customButton(
            this.getText("add"),
            () -> {
                this.settings.add(getDefaultElement());
                this.rebuildWidgets();
            }
        ));
    }

    protected OptionInstance<?> biomeOption(int i, boolean allowNone) {
        Supplier<String> stringSupplier = () -> VersionCompat.unwrap(settings.getString(i));
        return new OptionInstance<>(
            "",
            OptionInstance.noTooltip(),
            (optionText, value) -> Component.nullToEmpty(stringSupplier.get()),
            new BiomePickerCallbacks(this.minecraft::setScreen, this, this.context, allowNone),
            stringSupplier.get(),
            value -> {
                settings.remove(i);
                settings.add(i, StringTag.valueOf(value));
                this.rebuildWidgets();
            }
        );
    }

    protected OptionInstance<?> biomeSubOption(int i, String subKey, boolean allowNone) {
        Supplier<CompoundTag> compoundSupplier = () -> VersionCompat.unwrap(settings.getCompound(i));
        Supplier<String> stringSupplier = () -> VersionCompat.unwrapOrElse(compoundSupplier.get().getString(subKey), "");
        return new OptionInstance<>(
            "",
            OptionInstance.noTooltip(),
            (optionText, value) -> Component.nullToEmpty(stringSupplier.get()),
            new BiomePickerCallbacks(this.minecraft::setScreen, this, this.context, allowNone),
            stringSupplier.get(),
            value -> {
                compoundSupplier.get().put(subKey, StringTag.valueOf(value));
                this.rebuildWidgets();
            }
        );
    }

    protected OptionInstance<Float> floatRangeSubOption(int i, String subKey, float min, float max) {
        Supplier<CompoundTag> compoundSupplier = () -> VersionCompat.unwrap(settings.getCompound(i));
        return new OptionInstance<>(
            this.getTextKey(subKey),
            OptionInstance.noTooltip(),
            (optionText, value) -> Options.genericValueLabel(this.getText(subKey), Component.literal("%.3f".formatted(value))),
            new FloatSliderCallbacks(min, max),
            VersionCompat.unwrapOrElse(compoundSupplier.get().getFloat(subKey), 0.0F),
            value -> compoundSupplier.get().putFloat(subKey, value)
        );
    }

    protected List<OptionInstance<?>> extendedBiomeIdOption(int i) {
        Supplier<String> stringSupplier = () -> VersionCompat.unwrapOrElse(settings.getString(i), "");
        return List.of(
            new OptionInstance<>(
                "",
                OptionInstance.noTooltip(),
                (optionText, value) -> Component.nullToEmpty(stringSupplier.get()),
                new TextFieldCallbacks(string -> ExtendedBiomeId.validate(string).error().isEmpty()),
                ExtendedBiomeId.of(stringSupplier.get()).toString(),
                value -> {
                    settings.add(i, StringTag.valueOf(value));
                    this.rebuildWidgets();
                }
            )
        );
    }

    protected List<OptionInstance<?>> voronoiPointBiomeOption(int i) {
        ArrayList<OptionInstance<?>> list = new ArrayList<>(List.of(
            this.headerOption(Component.translatable(STRING_PREFIX + "climate_mappings.biome")),
            this.biomeSubOption(i, "biome", false),
            this.headerOption(Component.translatable(STRING_PREFIX + "climate_mappings.oceanBiome")),
            this.biomeSubOption(i, "oceanBiome", false),
            this.headerOption(Component.translatable(STRING_PREFIX + "climate_mappings.deepOceanBiome")),
            this.biomeSubOption(i, "deepOceanBiome", false),
            this.floatRangeSubOption(i, "temp", 0.0F, 1.0F),
            this.floatRangeSubOption(i, "rain", 0.0F, 1.0F),
            this.floatRangeSubOption(i, "weird", 0.0F, 1.0F)
        ));
        list.add(null);
        return list;
    }

    protected List<OptionInstance<?>> voronoiPointCaveBiomeOption(int i) {
        ArrayList<OptionInstance<?>> list = new ArrayList<>(List.of(
            this.headerOption(Component.translatable(STRING_PREFIX + "climate_mappings.biome")),
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
            WorldCreationContext generatorOptionsHolder,
            ListTag settings,
            Consumer<ListTag> onDone
        );
    }
}
