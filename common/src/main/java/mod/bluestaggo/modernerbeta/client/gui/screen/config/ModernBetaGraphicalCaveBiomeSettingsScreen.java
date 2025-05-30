package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.util.NbtTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModernBetaGraphicalCaveBiomeSettingsScreen extends ModernBetaGraphicalCompoundSettingsScreen {
    public ModernBetaGraphicalCaveBiomeSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        NbtCompound settings,
        Consumer<NbtCompound> onDone
    ) {
        super(title, parent, generatorOptionsHolder, "caveBiome", settings, onDone);
    }

    @Override
    protected void addOptions(OptionListWidget list) {
        Identifier biomeProvider = Identifier.of(this.settings.getString(NbtTags.BIOME_PROVIDER).orElseThrow());

        list.addSingleOptionEntry(this.primarySelectionOption(NbtTags.BIOME_PROVIDER,
            Arrays.stream(ModernBetaBuiltInTypes.CaveBiome.values())
                .map(caveBiome -> caveBiome.id)
                .toArray(Identifier[]::new)));

        if (ModernBetaBuiltInTypes.CaveBiome.SINGLE.id.equals(biomeProvider)) {
            list.addAll(this.headerOption(this.getText(NbtTags.SINGLE_BIOME)), this.biomeOption(NbtTags.SINGLE_BIOME, false));
        } else if (ModernBetaBuiltInTypes.CaveBiome.VORONOI.id.equals(biomeProvider)) {
            list.addAll(
                this.floatRangeOption(NbtTags.VORONOI_HORIZONTAL_NOISE_SCALE, 0.001F, 100.0F),
                this.floatRangeOption(NbtTags.VORONOI_VERTICAL_NOISE_SCALE, 0.001F, 100.0F),
                this.intRangeOption(NbtTags.VORONOI_DEPTH_MIN_Y, -64, 320),
                this.intRangeOption(NbtTags.VORONOI_DEPTH_MAX_Y, -64, 320)
            );

            list.addSingleOptionEntry(this.listEditButton(
                this.getText(NbtTags.VORONOI_POINTS),
                NbtTags.VORONOI_POINTS,
                NbtElement.COMPOUND_TYPE,
                VoronoiPointCaveBiomeListScreen::new
            ));
        }
    }
}
