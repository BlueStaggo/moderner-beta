package mod.bluestaggo.modernerbeta.world.carver;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;

import java.util.Optional;

public class BetaCaveCarverConfig extends CaveCarverConfiguration {
    public static final Codec<BetaCaveCarverConfig> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            ((MapCodec.MapCodecCodec<CaveCarverConfiguration>) CaveCarverConfiguration.CODEC).codec().forGetter(config -> config),
            Codec.BOOL.optionalFieldOf("use_fixed_caves").forGetter(config -> config.useFixedCaves),
            Codec.BOOL.optionalFieldOf("use_aquifers").forGetter(config -> config.useAquifers),
            Codec.BOOL.optionalFieldOf("use_surface_rules").forGetter(config -> config.useSurfaceRules)
        ).apply(instance, BetaCaveCarverConfig::new));
    
    public Optional<Boolean> useFixedCaves;
    public Optional<Boolean> useAquifers;
    public Optional<Boolean> useSurfaceRules;

    public BetaCaveCarverConfig(
        float probability,
        HeightProvider y,
        FloatProvider yScale,
        VerticalAnchor lavaLevel,
        CarverDebugSettings debugConfig,
        HolderSet<Block> replaceable,
        FloatProvider horizontalRadiusMultiplier,
        FloatProvider verticalRadiusMultiplier,
        FloatProvider floorLevel,
        Optional<Boolean> useFixedCaves,
        Optional<Boolean> useAquifers,
        Optional<Boolean> useSurfaceRules
    ) {
        super(
            probability,
            y,
            yScale,
            lavaLevel,
            debugConfig,
            replaceable,
            horizontalRadiusMultiplier,
            verticalRadiusMultiplier,
            floorLevel
        );
        
        this.useFixedCaves = useFixedCaves;
        this.useAquifers = useAquifers;
        this.useSurfaceRules = useSurfaceRules;
    }
    
    public BetaCaveCarverConfig(
        CaveCarverConfiguration config,
        Optional<Boolean> useFixedCaves,
        Optional<Boolean> useAquifers,
        Optional<Boolean> useSurfaceRules
    ) {
        this(
            config.probability,
            config.y,
            config.yScale,
            config.lavaLevel,
            config.debugSettings,
            config.replaceable,
            config.horizontalRadiusMultiplier,
            config.verticalRadiusMultiplier,
            config.floorLevel,
            useFixedCaves,
            useAquifers,
            useSurfaceRules
        );
    }
}
