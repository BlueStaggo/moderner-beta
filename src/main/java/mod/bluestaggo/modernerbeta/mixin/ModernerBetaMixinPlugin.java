package mod.bluestaggo.modernerbeta.mixin;

import com.google.common.collect.ImmutableMap;
import mod.bluestaggo.modernerbeta.services.ModernBetaServices;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ModernerBetaMixinPlugin implements IMixinConfigPlugin {
    private static final Supplier<Boolean> TRUE = () -> true;
    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
        //? if <1.21.2
        //"mod.bluestaggo.modernerbeta.mixin.client.LevelRendererMixin", () -> !ModernBetaServices.PLATFORM.isModPresent("sereneseasons"),
        "mod.bluestaggo.modernerbeta.mixin.BiomeMixin", () -> !ModernBetaServices.PLATFORM.isModPresent("sereneseasons"),
        "mod.bluestaggo.modernerbeta.mixin.compat.sereneseasons.SeasonHooksMixin", () -> ModernBetaServices.PLATFORM.isModPresent("sereneseasons")
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
