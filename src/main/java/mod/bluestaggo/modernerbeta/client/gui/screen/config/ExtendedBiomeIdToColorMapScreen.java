package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.random.RandomSeed;
import net.minecraft.world.biome.BiomeKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ExtendedBiomeIdToColorMapScreen extends ModernBetaGraphicalMapSettingsScreen {
    public ExtendedBiomeIdToColorMapScreen(String title, Screen parent, GeneratorOptionsHolder generatorOptionsHolder, NbtCompound settings, Consumer<NbtCompound> onDone) {
        super(title, parent, generatorOptionsHolder, settings, onDone);
    }

    @Override
    protected List<SimpleOption<?>> getOptions(int i) {
        ArrayList<SimpleOption<?>> options = new ArrayList<>();
        options.add(this.headerOption(Text.translatable(this.getTextKey("item"), i).formatted(Formatting.BOLD)));
        options.add(null);
        options.add(this.extendedBiomeIdOption(KEY + i));
        options.add(this.rgbFieldOption(VALUE + i, ""));
        return options;
    }

    @Override
    protected String getDefaultKey() {
        return BiomeKeys.PLAINS.getValue().toString();
    }

    @Override
    protected NbtElement getDefaultValue() {
        return NbtInt.of((int)(RandomSeed.getSeed() & 0xFFFFFF));
    }
}
