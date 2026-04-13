package mod.bluestaggo.modernerbeta.level.biome.injection;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bluestaggo.modernerbeta.level.biome.injection.injector.BiomeInjector;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.biome.Biome;

import java.util.EnumSet;

public record BiomeInjectionRule(BiomeInjector injector, Step stepFor) {
    public static final Codec<BiomeInjectionRule> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            BiomeInjector.TYPE_CODEC.forGetter(BiomeInjectionRule::injector),
            StringRepresentable.fromEnum(Step::values).fieldOf("step_for").forGetter(BiomeInjectionRule::stepFor)
        ).apply(instance, BiomeInjectionRule::new)
    );

    public void initIfNeeded() {
        this.injector.initIfNeeded();
    }

    public Holder<Biome> apply(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        return injector.apply(context, biomeX, biomeY, biomeZ);
    }

    public boolean canFulfill(EnumSet<InjectionNeeds> ableToFulfill) {
        return ableToFulfill.containsAll(injector.needs());
    }

    public enum Step implements StringRepresentable {
        /**
         * Injects before surface generation step.
         */
        PRE("before_surface"),
        /**
         * Injects after surface generation step.
         */
        POST("after_surface"),
        /**
         * Injects for structure generation, spawn location.
         */
        ALL(null);

        private final String value;

        Step(String value) {
            this.value = value;
        }

        @Override
        public String getSerializedName() {
            return value;
        }
    }
}
