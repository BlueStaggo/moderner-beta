package mod.bluestaggo.modernerbeta.level.blocksource;

import mod.bluestaggo.modernerbeta.api.level.blocksource.BlockSource;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.DeepslateGeneration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public class BlockSourceDeepslate implements BlockSource {
    private final int minY;
    private final int maxY;
    private final boolean useDeepslate;
    private final BlockState deepslateBlock;
    private final PositionalRandomFactory randomFactory;
    
    public BlockSourceDeepslate(ModernBetaSettings chunkSettings, PositionalRandomFactory randomFactory) {
        DeepslateGeneration deepslateGeneration = chunkSettings.getOrDefault(SettingsComponentTypes.DEEPSLATE_GENERATION);
        this.minY = deepslateGeneration.minY();
        this.maxY = deepslateGeneration.maxY();
        this.useDeepslate = deepslateGeneration.enabled();
        this.deepslateBlock = BuiltInRegistries.BLOCK.getOrThrow(ResourceKey.create(Registries.BLOCK, deepslateGeneration.block()))
            //? if >=1.21.2
            .value()
            .defaultBlockState();
        this.randomFactory = randomFactory;
    }
    
    @Override
    public BlockState apply(int x, int y, int z) {
        if (!this.useDeepslate || y >= maxY)
            return null;
        
        if (y <= minY)
            return this.deepslateBlock;
        
        double yThreshold = Mth.lerp(Mth.inverseLerp(y, minY, maxY), 1.0, 0.0);
        RandomSource random = this.randomFactory.at(x, y, z);
        
        return (double)random.nextFloat() < yThreshold ? this.deepslateBlock : null;
    }
}
