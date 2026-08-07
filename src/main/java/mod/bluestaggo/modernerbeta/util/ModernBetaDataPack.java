package mod.bluestaggo.modernerbeta.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import mod.bluestaggo.modernerbeta.registry.ModernBetaResourceKeys;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.GsonHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class ModernBetaDataPack {
    private ModernBetaDataPack() {}

    public static <T> JsonElement encode(T value, Codec<T> codec) {
        return VersionCompat.getOrThrow(codec.encodeStart(JsonOps.INSTANCE, value));
    }

    public static <T> JsonElement encode(T value, Codec<T> codec, HolderLookup.Provider registries) {
        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registries);
        return VersionCompat.getOrThrow(codec.encodeStart(ops, value));
    }

    public static ModernBetaSettingsPreset expandPreset(
        ModernBetaSettingsPreset preset,
        HolderGetter<ModernBetaSettingsPreset> presetRegistry,
        Component name,
        Component description
    ) {
        return preset
            .mapped(presetRegistry)
            .withNameAndDesc(name, description);
    }

    public static Identifier getPresetPath(Identifier presetId) {
        FileToIdConverter converter = FileToIdConverter.json(
            VersionCompat.elementsDirPath(ModernBetaResourceKeys.SETTINGS_PRESET)
        );
        return converter.idToFile(presetId);
    }

    public static JsonObject createMetadata(Component description) {
        PackMetadataSection metadata = new PackMetadataSection(
            description,
            DetectedVersion.BUILT_IN
            //? if >=1.21.6 {
            .packVersion
            //? } else {
            /*.getPackVersion
            *///? }
            (PackType.SERVER_DATA)
            //? if >=1.21.9
                .minorRange()
            //? if >=1.20.2 && <1.21.9
            //, Optional.empty()
        );
        JsonElement encoded =
            //? if >=1.20.2 {
            encode(
                metadata,
                //? if >=1.21.9 {
                PackMetadataSection.SERVER_TYPE.codec()
                //? } else {
                /*PackMetadataSection.CODEC
                *///? }
            );
            //? } else {
            /*PackMetadataSection.TYPE.toJson(metadata);
            *///? }
        JsonObject root = new JsonObject();
        root.add("pack", encoded);
        return root;
    }

    public static byte[] toBytes(JsonElement json) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))) {
            writer.setSerializeNulls(false);
            writer.setIndent("  ");
            GsonHelper.writeValue(writer, json, DataProvider.KEY_COMPARATOR);
        }
        return output.toByteArray();
    }
}
