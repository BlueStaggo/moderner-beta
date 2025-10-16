package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.api.world.provider.ProviderType;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.Optional;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModernBetaGraphicalProviderSettingsScreen extends ModernBetaGraphicalComponentedSettingsScreen {
    private final Registry<? extends ProviderType> providerRegistry;
    private final ResourceLocation[] providers;

    public final int worldMinY;
    public final int worldMaxY;

    public ModernBetaGraphicalProviderSettingsScreen(
        String title,
        Screen parent,
        WorldCreationContext generatorOptionsHolder,
        CompoundTag settings,
        Consumer<CompoundTag> onDone,
        Registry<? extends ProviderType> providerRegistry
    ) {
        super(title, parent, generatorOptionsHolder, null, settings, onDone);
        this.providerRegistry = providerRegistry;
        this.providers = providerRegistry.listElements()
            .map(Holder::unwrapKey)
            .flatMap(Optional::stream)
            .map(ResourceKey::location)
            .sorted()
            .toArray(ResourceLocation[]::new);

        ChunkGenerator chunkGenerator = generatorOptionsHolder.selectedDimensions().get(LevelStem.OVERWORLD)
            .map(LevelStem::generator).orElse(null);
        if (chunkGenerator != null) {
            this.worldMinY = chunkGenerator.getMinY();
            this.worldMaxY = this.worldMinY + chunkGenerator.getGenDepth();
        } else {
            this.worldMinY = -64;
            this.worldMaxY = 320;
        }
    }

    @Override
    protected void addOptions(OptionsList list) {
        String providerKey = ModernBetaBuiltInTypes.SettingsComponentType.PROVIDER.id.toString();
        ResourceLocation providerId = VersionCompat.id(VersionCompat.unwrap(this.settings.getString(providerKey)));

        list.addBig(this.primarySelectionOption(providerKey, this.providers));

        ProviderType providerType = this.providerRegistry
            //? if >=1.21.2 {
            .getValue
            //? } else {
            /*.get
            *///? }
                (providerId);
        if (providerType == null) {
            list.addBig(this.headerOption(Component.translatable(STRING_PREFIX + "invalidProvider")));
        } else {
            this.addOptionsForComponents(list, providerType.requiredSettingsComponents().get());
        }
    }
}
