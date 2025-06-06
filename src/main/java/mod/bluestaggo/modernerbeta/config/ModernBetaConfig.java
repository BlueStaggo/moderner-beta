package mod.bluestaggo.modernerbeta.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.util.VersionCompat;

import java.util.HashMap;
import java.util.Map;

@Config(name = ModernerBeta.MOD_ID)
public class ModernBetaConfig implements ConfigData {
    @ConfigEntry.Category(value = "betaBiomeColor")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean useBetaSkyColor = true;

    @ConfigEntry.Category(value = "betaBiomeColor")
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean useBetaBiomeColor = true;

    @ConfigEntry.Category(value = "betaBiomeColor")
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean useBetaWaterColor = false;

    @ConfigEntry.Category(value = "peBiomeColor")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean usePEBetaSkyColor = false;

    @ConfigEntry.Category(value = "peBiomeColor")
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean usePEBetaBiomeColor = false;

    @ConfigEntry.Category(value = "peBiomeColor")
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean usePEBetaWaterColor = false;

    @ConfigEntry.Category(value = "other")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean useOldFogColor = true;

    @ConfigEntry.Category(value = "other")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public String defaultSettingsPreset = "moderner_beta:beta";

    @ConfigEntry.Category(value = "other")
    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Gui.Tooltip(count = 4)
    public Map<String, Integer> biomePreviewColors = new HashMap<>();

