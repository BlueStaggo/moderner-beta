package mod.bluestaggo.modernerbeta.settings.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.util.CodecUtil;
import net.minecraft.util.StringIdentifiable;

public record CaveGeneration(
    boolean useCarvers,
    boolean useNoiseCaves,
    boolean fixCaveBorders,
    boolean forceBetaCaves,
    boolean forceBetaCanyons,
    SeedMethod seedMethod
) {
    public static final Codec<CaveGeneration> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.BOOL.fieldOf("useCarvers").orElse(true).forGetter(CaveGeneration::useCarvers),
            Codec.BOOL.fieldOf("useNoiseCaves").orElse(false).forGetter(CaveGeneration::useNoiseCaves),
            Codec.BOOL.fieldOf("fixCaveBorders").orElse(true).forGetter(CaveGeneration::fixCaveBorders),
            Codec.BOOL.fieldOf("forceBetaCaves").orElse(true).forGetter(CaveGeneration::forceBetaCaves),
            Codec.BOOL.fieldOf("forceBetaCanyons").orElse(true).forGetter(CaveGeneration::forceBetaCanyons),
            StringIdentifiable.createCodec(SeedMethod::values).fieldOf("seedMethod").orElse(SeedMethod.MODERN).forGetter(CaveGeneration::seedMethod)
        ).apply(instance, CaveGeneration::new)
    );
    public static final CaveGeneration DEFAULT = CodecUtil.getDefaultByMap(CODEC);

    public static final CaveGeneration DISABLED = new CaveGeneration(false, false, false, true, true, SeedMethod.MODERN);
    public static final CaveGeneration BETA = new CaveGeneration(true, false, false, true, true, SeedMethod.BETA);
    public static final CaveGeneration EARLY_RELEASE = new CaveGeneration(true, false, true, true, true, SeedMethod.EARLY_RELEASE);
    public static final CaveGeneration RELEASE_1_12_2 = new CaveGeneration(true, false, true, false, false, SeedMethod.MODERN);
    public static final CaveGeneration RELEASE_1_17_1 = new CaveGeneration(true, false, true, false, false, SeedMethod.MODERN);
    public static final CaveGeneration BEDROCK = new CaveGeneration(true, false, true, false, false, SeedMethod.BEDROCK);
    public static final CaveGeneration MODERN_BETA = new CaveGeneration(true, true, true, true, true, SeedMethod.MODERN);

    public enum SeedMethod implements StringIdentifiable {
        BETA("beta"),
        EARLY_RELEASE("early_release"),
        MODERN("modern"),
        BEDROCK("bedrock"),
        ;

        public final String id;

        SeedMethod(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return this.id;
        }
    }
}
