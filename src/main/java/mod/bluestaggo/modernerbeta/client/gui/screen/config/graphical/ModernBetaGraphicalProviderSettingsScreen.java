//~holders
package mod.bluestaggo.modernerbeta.client.gui.screen.config.graphical;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.api.level.provider.ProviderType;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettings;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.Optional;
import java.util.function.Consumer;

public class ModernBetaGraphicalProviderSettingsScreen extends ModernBetaGraphicalComponentedSettingsScreen {
    private final ModernBetaSettings settingsObject;

    private final Registry<? extends ProviderType> providerRegistry;
    private final Identifier[] providers;

    public final int worldMinY;
    public final int worldMaxY;

    public ModernBetaGraphicalProviderSettingsScreen(
        String title,
        Screen parent,
        WorldCreationContext context,
        CompoundTag settingsTag,
        ModernBetaSettings settings,
        Consumer<CompoundTag> onDone,
        Registry<? extends ProviderType> providerRegistry
    ) {
        super(title, parent, context, null, settingsTag, onDone);
        this.settingsObject = settings;
        this.providerRegistry = providerRegistry;
        this.providers = providerRegistry.listElements()
            .map(Holder::unwrapKey)
            .flatMap(Optional::stream)
            .map(ResourceKey::identifier)
            .sorted()
            .toArray(Identifier[]::new);

        DimensionType dimensionType = context.selectedDimensions().get(LevelStem.OVERWORLD)
            .map(LevelStem::type).map(Holder::value).orElse(null);
        if (dimensionType != null) {
            this.worldMinY = dimensionType.minY();
            this.worldMaxY = this.worldMinY + dimensionType.height();
        } else {
            this.worldMinY = -64;
            this.worldMaxY = 320;
        }
    }

    @Override
    protected void addOptions(OptionsList list) {
        String providerKey = ModernBetaBuiltInTypes.SettingsComponentType.PROVIDER.id.toString();
        Identifier providerId = VersionCompat.id(VersionCompat.unwrap(this.settings.getString(providerKey)));

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
            this.addOptionsForComponents(list, this.settingsObject, providerType.requiredSettingsComponents().get());
        }
    }
}
