package mod.bluestaggo.modernerbeta.forgelike.mixin;

import com.google.common.collect.ImmutableMap;
import mod.bluestaggo.modernerbeta.forgelike.FMLUtils;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ModernerBetaForgeLikeMixinPlugin implements IMixinConfigPlugin {
    private static final Supplier<Boolean> TRUE = () -> true;
    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
        "mod.bluestaggo.modernerbeta.forgelike.mixin.compat.blueprint.ModdedBiomeSlicesManagerMixin", () -> FMLUtils.isModPresent("blueprint")
    );

    @Override
    public boolean shouldApplyMixin(String s, String s1) {
        return CONDITIONS.getOrDefault(s1, TRUE).get();
    }

    @Override
    public void onLoad(String s) {}

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {}

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
}
