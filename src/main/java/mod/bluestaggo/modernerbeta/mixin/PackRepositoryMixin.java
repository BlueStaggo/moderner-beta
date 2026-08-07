package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSavedPresetPack;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.repository.ServerPacksSource;
//? if >=1.20.5
import net.minecraft.world.level.validation.DirectoryValidator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Mixin(PackRepository.class)
public abstract class PackRepositoryMixin {
    @Unique private boolean modernBeta$hasSavedPresetSource;

    @Shadow @Final @Mutable private Set<RepositorySource> sources;

    @Shadow
    public abstract Pack getPack(String id);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addSavedPresetSource(RepositorySource[] sources, CallbackInfo info) {
        Path configDir = ModernerBeta.getConfigDir();
        if (configDir == null || Arrays.stream(sources).noneMatch(ServerPacksSource.class::isInstance)) {
            return;
        }

        this.sources = new LinkedHashSet<>(this.sources);
        this.sources.add(new FolderRepositorySource(
            ModernBetaSavedPresetPack.getRoot(configDir).getParent(),
            PackType.SERVER_DATA,
            PackSource.BUILT_IN
            //? if >=1.20.5 {
            , new DirectoryValidator(path -> false)
            //?}
        ));
        this.modernBeta$hasSavedPresetSource = true;
    }

    @Inject(method = "rebuildSelected", at = @At("RETURN"), cancellable = true)
    private void selectSavedPresetPack(
        Collection<String> selectedIds,
        CallbackInfoReturnable<List<Pack>> info
    ) {
        Path configDir = ModernerBeta.getConfigDir();
        if (!this.modernBeta$hasSavedPresetSource || configDir == null || !ModernBetaSavedPresetPack.exists(configDir)) {
            return;
        }

        Pack pack = this.getPack(ModernBetaSavedPresetPack.PACK_ID);
        List<Pack> selected = info.getReturnValue();
        if (pack == null || selected.contains(pack)) {
            return;
        }

        List<Pack> updated = new ArrayList<>(selected);
        updated.add(pack);
        info.setReturnValue(List.copyOf(updated));
    }
}
