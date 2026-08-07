package mod.bluestaggo.modernerbeta.settings;

import com.google.common.hash.Hashing;
import com.google.gson.JsonElement;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.util.AtomicFile;
import mod.bluestaggo.modernerbeta.util.ModernBetaDataPack;
import mod.bluestaggo.modernerbeta.util.LoggingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.event.Level;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class ModernBetaSavedPresetPack {
    public static final String PACK_ID = "file/moderner_beta_saved_presets";

    private static final String PACKS_PATH = "datapacks";
    private static final String ROOT_NAME = "moderner_beta_saved_presets";
    private static final String PRESET_PATH = "default_presets/";
    private static final String PACK_METADATA = "pack.mcmeta";
    private static final Map<Identifier, ModernBetaSettingsPreset> SAVED_PRESETS = new ConcurrentHashMap<>();

    private ModernBetaSavedPresetPack() {}

    public static Path getRoot(Path configDir) {
        return configDir
            .resolve(ModernerBeta.MOD_ID)
            .resolve(PACKS_PATH)
            .resolve(ROOT_NAME);
    }

    public static boolean exists(Path configDir) {
        return Files.isDirectory(getRoot(configDir));
    }

    public static void refreshMetadata(Path configDir) {
        try {
            writeMetadata(getRoot(configDir));
        } catch (IOException exception) {
            LoggingUtil.log(Level.ERROR, "Failed to refresh saved preset pack metadata: " + exception.getMessage());
        }
    }

    public static Optional<ModernBetaSettingsPreset> getPreset(Identifier presetId) {
        return Optional.ofNullable(SAVED_PRESETS.get(presetId));
    }

    public static Identifier save(
        Path configDir,
        ModernBetaSettingsPreset preset,
        HolderGetter<ModernBetaSettingsPreset> presetRegistry,
        HolderLookup.Provider registries
    ) throws IOException {
        Path root = getRoot(configDir);
        ModernBetaSettingsPreset expanded = ModernBetaDataPack.expandPreset(
            preset,
            presetRegistry,
            customDefaultName(),
            Component.translatable("createWorld.customize.modern_beta.preset.desc.moderner_beta.custom")
        );
        JsonElement encoded = ModernBetaDataPack.encode(
            expanded,
            ModernBetaSettingsPreset.CODEC,
            registries
        );
        byte[] bytes = ModernBetaDataPack.toBytes(encoded);
        Identifier presetId = ModernerBeta.createId(PRESET_PATH + Hashing.sha256().hashBytes(bytes));
        writeMetadata(root);
        writePreset(root, presetId, bytes);
        SAVED_PRESETS.put(presetId, expanded);
        return presetId;
    }

    private static Component customDefaultName() {
        return Component.translatable("createWorld.customize.modern_beta.preset.custom")
            .append(" ")
            .append(Component.translatable("createWorld.customize.modern_beta.preset.type.default"))
            .withStyle(ChatFormatting.YELLOW);
    }

    private static void writeMetadata(Path root) throws IOException {
        byte[] bytes = ModernBetaDataPack.toBytes(ModernBetaDataPack.createMetadata(
            Component.translatable("pack.moderner_beta.saved_presets")
        ));
        AtomicFile.write(root.resolve(PACK_METADATA), bytes);
    }

    private static void writePreset(Path root, Identifier presetId, byte[] bytes) throws IOException {
        Identifier fileId = ModernBetaDataPack.getPresetPath(presetId);
        Path path = root
            .resolve("data")
            .resolve(fileId.getNamespace())
            .resolve(fileId.getPath());

        try {
            AtomicFile.create(path, bytes);
        } catch (FileAlreadyExistsException exception) {
            if (!Arrays.equals(bytes, Files.readAllBytes(path))) {
                throw new IOException("Saved preset hash collision at " + path, exception);
            }
        }
    }
}
