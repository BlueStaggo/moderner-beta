package mod.bluestaggo.modernerbeta.neoforge;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@EventBusSubscriber(modid = ModernerBeta.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModernerBetaNeoForge {
    @SubscribeEvent
    public static void commonInit(FMLConstructModEvent event) {
        ModernerBeta.init();
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
