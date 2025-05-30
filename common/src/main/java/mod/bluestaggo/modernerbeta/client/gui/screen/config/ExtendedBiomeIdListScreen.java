package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.world.biome.BiomeKeys;

import java.util.List;
import java.util.function.Consumer;

public class ExtendedBiomeIdListScreen extends ModernBetaGraphicalListSettingsScreen {
    public ExtendedBiomeIdListScreen(String title, Screen parent, GeneratorOptionsHolder generatorOptionsHolder, NbtList settings, Consumer<NbtList> onDone) {
        super(title, parent, generatorOptionsHolder, settings, onDone);
    }

    @Override
    protected List<SimpleOption<?>> getOptions(int i) {
        return this.extendedBiomeIdOption(i, false);
    }

    @Override
    protected NbtElement getDefaultElement() {
        return NbtString.of(BiomeKeys.PLAINS.getValue().toString());
    }
}
