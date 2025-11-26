package mod.bluestaggo.modernerbeta.fabric.data.provider;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
//? if >=1.20.4
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.FastBufferedInputStream;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ModernBetaStructureDatafixProvider implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Iterable<Path> paths;
    private final PackOutput output;

    public ModernBetaStructureDatafixProvider(PackOutput output, Collection<Path> paths) {
        this.paths = paths;
        this.output = output;
    }

    public @NotNull CompletableFuture<?> run(CachedOutput writer) {
        Path output = this.output.getOutputFolder();
        List<CompletableFuture<?>> list = new ArrayList<>();

        for (Path path : this.paths) {
            list.add(CompletableFuture.supplyAsync(() -> {
                try (Stream<Path> stream = Files.walk(path)) {
                    return CompletableFuture.allOf(stream
                            .filter(p -> p.toString().endsWith(".nbt"))
                            .map(p -> CompletableFuture.runAsync(() ->
                                    datafixNBTFile(writer, p, getLocation(path, p), output), Util.ioPool()))
                            .toArray(CompletableFuture[]::new));
                } catch (IOException e) {
                    LOGGER.error("Failed to read input directory", e);
                    return CompletableFuture.completedFuture(null);
                }
            }, Util.backgroundExecutor()).thenCompose(future -> future));
        }

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    public @NotNull String getName() {
        return "Structure NBT Datafixer";
    }

    private static String getLocation(Path inputPath, Path filePath) {
        return inputPath.relativize(filePath).toString().replaceAll("\\\\", "/");
    }

    @Nullable
    public static Path datafixNBTFile(CachedOutput writer, Path inputPath, String filename, Path outputPath) {
        try {
            try (InputStream is = new FastBufferedInputStream(Files.newInputStream(inputPath))) {
                CompoundTag read = NbtIo.readCompressed(is /*? if >=1.20.4 {*/, NbtAccounter.unlimitedHeap() /*?}*/);

                StructureTemplate structureTemplate = new StructureTemplate();
                int dataVersion = NbtUtils.getDataVersion(read, 500);

                CompoundTag fixed = DataFixTypes.STRUCTURE.updateToCurrentVersion(DataFixers.getDataFixer(), read, dataVersion);
                structureTemplate.load(BuiltInRegistries.BLOCK/*? if <1.21.2 {*//*.asLookup()*//*?}*/, fixed);
                CompoundTag out = structureTemplate.save(new CompoundTag());
                Path path = outputPath.resolve(filename);

                writeTo(writer, path, out);
                LOGGER.info("Datafixed structure file {}", filename);
                return path;
            }
        } catch (IOException iOException) {
            LOGGER.error("Couldn't datafix file {} located at {}", filename, inputPath, iOException);
            return null;
        }
    }

    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    public static void writeTo(CachedOutput writer, Path path, CompoundTag content) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HashingOutputStream hashing = new HashingOutputStream(Hashing.sha1(), out);
        NbtIo.writeCompressed(content, hashing);
        byte[] bytes = out.toByteArray();
        HashCode sha1 = hashing.hash();

        try {
            writer.writeIfNeeded(path, bytes, sha1);
        } catch (IOException e) {
            LOGGER.error("Couldn't write structure file {}", path, e);
        }
    }
}
