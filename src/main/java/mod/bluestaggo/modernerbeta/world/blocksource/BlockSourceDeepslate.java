package mod.bluestaggo.modernerbeta.world.blocksource;

import mod.bluestaggo.modernerbeta.api.world.blocksource.BlockSource;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentTypes;
import mod.bluestaggo.modernerbeta.settings.component.DeepslateGeneration;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSplitter;

public class BlockSourceDeepslate implements BlockSource {
    private final int minY;
    private final int maxY;
    private final boolean useDeepslate;
    private final BlockState deepslateBlock;
    private final RandomSplitter randomSplitter;
    
    public BlockSourceDeepslate(ModernBetaSettings chunkSettings, RandomSplitter randomSplitter) {
        DeepslateGeneration deepslateGeneration = chunkSettings.getOrDefault(SettingsComponentTypes.DEEPSLATE_GENERATION);
        // Surface rules have their own deepslate generation.
        boolean surfaceRules = chunkSettings.getOrDefault(SettingsComponentTypes.USE_SURFACE_RULES);
        this.minY = deepslateGeneration.minY();
        this.maxY = deepslateGeneration.maxY();
        this.useDeepslate = !surfaceRules && deepslateGeneration.enabled();
        this.deepslateBlock = Registries.BLOCK.getOrThrow(RegistryKey.of(RegistryKeys.BLOCK, deepslateGeneration.block()))
            //? if >=1.21.2
            .value()
            .getDefaultState();
        this.randomSplitter = randomSplitter;
    }
    
    @Override
    public BlockState apply(int x, int y, int z) {
        if (!this.useDeepslate || y >= maxY)
            return null;
        
        if (y <= minY)
            return this.deepslateBlock;
        
        double yThreshold = MathHelper.lerp(MathHelper.getLerpProgress(y, minY, maxY), 1.0, 0.0);
        Random random = this.randomSplitter.split(x, y, z);
        
        return (double)random.nextFloat() < yThreshold ? this.deepslateBlock : null;
    }
}
