package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.util.NbtTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModernBetaGraphicalBiomeSettingsScreen extends ModernBetaGraphicalCompoundSettingsScreen {
    private static final String[] CLIMATE_MAPPINGS = {
        "desert", "forest", "ice_desert", "plains", "rainforest", "savanna", "shrubland", "seasonal_forest",
        "swampland", "taiga", "tundra"
    };

    public ModernBetaGraphicalBiomeSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        NbtCompound settings,
        Consumer<NbtCompound> onDone
    ) {
        super(title, parent, generatorOptionsHolder, "biome", settings, onDone);
    }

    @Override
    protected void addOptions(OptionListWidget list) {
        Identifier biomeProvider = Identifier.of(this.settings.getString(NbtTags.BIOME_PROVIDER).orElseThrow());
        boolean usesNoise = ModernBetaBuiltInTypes.Biome.BIOME_PROVIDER_USES_NOISE.contains(biomeProvider);
        boolean isVoronoiProvider = ModernBetaBuiltInTypes.Biome.VORONOI.id.equals(biomeProvider);

        list.addSingleOptionEntry(this.primarySelectionOption(NbtTags.BIOME_PROVIDER,
            Arrays.stream(ModernBetaBuiltInTypes.Biome.values())
                .map(chunk -> chunk.id)
                .toArray(Identifier[]::new)));

        if (ModernBetaBuiltInTypes.Biome.SINGLE.id.equals(biomeProvider)) {
            list.addAll(
                    this.headerOption(this.getText(NbtTags.SINGLE_BIOME)),
                    this.biomeOption(NbtTags.SINGLE_BIOME, false)
            );
        } else if (usesNoise) {
            var noiseOptionList = new ArrayList<SimpleOption<?>>(List.of(
                this.booleanOption(NbtTags.USE_OCEAN_BIOMES),
                this.floatRangeOption(NbtTags.CLIMATE_TEMP_NOISE_SCALE, 0.001F, 1.0F),
                this.floatRangeOption(NbtTags.CLIMATE_RAIN_NOISE_SCALE, 0.001F, 1.0F),
                this.floatRangeOption(NbtTags.CLIMATE_DETAIL_NOISE_SCALE, 0.001F, 1.0F)
            ));

            if (isVoronoiProvider) {
                noiseOptionList.add(this.floatRangeOption(NbtTags.CLIMATE_WEIRD_NOISE_SCALE, 0.001F, 1.0F));
            }

            list.addAll(noiseOptionList.toArray(SimpleOption[]::new));

            if (isVoronoiProvider) {
                list.addSingleOptionEntry(this.listEditButton(
                    this.getText(NbtTags.VORONOI_POINTS),
                    NbtTags.VORONOI_POINTS,
                    NbtElement.COMPOUND_TYPE,
                    VoronoiPointBiomeListScreen::new
                ));
            } else {
                for (String climateMapping : CLIMATE_MAPPINGS) {
                    String prefix = NbtTags.CLIMATE_MAPPINGS + ".";
                    list.addSingleOptionEntry(this.headerOption(this.getText(prefix + climateMapping).formatted(Formatting.BOLD)));
                    list.addAll(
                        this.headerOption(this.getText(prefix + NbtTags.BIOME)),
                        this.biomeOption(prefix + climateMapping + "." + NbtTags.BIOME, false),
                        this.headerOption(this.getText(prefix + NbtTags.OCEAN_BIOME)),
                        this.biomeOption(prefix + climateMapping + "." + NbtTags.OCEAN_BIOME, false),
                        this.headerOption(this.getText(prefix + NbtTags.DEEP_OCEAN_BIOME)),
                        this.biomeOption(prefix + climateMapping + "." + NbtTags.DEEP_OCEAN_BIOME, false)
                    );
                }
            }
        } else if (ModernBetaBuiltInTypes.Biome.FRACTAL.id.equals(biomeProvider)) {
            list.addSingleOptionEntry(this.headerOption(this.getText("header.scale").formatted(Formatting.BOLD)));
            list.addSingleOptionEntry(this.headerOption(Text.literal("Work in progress!").formatted(Formatting.ITALIC)));
        }
    }
}
