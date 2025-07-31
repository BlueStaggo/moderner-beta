package mod.bluestaggo.modernerbeta.world.carver;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import net.minecraft.world.gen.heightprovider.HeightProvider;

import java.util.Optional;

public class BetaCaveCarverConfig extends CaveCarverConfig {
    public static final Codec<BetaCaveCarverConfig> CAVE_CODEC = RecordCodecBuilder.create(instance -> 
        instance.group(
            ((MapCodec.MapCodecCodec<CaveCarverConfig>) CaveCarverConfig.CAVE_CODEC).codec().forGetter(config -> config),
            Codec.BOOL.optionalFieldOf("use_fixed_caves").forGetter(config -> config.useFixedCaves),
            Codec.BOOL.optionalFieldOf("use_aquifers").forGetter(config -> config.useAquifers)
        ).apply(instance, BetaCaveCarverConfig::new));
    
    public Optional<Boolean> useFixedCaves;
    public Optional<Boolean> useAquifers;
    
    public BetaCaveCarverConfig(
        float probability,
        HeightProvider y,
        FloatProvider yScale,
        YOffset lavaLevel,
        CarverDebugConfig debugConfig,
        RegistryEntryList<Block> replaceable,
        FloatProvider horizontalRadiusMultiplier,
        FloatProvider verticalRadiusMultiplier,
        FloatProvider floorLevel,
        Optional<Boolean> useFixedCaves,
        Optional<Boolean> useAquifers
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
    }
    
    public BetaCaveCarverConfig(
        CaveCarverConfig config,
        Optional<Boolean> useFixedCaves,
        Optional<Boolean> useAquifers
    ) {
        this(
            config.probability,
            config.y,
            config.yScale,
            config.lavaLevel,
            config.debugConfig,
            config.replaceable,
            config.horizontalRadiusMultiplier,
            config.verticalRadiusMultiplier,
            config.floorLevel,
            useFixedCaves,
            useAquifers
        );
    }
}
