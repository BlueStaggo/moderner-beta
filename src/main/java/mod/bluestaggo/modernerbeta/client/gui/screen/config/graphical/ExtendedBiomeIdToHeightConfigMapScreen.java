//~dotLocation
package mod.bluestaggo.modernerbeta.client.gui.screen.config.graphical;

import mod.bluestaggo.modernerbeta.level.biome.HeightConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biomes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ExtendedBiomeIdToHeightConfigMapScreen extends ModernBetaGraphicalMapSettingsScreen {
    public ExtendedBiomeIdToHeightConfigMapScreen(String title, Screen parent, WorldCreationContext generatorOptionsHolder, CompoundTag settings, Consumer<CompoundTag> onDone) {
        super(title, parent, generatorOptionsHolder, settings, onDone);
    }

    @Override
    protected List<OptionInstance<?>> getOptions(int i) {
        ArrayList<OptionInstance<?>> options = new ArrayList<>();
        options.add(this.headerOption(Component.translatable(this.getTextKey("item"), i).withStyle(ChatFormatting.BOLD)));
        options.add(null);
        options.add(this.extendedBiomeIdOption(KEY + i));
        options.addAll(this.heightConfigOption(VALUE + i));
        return options;
    }

    @Override
    protected String getDefaultKey() {
        return Biomes.PLAINS.location().toString();
    }

    @Override
    protected Tag getDefaultValue() {
        return StringTag.valueOf(HeightConfig.DEFAULT.makeString());
    }
}
