package mod.bluestaggo.modernerbeta.world.biome;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import org.slf4j.event.Level;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record HeightConfig(float depth, float scale, String type) {
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

	public static final Map<String, String> MAJOR_RELEASE_CONFIGS;

	static {
		Map<String, String> majorReleaseHeightConfigs = new HashMap<>();
		majorReleaseHeightConfigs.put("minecraft:ocean", "-1.0;0.2");
		majorReleaseHeightConfigs.put("minecraft:warm_ocean", "-1.0;0.2");
		majorReleaseHeightConfigs.put("minecraft:lukewarm_ocean", "-1.0;0.2");
		majorReleaseHeightConfigs.put("minecraft:cold_ocean", "-1.0;0.2");
		majorReleaseHeightConfigs.put("minecraft:plains", "0.125;0.1");
		majorReleaseHeightConfigs.put("minecraft:desert", "0.125;0.1");
		majorReleaseHeightConfigs.put("minecraft:windswept_hills", "1.0;1.0");
		majorReleaseHeightConfigs.put("minecraft:forest", "0.1;0.4");
		majorReleaseHeightConfigs.put("minecraft:taiga", "0.2;0.4");
		majorReleaseHeightConfigs.put("minecraft:swamp", "-0.2;0.2");
		majorReleaseHeightConfigs.put("minecraft:river", "-0.5;0");
		majorReleaseHeightConfigs.put("minecraft:frozen_ocean", "-1.0;0.2");
		majorReleaseHeightConfigs.put("minecraft:frozen_river", "-0.5;0");
		majorReleaseHeightConfigs.put("minecraft:snowy_plains", "0.125;0.1");
		majorReleaseHeightConfigs.put("minecraft:snowy_plains*hills", "0.45;0.6");
		majorReleaseHeightConfigs.put("minecraft:mushroom_fields", "0.2;0.6");
		majorReleaseHeightConfigs.put("minecraft:mushroom_fields*shore", "0.0;0.05");
		majorReleaseHeightConfigs.put("minecraft:beach", "0.0;0.05");
		majorReleaseHeightConfigs.put("minecraft:desert*hills", "0.45;0.6");
		majorReleaseHeightConfigs.put("minecraft:forest*hills", "0.45;0.6");
		majorReleaseHeightConfigs.put("minecraft:taiga*hills", "0.45;0.6");
		majorReleaseHeightConfigs.put("minecraft:windswept_hills*edge", "0.8;0.6");
		majorReleaseHeightConfigs.put("minecraft:jungle", "0.1;0.4");
		majorReleaseHeightConfigs.put("minecraft:jungle*hills", "0.45;0.6");
		majorReleaseHeightConfigs.put("minecraft:bamboo_jungle", "0.1;0.4");
		majorReleaseHeightConfigs.put("minecraft:bamboo_jungle*hills", "0.45;0.6");
		majorReleaseHeightConfigs.put("minecraft:sparse_jungle", "0.1;0.4");
		majorReleaseHeightConfigs.put("minecraft:deep_ocean", "-1.8;0.2");
		majorReleaseHeightConfigs.put("minecraft:deep_lukewarm_ocean", "-1.8;0.2");
		majorReleaseHeightConfigs.put("minecraft:deep_cold_ocean", "-1.8;0.2");
		majorReleaseHeightConfigs.put("minecraft:deep_frozen_ocean", "-1.8;0.2");
		majorReleaseHeightConfigs.put("minecraft:stony_shore", "0.1;1.6");
		majorReleaseHeightConfigs.put("minecraft:snowy_beach", "0.0;0.05");
		majorReleaseHeightConfigs.put("minecraft:birch_forest", "0.1;0.4");
		majorReleaseHeightConfigs.put("minecraft:birch_forest*hills", "0.45;0.6");
		majorReleaseHeightConfigs.put("minecraft:dark_forest", "0.1;0.4");
		majorReleaseHeightConfigs.put("minecraft:snowy_taiga", "0.2;0.4");
		majorReleaseHeightConfigs.put("minecraft:snowy_taiga*hills", "0.45;0.6");
		majorReleaseHeightConfigs.put("minecraft:old_growth_pine_taiga", "0.2;0.4");
		majorReleaseHeightConfigs.put("minecraft:old_growth_pine_taiga*hills", "0.45;0.6");
		majorReleaseHeightConfigs.put("minecraft:windswept_forest", "1.0;1.0");
		majorReleaseHeightConfigs.put("minecraft:savanna", "0.125;0.1");
		majorReleaseHeightConfigs.put("minecraft:savanna_plateau", "1.5;0.05");
		majorReleaseHeightConfigs.put("minecraft:badlands", "0.1;0.4");
		majorReleaseHeightConfigs.put("minecraft:wooded_badlands", "1.5;0.05");
		majorReleaseHeightConfigs.put("minecraft:badlands*plateau", "1.5;0.05");
		majorReleaseHeightConfigs.put("minecraft:sunflower_plains", "0.125;0.1");
		majorReleaseHeightConfigs.put("minecraft:desert*lakes", "0.225;0.5");
		majorReleaseHeightConfigs.put("minecraft:windswept_gravelly_hills", "1.0;1.0");
		majorReleaseHeightConfigs.put("minecraft:windswept_gravelly_hills*modified", "1.0;1.0");
		majorReleaseHeightConfigs.put("minecraft:flower_forest", "0.1;0.8");
		majorReleaseHeightConfigs.put("minecraft:taiga*mountains", "0.3;0.8");
		majorReleaseHeightConfigs.put("minecraft:swamp*hills", "-0.1;0.6");
		majorReleaseHeightConfigs.put("minecraft:ice_spikes", "0.425;0.9");
		majorReleaseHeightConfigs.put("minecraft:jungle*modified", "0.2;0.8");
		majorReleaseHeightConfigs.put("minecraft:sparse_jungle*modified", "0.2;0.8");
		majorReleaseHeightConfigs.put("minecraft:old_growth_birch_forest", "0.2;0.8");
		majorReleaseHeightConfigs.put("minecraft:old_growth_birch_forest*hills", "0.55;1.0");
		majorReleaseHeightConfigs.put("minecraft:dark_forest*hills", "0.2;0.8");
		majorReleaseHeightConfigs.put("minecraft:snowy_taiga*mountains", "0.3;0.8");
		majorReleaseHeightConfigs.put("minecraft:old_growth_spruce_taiga", "0.2;0.4");
		majorReleaseHeightConfigs.put("minecraft:old_growth_spruce_taiga*hills", "0.2;0.4");
		majorReleaseHeightConfigs.put("minecraft:windswept_savanna", "0.3625;2.45");
		majorReleaseHeightConfigs.put("minecraft:windswept_savanna*plateau", "1.05;2.425");
		majorReleaseHeightConfigs.put("minecraft:eroded_badlands", "0.1;0.4");
		majorReleaseHeightConfigs.put("minecraft:wooded_badlands*modified", "0.45;0.6");
		majorReleaseHeightConfigs.put("minecraft:badlands*modified_plateau", "0.45;0.6");
		majorReleaseHeightConfigs.put("minecraft:cherry_grove", "1.5;0.6");
		majorReleaseHeightConfigs.put("minecraft:pale_garden", "1.5;0.6");
		majorReleaseHeightConfigs.put("minecraft:mangrove_swamp", "-0.2;0.2");
		majorReleaseHeightConfigs.put("minecraft:mangrove_swamp*hills", "-0.1;0.6");
		majorReleaseHeightConfigs.put("minecraft:meadow", "1.0;1.0");
		MAJOR_RELEASE_CONFIGS = Collections.unmodifiableMap(majorReleaseHeightConfigs);
	}

	public HeightConfig(float depth, float scale) {
		this(depth, scale, null);
	}

	public static HeightConfig parse(String string, HeightConfig fallback) {
		String[] heightConfigPair = string.split(";");
		try {
			float scale = Float.parseFloat(heightConfigPair[0]);
			float depth = Float.parseFloat(heightConfigPair[1]);
			return new HeightConfig(scale, depth);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
			ModernerBeta.log(Level.WARN, String.format("Invalid height config \"%s\"", string));
			return fallback;
		}
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