    {
        // Colors sourced from Cubiomes (https://github.com/Cubitect/cubiomes/blob/e61f90580cbdd883214a8054670dacae655e59c0/util.c#L316)
        biomePreviewColors.put("minecraft:ocean", 0x000070);
        biomePreviewColors.put("minecraft:plains", 0x8db360);
        biomePreviewColors.put("moderner_beta:late_beta_plains", 0x8db360);
        biomePreviewColors.put("minecraft:desert", 0xfa9418);
        biomePreviewColors.put("minecraft:windswept_hills", 0x606060);
        biomePreviewColors.put("moderner_beta:late_beta_extreme_hills", 0x606060);
        biomePreviewColors.put("moderner_beta:early_release_extreme_hills", 0x606060);
        biomePreviewColors.put("minecraft:forest", 0x056621);
        biomePreviewColors.put("minecraft:taiga", 0x0b6a5f);
        biomePreviewColors.put("moderner_beta:late_beta_taiga", 0x0b6a5f);
        biomePreviewColors.put("moderner_beta:early_release_taiga", 0x0b6a5f);
        biomePreviewColors.put("minecraft:swamp", 0x07f9b2);
        biomePreviewColors.put("moderner_beta:late_beta_swampland", 0x07f9b2);
        biomePreviewColors.put("moderner_beta:early_release_swampland", 0x07f9b2);
        biomePreviewColors.put("minecraft:river", 0x0000ff);
        biomePreviewColors.put("minecraft:river*region_a", 0x7f00ff);
        biomePreviewColors.put("minecraft:river*region_b", 0x007fff);
        biomePreviewColors.put("minecraft:nether_wastes", 0x572526);
        biomePreviewColors.put("minecraft:the_end", 0x8080ff);
        biomePreviewColors.put("minecraft:frozen_ocean", 0x7070d6);
        biomePreviewColors.put("minecraft:frozen_river", 0xa0a0ff);
        biomePreviewColors.put("minecraft:snowy_plains", 0xffffff);
        biomePreviewColors.put("moderner_beta:late_beta_ice_plains", 0xffffff);
        biomePreviewColors.put("moderner_beta:early_release_ice_plains", 0xffffff);
        biomePreviewColors.put("minecraft:snowy_plains*hills", 0xa0a0a0);
        biomePreviewColors.put("moderner_beta:late_beta_ice_plains*hills", 0xa0a0a0);
        biomePreviewColors.put("moderner_beta:early_release_ice_plains*hills", 0xa0a0a0);
        biomePreviewColors.put("minecraft:mushroom_fields", 0xff00ff);
        biomePreviewColors.put("minecraft:mushroom_fields*shore", 0xa000ff);
        biomePreviewColors.put("minecraft:beach", 0xfade55);
        biomePreviewColors.put("minecraft:desert*hills", 0xd25f12);
        biomePreviewColors.put("minecraft:forest*hills", 0x22551c);
        biomePreviewColors.put("minecraft:taiga*hills", 0x163933);
        biomePreviewColors.put("moderner_beta:late_beta_taiga*hills", 0x163933);
        biomePreviewColors.put("moderner_beta:early_release_taiga*hills", 0x163933);
        biomePreviewColors.put("minecraft:windswept_hills*edge", 0x72789a);
        biomePreviewColors.put("moderner_beta:late_beta_extreme_hills*edge", 0x72789a);
        biomePreviewColors.put("moderner_beta:early_release_extreme_hills*edge", 0x72789a);
        biomePreviewColors.put("minecraft:jungle", 0x507b0a);
        biomePreviewColors.put("minecraft:jungle*hills", 0x2c4205);
        biomePreviewColors.put("minecraft:sparse_jungle", 0x60930f);
        biomePreviewColors.put("minecraft:deep_ocean", 0x000030);
        biomePreviewColors.put("minecraft:stony_shore", 0xa2a284);
        biomePreviewColors.put("minecraft:snowy_beach", 0xfaf0c0);
        biomePreviewColors.put("minecraft:birch_forest", 0x307444);
        biomePreviewColors.put("minecraft:birch_forest*hills", 0x1f5f32);
        biomePreviewColors.put("minecraft:dark_forest", 0x40511a);
        biomePreviewColors.put("minecraft:snowy_taiga", 0x31554a);
        biomePreviewColors.put("minecraft:snowy_taiga*hills", 0x243f36);
        biomePreviewColors.put("minecraft:old_growth_pine_taiga", 0x596651);
        biomePreviewColors.put("minecraft:old_growth_pine_taiga*hills", 0x454f3e);
        biomePreviewColors.put("minecraft:windswept_forest", 0x5b7352);
        biomePreviewColors.put("minecraft:savanna", 0xbdb25f);
        biomePreviewColors.put("minecraft:savanna_plateau", 0xa79d64);
        biomePreviewColors.put("minecraft:badlands", 0xd94515);
        biomePreviewColors.put("minecraft:wooded_badlands", 0xb09765);
        biomePreviewColors.put("minecraft:badlands*plateau", 0xca8c65);
        biomePreviewColors.put("minecraft:small_end_islands", 0x4b4bab);
        biomePreviewColors.put("minecraft:end_midlands", 0xc9c959);
        biomePreviewColors.put("minecraft:end_highlands", 0xb5b536);
        biomePreviewColors.put("minecraft:end_barrens", 0x7070cc);
        biomePreviewColors.put("minecraft:warm_ocean", 0x0000ac);
        biomePreviewColors.put("minecraft:lukewarm_ocean", 0x000090);
        biomePreviewColors.put("minecraft:cold_ocean", 0x202070);
        biomePreviewColors.put("minecraft:deep_lukewarm_ocean", 0x000040);
        biomePreviewColors.put("minecraft:deep_cold_ocean", 0x202038);
        biomePreviewColors.put("minecraft:deep_frozen_ocean", 0x404090);
        biomePreviewColors.put("minecraft:the_void", 0x000000);
        biomePreviewColors.put("minecraft:the_void*mutation", 0xff00ff);
        biomePreviewColors.put("minecraft:sunflower_plains", 0xb5db88);
        biomePreviewColors.put("minecraft:desert*lakes", 0xffbc40);
        biomePreviewColors.put("minecraft:windswept_gravelly_hills", 0x888888);
        biomePreviewColors.put("minecraft:flower_forest", 0x2d8e49);
        biomePreviewColors.put("minecraft:taiga*mountains", 0x339287);
        biomePreviewColors.put("minecraft:swamp_hills", 0x2fffda);
        biomePreviewColors.put("minecraft:ice_spikes", 0xb4dcdc);
        biomePreviewColors.put("minecraft:jungle*modified", 0x78a332);
        biomePreviewColors.put("minecraft:sparse_jungle*modified", 0x88bb37);
        biomePreviewColors.put("minecraft:old_growth_birch_forest", 0x589c6c);
        biomePreviewColors.put("minecraft:old_growth_birch_forest*hills", 0x47875a);
        biomePreviewColors.put("minecraft:dark_forest*hills", 0x687942);
        biomePreviewColors.put("minecraft:snowy_taiga*mountains", 0x597d72);
        biomePreviewColors.put("minecraft:old_growth_spruce_taiga", 0x818e79);
        biomePreviewColors.put("minecraft:old_growth_spruce_taiga*hills", 0x6d7766);
        biomePreviewColors.put("minecraft:windswept_gravelly_hills*modified", 0x839b7a);
        biomePreviewColors.put("minecraft:windswept_savanna", 0xe5da87);
        biomePreviewColors.put("minecraft:windswept_savanna*plateau", 0xcfc58c);
        biomePreviewColors.put("minecraft:eroded_badlands", 0xff6d3d);
        biomePreviewColors.put("minecraft:wooded_badlands*modified", 0xd8bf8d);
        biomePreviewColors.put("minecraft:badlands*modified_plateau", 0xf2b48d);
        biomePreviewColors.put("minecraft:bamboo_jungle", 0x849500);
        biomePreviewColors.put("minecraft:bamboo_jungle*hills", 0x5c6c04);
        biomePreviewColors.put("minecraft:soul_sand_valley", 0x4d3a2e);
        biomePreviewColors.put("minecraft:crimson_forest", 0x981a11);
        biomePreviewColors.put("minecraft:warped_forest", 0x49907b);
        biomePreviewColors.put("minecraft:basalt_deltas", 0x645f63);
        biomePreviewColors.put("minecraft:dripstone_caves", 0x4e3012);
        biomePreviewColors.put("minecraft:lush_caves", 0x283c00);
        biomePreviewColors.put("minecraft:meadow", 0x60a445);
        biomePreviewColors.put("minecraft:grove", 0x47726c);
        biomePreviewColors.put("minecraft:snowy_slopes", 0xc4c4c4);
        biomePreviewColors.put("minecraft:jagged_peaks", 0xdcdcc8);
        biomePreviewColors.put("minecraft:frozen_peaks", 0xb0b3ce);
        biomePreviewColors.put("minecraft:stony_peaks", 0x7b8f74);
        biomePreviewColors.put("minecraft:deep_dark", 0x031f29);
        biomePreviewColors.put("minecraft:mangrove_swamp", 0x2ccc8e);
        biomePreviewColors.put("minecraft:cherry_grove", 0xff91c8);
        biomePreviewColors.put("minecraft:pale_garden", 0x696d95);
        biomePreviewColors.put("moderner_beta:beta_rainforest", 0x08fa36);
        biomePreviewColors.put("moderner_beta:beta_swampland", 0x07f9b2);
        biomePreviewColors.put("moderner_beta:beta_seasonal_forest", 0x9be023);
        biomePreviewColors.put("moderner_beta:beta_forest", 0x056621);
        biomePreviewColors.put("moderner_beta:beta_oak_forest", 0x056621);
        biomePreviewColors.put("moderner_beta:beta_savanna", 0xd9e023);
        biomePreviewColors.put("moderner_beta:beta_shrubland", 0xa1ad20);
        biomePreviewColors.put("moderner_beta:beta_taiga", 0x2eb153);
        biomePreviewColors.put("moderner_beta:beta_oak_taiga", 0x2eb153);
        biomePreviewColors.put("moderner_beta:beta_desert", 0xfa9418);
        biomePreviewColors.put("moderner_beta:beta_plains", 0xffd910);
        biomePreviewColors.put("moderner_beta:beta_ice_desert", 0xffed93);
        biomePreviewColors.put("moderner_beta:beta_tundra", 0x57eb59);
        biomePreviewColors.put("moderner_beta:beta_sky", 0x8080ff);
        biomePreviewColors.put("moderner_beta:beta_warm_ocean", 0x0000ac);
        biomePreviewColors.put("moderner_beta:beta_lukewarm_ocean", 0x000090);
        biomePreviewColors.put("moderner_beta:beta_ocean", 0x000070);
        biomePreviewColors.put("moderner_beta:beta_cold_ocean", 0x202070);
        biomePreviewColors.put("moderner_beta:beta_frozen_ocean", 0x7070d6);
        biomePreviewColors.put("moderner_beta:pe_rainforest", 0x08fa36);
        biomePreviewColors.put("moderner_beta:pe_swampland", 0x07f9b2);
        biomePreviewColors.put("moderner_beta:pe_seasonal_forest", 0x9be023);
        biomePreviewColors.put("moderner_beta:pe_forest", 0x056621);
        biomePreviewColors.put("moderner_beta:pe_savanna", 0xd9e023);
        biomePreviewColors.put("moderner_beta:pe_shrubland", 0xa1ad20);
        biomePreviewColors.put("moderner_beta:pe_taiga", 0x2eb153);
        biomePreviewColors.put("moderner_beta:pe_desert", 0xfa9418);
        biomePreviewColors.put("moderner_beta:pe_plains", 0xffd910);
        biomePreviewColors.put("moderner_beta:pe_ice_desert", 0xffed93);
        biomePreviewColors.put("moderner_beta:pe_tundra", 0x57eb59);
        biomePreviewColors.put("moderner_beta:pe_warm_ocean", 0x0000ac);
        biomePreviewColors.put("moderner_beta:pe_lukewarm_ocean", 0x000090);
        biomePreviewColors.put("moderner_beta:pe_ocean", 0x000070);
        biomePreviewColors.put("moderner_beta:pe_cold_ocean", 0x202070);
        biomePreviewColors.put("moderner_beta:pe_frozen_ocean", 0x7070d6);
    }

    @Override
    public void validatePostLoad() {
        try {
            VersionCompat.id(this.defaultSettingsPreset);
        } catch (Exception e) {
            this.defaultSettingsPreset = ModernerBeta.createId("beta").toString();
        }
    }
}
