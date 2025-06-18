package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.api.world.provider.ProviderType;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.Optional;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModernBetaGraphicalProviderSettingsScreen extends ModernBetaGraphicalComponentedSettingsScreen {
    private final Registry<? extends ProviderType> providerRegistry;
    private final Identifier[] providers;

    public final int worldMinY;
    public final int worldMaxY;

    public ModernBetaGraphicalProviderSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        NbtCompound settings,
        Consumer<NbtCompound> onDone,
        Registry<? extends ProviderType> providerRegistry
    ) {
        super(title, parent, generatorOptionsHolder, null, settings, onDone);
        this.providerRegistry = providerRegistry;
        this.providers = providerRegistry.streamEntries()
            .map(RegistryEntry::getKey)
            .flatMap(Optional::stream)
            .map(RegistryKey::getValue)
            .sorted()
            .toArray(Identifier[]::new);

        ChunkGenerator chunkGenerator = generatorOptionsHolder.selectedDimensions().getOrEmpty(DimensionOptions.OVERWORLD)
            .map(DimensionOptions::chunkGenerator).orElse(null);
        if (chunkGenerator != null) {
            this.worldMinY = chunkGenerator.getMinimumY();
            this.worldMaxY = this.worldMinY + chunkGenerator.getWorldHeight();
        } else {
            this.worldMinY = -64;
            this.worldMaxY = 320;
        }
    }

    @Override
    protected void addOptions(OptionListWidget list) {
        String providerKey = ModernBetaBuiltInTypes.SettingsComponentType.PROVIDER.id.toString();
        Identifier providerId = VersionCompat.id(VersionCompat.unwrap(this.settings.getString(providerKey)));

        list.addSingleOptionEntry(this.primarySelectionOption(providerKey, this.providers));

        ProviderType providerType = this.providerRegistry.get(providerId);
        if (providerType == null) {
            list.addSingleOptionEntry(this.headerOption(Text.translatable(STRING_PREFIX + "invalidProvider")));
        } else {
            this.addOptionsForComponents(list, providerType.requiredSettingsComponents().get());
        }
    }
}
