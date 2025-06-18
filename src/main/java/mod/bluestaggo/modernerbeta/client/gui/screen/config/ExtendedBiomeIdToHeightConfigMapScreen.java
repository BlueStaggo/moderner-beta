package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.biome.BiomeKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ExtendedBiomeIdToHeightConfigMapScreen extends ModernBetaGraphicalMapSettingsScreen {
    public ExtendedBiomeIdToHeightConfigMapScreen(String title, Screen parent, GeneratorOptionsHolder generatorOptionsHolder, NbtCompound settings, Consumer<NbtCompound> onDone) {
        super(title, parent, generatorOptionsHolder, settings, onDone);
    }

    @Override
    protected List<SimpleOption<?>> getOptions(int i) {
        ArrayList<SimpleOption<?>> options = new ArrayList<>();
        options.add(this.headerOption(Text.translatable(this.getTextKey("item"), i).formatted(Formatting.BOLD)));
        options.add(null);
        options.add(this.extendedBiomeIdOption(KEY + i));
        options.addAll(this.heightConfigOption(VALUE + i));
        return options;
    }

    @Override
    protected String getDefaultKey() {
        return BiomeKeys.PLAINS.getValue().toString();
    }

    @Override
    protected NbtElement getDefaultValue() {
        return NbtString.of(HeightConfig.DEFAULT.makeString());
    }
}
