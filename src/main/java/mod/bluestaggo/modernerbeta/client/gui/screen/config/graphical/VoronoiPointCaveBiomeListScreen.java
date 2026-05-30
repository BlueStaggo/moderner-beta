//~dotLocation
package mod.bluestaggo.modernerbeta.client.gui.screen.config.graphical;

import mod.bluestaggo.modernerbeta.util.CompoundTagBuilder;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.biome.Biomes;

import java.util.List;
import java.util.function.Consumer;

public class VoronoiPointCaveBiomeListScreen extends ModernBetaGraphicalListSettingsScreen {
    public VoronoiPointCaveBiomeListScreen(String title, Screen parent, WorldCreationContext context, ListTag settings, Consumer<ListTag> onDone) {
        super(title, parent, context, settings, onDone);
    }

    @Override
    protected List<OptionInstance<?>> getOptions(int i) {
        return this.voronoiPointCaveBiomeOption(i);
    }

    @Override
    protected Tag getDefaultElement() {
        return new CompoundTagBuilder()
            .putString("biome", Biomes.LUSH_CAVES.identifier().toString())
            .putFloat("temp", 0.5F)
            .putFloat("rain", 0.5F)
            .putFloat("depth", 0.5F)
            .build();
    }
}
