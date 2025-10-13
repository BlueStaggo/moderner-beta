package mod.bluestaggo.modernerbeta.client.gui.screen.config;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.client.registry.ModernBetaClientRegistries;
import mod.bluestaggo.modernerbeta.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.settings.SettingsComponentType;
import mod.bluestaggo.modernerbeta.util.VersionCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;

import java.util.List;
import java.util.function.Consumer;

public abstract class ModernBetaGraphicalComponentedSettingsScreen extends ModernBetaGraphicalCompoundSettingsScreen {
    private ResourceLocation currentComponentType;

    public ModernBetaGraphicalComponentedSettingsScreen(
        String title,
        Screen parent,
        WorldCreationContext generatorOptionsHolder,
        String type,
        CompoundTag settings,
        Consumer<CompoundTag> onDone
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
    protected Tuple<CompoundTag, String> resolveSettings(String key) {
        if (this.currentComponentType != null) {
            if ("self".equals(key)) {
                return new Tuple<>(this.settings, this.currentComponentType.toString());
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
    protected void addOptionsForComponents(OptionsList list, List<SettingsComponentType<?>> componentTypes) {
        for (SettingsComponentType<?> componentType : componentTypes) {
            ModernBetaRegistries.SETTINGS_COMPONENT_TYPE.getResourceKey(componentType)
                .ifPresent(componentTypeKey -> {
                    ResourceLocation componentTypeId = componentTypeKey.location();

                    GraphicalConfigBuilder configBuilder
                        = ModernBetaClientRegistries.SETTINGS_COMPONENT_TYPE_GUI.getValue(componentTypeId);

                    if (configBuilder == null) {
                        list.addBig(this.headerOption(
                            Component.translatable(STRING_PREFIX + "invalidComponentType", componentTypeId.toString())
                                .withStyle(ChatFormatting.RED, ChatFormatting.ITALIC)));
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
                    list.addBig(this.headerOption(
                        Component.translatable(STRING_PREFIX + this.getCurrentComponentTypeAsString())));
                    configBuilder.apply(this, list);
                    this.currentComponentType = null;
                });
        }
    }
}
