package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.provider.ProviderType;
import mod.bluestaggo.modernerbeta.client.registry.ModernBetaClientRegistries;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModernBetaGraphicalProviderSettingsScreen extends ModernBetaGraphicalCompoundSettingsScreen {
    private final Registry<? extends ProviderType> providerRegistry;
    private final Identifier[] providers;
    private Identifier currentComponentType;

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
        this.providers = providerRegistry.streamKeys()
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
            this.worldMaxY = 384;
        }
    }

    protected String getCurrentComponentTypeAsString() {
        String currentComponentTypeString = this.currentComponentType.getPath();
        if (!this.currentComponentType.getNamespace().equals(ModernerBeta.MOD_ID)) {
            currentComponentTypeString = this.currentComponentType.getNamespace() + "." + currentComponentTypeString;
        }
        return currentComponentTypeString;
    }

    @Override
    protected Pair<NbtCompound, String> resolveSettings(String key) {
        if (this.currentComponentType != null) {
            key = this.currentComponentType + "." + key;
        }
        return super.resolveSettings(key);
    }

    @Override
    protected String getTextKey(String key, String subKey) {
        String text = STRING_PREFIX;
        if (this.currentComponentType != null) {
            String currentComponentTypeString = this.currentComponentType.getPath();
            if (!this.currentComponentType.getNamespace().equals(ModernerBeta.MOD_ID)) {
                currentComponentTypeString = this.currentComponentType.getNamespace() + "." + currentComponentTypeString;
            }
            text += currentComponentTypeString + ".";
        }

        int colon = key.indexOf(':');
        if (colon != -1) {
            key = key.substring(colon + 1);
        }

        text += key;
        if (subKey != null) {
            text += "." + subKey;
        }
        return text;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addOptions(OptionListWidget list) {
        String providerKey = ModernBetaBuiltInTypes.SettingsComponentType.PROVIDER.id.toString();
        Identifier providerId = Identifier.of(this.settings.getString(providerKey).orElseThrow());

        list.addSingleOptionEntry(this.primarySelectionOption(providerKey, this.providers));

        ProviderType providerType = this.providerRegistry.get(providerId);
        if (providerType == null) {
            list.addSingleOptionEntry(this.headerOption(Text.translatable(STRING_PREFIX + "invalidProvider")));
        } else {
            for (SettingsComponentType<?> componentType : providerType.requiredSettingsComponents()) {
                ModernBetaRegistries.SETTINGS_COMPONENT_TYPE.getKey(componentType)
                    .ifPresent(componentTypeKey -> {
                        Identifier componentTypeId = componentTypeKey.getValue();

                        GraphicalConfigBuilder configBuilder
                            = ModernBetaClientRegistries.GRAPHICAL_CONFIG_BUILDER.get(componentTypeId);

                        if (configBuilder == null) {
                            list.addSingleOptionEntry(this.headerOption(
                                Text.translatable(STRING_PREFIX + "invalidComponentType", componentTypeId)
                                    .formatted(Formatting.RED, Formatting.ITALIC)));
                            return;
                        }

                        if (!this.settings.contains(componentTypeId.toString())) {
                            this.settings.put(componentTypeId.toString(), (Codec<Object>)componentType.codec(), componentType.defaultValue());
                        }

                        this.currentComponentType = componentTypeId;
                        list.addSingleOptionEntry(this.headerOption(
                            Text.translatable(STRING_PREFIX + this.getCurrentComponentTypeAsString())));
                        configBuilder.apply(this, list);
                        this.currentComponentType = null;
                    });
            }
        }
    }
}
