package mod.bluestaggo.modernerbeta.neoforge;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.neoforge.registry.RegistryHelperImpl;
import mod.bluestaggo.modernerbeta.registry.IRegistryHandler;
import mod.bluestaggo.modernerbeta.registry.RegistryHelper;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.registry.VanillaRegistryHandler;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventsCommon {
    @SubscribeEvent
    public static void commonInit(FMLConstructModEvent event) {
        ModernerBeta.init();

        if (!FMLLoader.isProduction())
            ModernerBeta.DEV_ENV = true;
    }

    private static final Consumer<IRegistryHandler<?>> NONE = h -> {};
    @SubscribeEvent
    public static void registerToRegistries(RegisterEvent event) {
        Registry<?> registry = event.getRegistry();

        VanillaRegistryHandler<?> registryHandler = new VanillaRegistryHandler<>(registry);
        ModernerBeta.REGISTRY_HANDLERS.getOrDefault(registry, NONE).accept(registryHandler);
        ModernerBeta.CUSTOM_REGISTRY_HANDLERS.getOrDefault(registry, NONE).accept(registryHandler);
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        RegistryHelper registryHelper = new RegistryHelperImpl(event);
        ModernBetaRegistries.makeRegistries(registryHelper);
        ModernerBeta.setupCustomRegistryHandlers();
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == ResourceType.SERVER_DATA)
            event.addPackFinders(
                    ModernerBeta.createId("resourcepacks/reduced_height"),
                    ResourceType.SERVER_DATA,
                    Text.of("Reduced Height"), //TODO: i18n perhaps?
                    new ResourcePackSource() {
                        @Override
                        public Text decorate(Text packDisplayName) {
                            return Text.translatable("pack.nameAndSource", packDisplayName,
                                    Text.translatable("pack.source.builtin")).formatted(Formatting.GRAY);
                        }

                        @Override
                        public boolean canBeEnabledLater() {
                            return false;
                        }
                    },
                    false,
                    ResourcePackProfile.InsertionPosition.TOP
            );
    }
}
