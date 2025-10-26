package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.util.CompoundTagBuilder;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.biome.Biomes;

import java.util.List;
import java.util.function.Consumer;

public class VoronoiPointBiomeListScreen extends ModernBetaGraphicalListSettingsScreen {
    public VoronoiPointBiomeListScreen(String title, Screen parent, WorldCreationContext context, ListTag settings, Consumer<ListTag> onDone) {
        super(title, parent, context, settings, onDone);
    }

    @Override
    protected List<OptionInstance<?>> getOptions(int i) {
        return this.voronoiPointBiomeOption(i);
    }

    @Override
    protected Tag getDefaultElement() {
        return new CompoundTagBuilder()
            .putString("biome", Biomes.PLAINS.location().toString())
            .putString("oceanBiome", Biomes.OCEAN.location().toString())
            .putString("deepOceanBiome", Biomes.DEEP_OCEAN.location().toString())
            .putFloat("temp", 0.5F)
            .putFloat("rain", 0.5F)
            .putFloat("weird", 0.5F)
            .build();
    }
}
