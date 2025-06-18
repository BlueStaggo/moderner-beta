package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.registry.ModernBetaClientRegistries;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.function.Consumer;

public abstract class ModernBetaGraphicalComponentedSettingsScreen extends ModernBetaGraphicalCompoundSettingsScreen {
    private Identifier currentComponentType;

    public ModernBetaGraphicalComponentedSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        String type,
        NbtCompound settings,
        Consumer<NbtCompound> onDone
    ) {
        super(title, parent, generatorOptionsHolder, type, settings, onDone);
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
            if ("self".equals(key)) {
                return new Pair<>(this.settings, this.currentComponentType.toString());
            }
            if (key.isEmpty()) {
                key = this.currentComponentType.toString();
            } else {
                key = this.currentComponentType + "." + key;
            }
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

    @SuppressWarnings("unchecked")
    protected void addOptionsForComponents(OptionListWidget list, List<SettingsComponentType<?>> componentTypes) {
        for (SettingsComponentType<?> componentType : componentTypes) {
            ModernBetaRegistries.SETTINGS_COMPONENT_TYPE.getKey(componentType)
                .ifPresent(componentTypeKey -> {
                    Identifier componentTypeId = componentTypeKey.getValue();

                    GraphicalConfigBuilder configBuilder
                        = ModernBetaClientRegistries.SETTINGS_COMPONENT_TYPE_GUI.get(componentTypeId);

                    if (configBuilder == null) {
                        list.addSingleOptionEntry(this.headerOption(
                            Text.translatable(STRING_PREFIX + "invalidComponentType", componentTypeId.toString())
                                .formatted(Formatting.RED, Formatting.ITALIC)));
                        return;
                    }

                    if (!this.settings.contains(componentTypeId.toString())) {
                        this.settings.put(
                            componentTypeId.toString(),
                            VersionCompat.getOrThrow(
                                ((Codec<Object>)componentType.codec())
                                    .encodeStart(NbtOps.INSTANCE, componentType.defaultValue())
                            )
                        );
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
