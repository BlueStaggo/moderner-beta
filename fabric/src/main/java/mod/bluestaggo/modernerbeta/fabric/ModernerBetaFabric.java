package mod.bluestaggo.modernerbeta.fabric;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

public class ModernerBetaFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register mod stuff
        registerDataPacks();

        ModernerBeta.init();
    }

    private static void registerDataPacks() {
        ModContainer modContainer = FabricLoader.getInstance().getModContainer(ModernerBeta.MOD_ID).orElseThrow();
        ResourceManagerHelper.registerBuiltinResourcePack(ModernerBeta.createId("reduced_height"), modContainer, ResourcePackActivationType.NORMAL);
    }
}
