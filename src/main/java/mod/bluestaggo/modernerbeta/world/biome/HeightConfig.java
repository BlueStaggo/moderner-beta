package mod.bluestaggo.modernerbeta.world.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.ExtendedBiomeId;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record HeightConfig(float depth, float scale, String type) {
	public static final Codec<HeightConfig> CODEC = Codec.STRING.comapFlatMap(HeightConfig::validate, HeightConfig::makeString);

	public static final HeightConfig DEFAULT = new HeightConfig(0.1F, 0.3F);
	public static final HeightConfig OCEAN = new HeightConfig(-1.0F, 0.4F);
	public static final HeightConfig DESERT = new HeightConfig(0.1F, 0.2F);
	public static final HeightConfig EXTREME_HILLS = new HeightConfig(0.2F, 1.3F);
	public static final HeightConfig BETA_HILLS = new HeightConfig(0.2F, 1.8F);
	public static final HeightConfig TAIGA = new HeightConfig(0.1F, 0.4F);
	public static final HeightConfig SWAMPLAND = new HeightConfig(-0.2F, 0.1F);
	public static final HeightConfig RIVER = new HeightConfig(-0.5F, 0.0F);
	public static final HeightConfig MOUNTAINS = new HeightConfig(0.2F, 1.2F, "hills");
	public static final HeightConfig MUSHROOM_ISLAND = new HeightConfig(0.2F, 1.0F);
	public static final HeightConfig MUSHROOM_ISLAND_SHORE = new HeightConfig(-1.0F, 0.1F, "shore");
	public static final HeightConfig BEACH = new HeightConfig(0.0F, 0.1F);
	public static final HeightConfig HILLS = new HeightConfig(0.2F, 0.7F, "hills");
	public static final HeightConfig SHORT_HILLS = new HeightConfig(0.2F, 0.6F, "hills");
	public static final HeightConfig EXTREME_HILLS_EDGE = new HeightConfig(0.2F, 0.8F, "hills");
	public static final HeightConfig JUNGLE = new HeightConfig(0.2F, 0.4F);
	public static final HeightConfig JUNGLE_HILLS = new HeightConfig(1.8F, 0.2F, "hills");
	public static final HeightConfig PLATEAU = new HeightConfig(1.8F, 0.2F);
	public static final HeightConfig SWAMPLAND_HILLS = new HeightConfig(-0.1F, 0.5F, "hills");
	public static final HeightConfig PLATEAU_HILL = new HeightConfig(1.8F, 0.2F, "hills");
	public static final HeightConfig DEEP_OCEAN = new HeightConfig(-1.8F, 0.2F);

	public static final Map<ExtendedBiomeId, HeightConfig> MAJOR_RELEASE_CONFIGS;

	static {
		Map<ExtendedBiomeId, HeightConfig> majorReleaseHeightConfigs = new HashMap<>();
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:ocean"),
			new HeightConfig(-1.0f, 0.2f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:warm_ocean"),
			new HeightConfig(-1.0f, 0.2f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:lukewarm_ocean"),
			new HeightConfig(-1.0f, 0.2f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:cold_ocean"),
			new HeightConfig(-1.0f, 0.2f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:plains"),
			new HeightConfig(0.125f, 0.1f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:desert"),
			new HeightConfig(0.125f, 0.1f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:windswept_hills"),
			new HeightConfig(1.0f, 1.0f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:forest"),
			new HeightConfig(0.1f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:taiga"),
			new HeightConfig(0.2f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:swamp"),
			new HeightConfig(-0.2f, 0.2f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:river"),
			new HeightConfig(-0.5f, 0f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:frozen_ocean"),
			new HeightConfig(-1.0f, 0.2f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:frozen_river"),
			new HeightConfig(-0.5f, 0f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:snowy_plains"),
			new HeightConfig(0.125f, 0.1f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:snowy_plains*hills"),
			new HeightConfig(0.45f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:mushroom_fields"),
			new HeightConfig(0.2f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:mushroom_fields*shore"),
			new HeightConfig(0.0f, 0.05f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:beach"),
			new HeightConfig(0.0f, 0.05f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:desert*hills"),
			new HeightConfig(0.45f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:forest*hills"),
			new HeightConfig(0.45f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:taiga*hills"),
			new HeightConfig(0.45f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:windswept_hills*edge"),
			new HeightConfig(0.8f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:jungle"),
			new HeightConfig(0.1f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:jungle*hills"),
			new HeightConfig(0.45f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:bamboo_jungle"),
			new HeightConfig(0.1f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:bamboo_jungle*hills"),
			new HeightConfig(0.45f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:sparse_jungle"),
			new HeightConfig(0.1f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:deep_ocean"),
			new HeightConfig(-1.8f, 0.2f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:deep_lukewarm_ocean"),
			new HeightConfig(-1.8f, 0.2f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:deep_cold_ocean"),
			new HeightConfig(-1.8f, 0.2f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:deep_frozen_ocean"),
			new HeightConfig(-1.8f, 0.2f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:stony_shore"),
			new HeightConfig(0.1f, 1.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:snowy_beach"),
			new HeightConfig(0.0f, 0.05f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:birch_forest"),
			new HeightConfig(0.1f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:birch_forest*hills"),
			new HeightConfig(0.45f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:dark_forest"),
			new HeightConfig(0.1f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:snowy_taiga"),
			new HeightConfig(0.2f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:snowy_taiga*hills"),
			new HeightConfig(0.45f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:old_growth_pine_taiga"),
			new HeightConfig(0.2f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:old_growth_pine_taiga*hills"),
			new HeightConfig(0.45f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:windswept_forest"),
			new HeightConfig(1.0f, 1.0f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:savanna"),
			new HeightConfig(0.125f, 0.1f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:savanna_plateau"),
			new HeightConfig(1.5f, 0.05f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:badlands"),
			new HeightConfig(0.1f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:wooded_badlands"),
			new HeightConfig(1.5f, 0.05f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:badlands*plateau"),
			new HeightConfig(1.5f, 0.05f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:sunflower_plains"),
			new HeightConfig(0.125f, 0.1f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:desert*lakes"),
			new HeightConfig(0.225f, 0.5f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:windswept_gravelly_hills"),
			new HeightConfig(1.0f, 1.0f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:windswept_gravelly_hills*modified"),
			new HeightConfig(1.0f, 1.0f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:flower_forest"),
			new HeightConfig(0.1f, 0.8f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:taiga*mountains"),
			new HeightConfig(0.3f, 0.8f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:swamp*hills"),
			new HeightConfig(-0.1f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:ice_spikes"),
			new HeightConfig(0.425f, 0.9f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:jungle*modified"),
			new HeightConfig(0.2f, 0.8f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:sparse_jungle*modified"),
			new HeightConfig(0.2f, 0.8f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:old_growth_birch_forest"),
			new HeightConfig(0.2f, 0.8f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:old_growth_birch_forest*hills"),
			new HeightConfig(0.55f, 1.0f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:dark_forest*hills"),
			new HeightConfig(0.2f, 0.8f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:snowy_taiga*mountains"),
			new HeightConfig(0.3f, 0.8f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:old_growth_spruce_taiga"),
			new HeightConfig(0.2f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:old_growth_spruce_taiga*hills"),
			new HeightConfig(0.2f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:windswept_savanna"),
			new HeightConfig(0.3625f, 2.45f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:windswept_savanna*plateau"),
			new HeightConfig(1.05f, 2.425f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:eroded_badlands"),
			new HeightConfig(0.1f, 0.4f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:wooded_badlands*modified"),
			new HeightConfig(0.45f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:badlands*modified_plateau"),
			new HeightConfig(0.45f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:cherry_grove"),
			new HeightConfig(1.5f, 0.6f));
		//? if >=1.21.4 {
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:pale_garden"),
			new HeightConfig(1.5f, 0.6f));
		//?}
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:mangrove_swamp"),
			new HeightConfig(-0.2f, 0.2f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:mangrove_swamp*hills"),
			new HeightConfig(-0.1f, 0.6f));
		majorReleaseHeightConfigs.put(
			ExtendedBiomeId.of("minecraft:meadow"),
			new HeightConfig(1.0f, 1.0f));
		MAJOR_RELEASE_CONFIGS = Collections.unmodifiableMap(majorReleaseHeightConfigs);
	}

	public HeightConfig(float depth, float scale) {
		this(depth, scale, null);
	}

	public static DataResult<HeightConfig> validate(String string) {
		String[] heightConfigPair = string.split(";");
		try {
			float scale = Float.parseFloat(heightConfigPair[0]);
			float depth = Float.parseFloat(heightConfigPair[1]);
			return DataResult.success(new HeightConfig(scale, depth));
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
			return DataResult.error(() -> String.format("Invalid height config \"%s\"", string));
		}
	}

	public static HeightConfig parse(String string, HeightConfig fallback) {
		return validate(string).result().orElse(fallback);
	}

	public static String makeString(float depth, float scale) {
		return depth + ";" + scale;
	}

	public String makeString() {
		return makeString(this.depth, this.scale);
	}

	@Override
	public String toString() {
		return String.format(
			"[depth=%.3f, scale=%.3f]",
			this.depth,
			this.scale
		);
	}
}
