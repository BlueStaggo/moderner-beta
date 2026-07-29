package mod.bluestaggo.modernerbeta.level.chunk.surface;

import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.util.random.BedrockRandomSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

import java.util.Arrays;
import java.util.List;

public final class LegacyBadlandsBands {
    private static final int SIZE = 64;

    private final BlockState[] bands = new BlockState[SIZE];
    private final PerlinSimplexNoise offsetNoise;

    public LegacyBadlandsBands(long seed, boolean bedrock) {
        RandomSource random = bedrock ? new BedrockRandomSource(seed) : new LegacyRandomSource(seed);

        Arrays.fill(this.bands, BlockStates.TERRACOTTA);
        this.offsetNoise = new PerlinSimplexNoise(random, List.of(0));

        for (int y = 0; y < SIZE; ++y) {
            y += random.nextInt(5) + 1;
            if (y < SIZE) {
                this.bands[y] = BlockStates.ORANGE_TERRACOTTA;
            }
        }

        this.addBands(random, BlockStates.YELLOW_TERRACOTTA, 1);
        this.addBands(random, BlockStates.BROWN_TERRACOTTA, 2);
        this.addBands(random, BlockStates.RED_TERRACOTTA, 1);

        int count = random.nextInt(3) + 3;
        int y = 0;
        for (int i = 0; i < count; ++i) {
            y += random.nextInt(16) + 4;
            if (y >= SIZE) {
                continue;
            }

            this.bands[y] = BlockStates.WHITE_TERRACOTTA;
            if (y > 1 && random.nextBoolean()) {
                this.bands[y - 1] = BlockStates.LIGHT_GRAY_TERRACOTTA;
            }
            if (y < SIZE - 1 && random.nextBoolean()) {
                this.bands[y + 1] = BlockStates.LIGHT_GRAY_TERRACOTTA;
            }
        }
    }

    public BlockState sample(int x, int y, int z) {
        int offset = (int)Math.round(this.offsetNoise.getValue(x / 512.0, z / 512.0, false) * 2.0);
        return this.bands[Math.floorMod(y + offset, SIZE)];
    }

    private void addBands(RandomSource random, BlockState state, int minWidth) {
        int count = random.nextInt(4) + 2;
        for (int i = 0; i < count; ++i) {
            int width = random.nextInt(3) + minWidth;
            int y = random.nextInt(SIZE);
            for (int offset = 0; y + offset < SIZE && offset < width; ++offset) {
                this.bands[y + offset] = state;
            }
        }
    }
}
