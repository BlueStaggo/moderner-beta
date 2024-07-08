package mod.bluestaggo.modernerbeta.neoforge;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.resource.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.nio.file.Path;

@Mod(ModernerBeta.MOD_ID)
@Mod.EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModernerBetaNeoForge {
    @SubscribeEvent
    public static void commonInit(FMLConstructModEvent event) {
        ModernerBeta.init();
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == ResourceType.SERVER_DATA) {
            Path resourcePath = ModList.get().getModFileById(ModernerBeta.MOD_ID).getFile().findResource("resourcepacks/reduced_height");
            ResourcePackProfile pack = ResourcePackProfile.create(
                    "moderner_beta/reduced_height",
                    Text.of("Reduced Height"), //TODO: i18n perhaps?
                    false,
                    new DirectoryResourcePack.DirectoryBackedFactory(resourcePath, false),
                    ResourceType.SERVER_DATA,
                    ResourcePackProfile.InsertionPosition.TOP,
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
                    }
            );
            event.addRepositorySource(consumer -> consumer.accept(pack));
        }
    }
}
