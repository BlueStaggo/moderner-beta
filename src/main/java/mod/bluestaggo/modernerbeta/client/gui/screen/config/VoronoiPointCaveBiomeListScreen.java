package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.util.NbtCompoundBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.biome.BiomeKeys;

import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class VoronoiPointCaveBiomeListScreen extends ModernBetaGraphicalListSettingsScreen {
    public VoronoiPointCaveBiomeListScreen(String title, Screen parent, GeneratorOptionsHolder generatorOptionsHolder, NbtList settings, Consumer<NbtList> onDone) {
        super(title, parent, generatorOptionsHolder, settings, onDone);
    }

    @Override
    protected List<SimpleOption<?>> getOptions(int i) {
        return this.voronoiPointCaveBiomeOption(i);
    }

    @Override
    protected NbtElement getDefaultElement() {
        return new NbtCompoundBuilder()
            .putString("biome", BiomeKeys.LUSH_CAVES.getValue().toString())
            .putFloat("temp", 0.5F)
            .putFloat("rain", 0.5F)
            .putFloat("depth", 0.5F)
            .build();
    }
}
