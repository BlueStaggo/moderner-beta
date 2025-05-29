package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.FloatSliderCallbacks;
import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.BiomePickerCallbacks;
import mod.bluestaggo.modernerbeta.util.NbtTags;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
                    list.addAll(left, right);
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
        return new SimpleOption<>(
            "",
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.of(settings.getString(i).orElseThrow()),
            new BiomePickerCallbacks(this.client::setScreen, this, this.generatorOptionsHolder, allowNone),
            settings.getString(i).orElseThrow(),
            value -> {
                settings.remove(i);
                settings.add(i, NbtString.of(value));
                this.clearAndInit();
            }
        );
    }

    protected SimpleOption<?> biomeSubOption(int i, String subKey, boolean allowNone) {
        return new SimpleOption<>(
            "",
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.of(settings.getCompound(i).orElseThrow().getString(subKey, "")),
            new BiomePickerCallbacks(this.client::setScreen, this, this.generatorOptionsHolder, allowNone),
            settings.getCompound(i).orElseThrow().getString(subKey, ""),
            value -> {
                settings.getCompound(i).orElseThrow().put(subKey, NbtString.of(value));
                this.clearAndInit();
            }
        );
    }

    protected SimpleOption<Float> floatRangeSubOption(int i, String subKey, float min, float max) {
        return new SimpleOption<>(
            this.getTextKey(subKey),
            SimpleOption.emptyTooltip(),
            (optionText, value) -> GameOptions.getGenericValueText(this.getText(subKey), Text.literal("%.3f".formatted(value))),
            new FloatSliderCallbacks(min, max),
            settings.getCompound(i).orElseThrow().getFloat(subKey).orElse(0.0F),
            value -> settings.getCompound(i).orElseThrow().putFloat(subKey, value)
        );
    }

    protected List<SimpleOption<?>> extendedBiomeIdOption(int i, boolean allowNone) {
        return List.of(
            new SimpleOption<>(
                "",
                SimpleOption.emptyTooltip(),
                (optionText, value) -> Text.of(settings.getString(i).orElseThrow()),
                new BiomePickerCallbacks(this.client::setScreen, this, this.generatorOptionsHolder, allowNone),
                ExtendedBiomeId.of(settings.getString(i).orElseThrow()).toString(),
                value -> {
                    settings.add(i, NbtString.of(value));
                    this.clearAndInit();
                }
            )
        );
    }

    protected List<SimpleOption<?>> voronoiPointBiomeOption(int i) {
        ArrayList<SimpleOption<?>> list = new ArrayList<>(List.of(
            this.headerOption(Text.translatable(STRING_PREFIX + "biome.climateMappings." + NbtTags.BIOME)),
            this.biomeSubOption(i, NbtTags.BIOME, false),
            this.headerOption(Text.translatable(STRING_PREFIX + "biome.climateMappings." + NbtTags.OCEAN_BIOME)),
            this.biomeSubOption(i, NbtTags.OCEAN_BIOME, false),
            this.headerOption(Text.translatable(STRING_PREFIX + "biome.climateMappings." + NbtTags.DEEP_OCEAN_BIOME)),
            this.biomeSubOption(i, NbtTags.DEEP_OCEAN_BIOME, false),
            this.floatRangeSubOption(i, NbtTags.TEMP, 0.0F, 1.0F),
            this.floatRangeSubOption(i, NbtTags.RAIN, 0.0F, 1.0F),
            this.floatRangeSubOption(i, NbtTags.WEIRD, 0.0F, 1.0F)
        ));
        list.add(null);
        return list;
    }

    protected List<SimpleOption<?>> voronoiPointCaveBiomeOption(int i) {
        ArrayList<SimpleOption<?>> list = new ArrayList<>(List.of(
            this.headerOption(Text.translatable(STRING_PREFIX + "biome.climateMappings." + NbtTags.BIOME)),
            this.biomeSubOption(i, NbtTags.BIOME, true),
            this.floatRangeSubOption(i, NbtTags.TEMP, 0.0F, 1.0F),
            this.floatRangeSubOption(i, NbtTags.RAIN, 0.0F, 1.0F),
            this.floatRangeSubOption(i, NbtTags.DEPTH, 0.0F, 1.0F)
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
