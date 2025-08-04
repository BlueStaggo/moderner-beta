package mod.bluestaggo.modernerbeta.fabric.data;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.mojang.logging.LogUtils;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.datafixer.Schemas;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtIo;
//? if >=1.20.4
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.registry.Registries;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.FixedBufferInputStream;
import net.minecraft.util.Util;
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

public class ModernBetaStructureDatafixApplier implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Iterable<Path> paths;
    private final DataOutput output;

    public ModernBetaStructureDatafixApplier(DataOutput output, Collection<Path> paths) {
        this.paths = paths;
        this.output = output;
    }

    public CompletableFuture<?> run(DataWriter writer) {
        Path output = this.output.getPath();
        List<CompletableFuture<?>> list = new ArrayList<>();

        for (Path path : this.paths) {
            list.add(CompletableFuture.supplyAsync(() -> {
                try (Stream<Path> stream = Files.walk(path)) {
                    return CompletableFuture.allOf(stream
                            .filter(p -> p.toString().endsWith(".nbt"))
                            .map(p -> CompletableFuture.runAsync(() ->
                                    datafixNBTFile(writer, p, getLocation(path, p), output), Util.getIoWorkerExecutor()))
                            .toArray(CompletableFuture[]::new));
                } catch (IOException e) {
                    LOGGER.error("Failed to read input directory", e);
                    return CompletableFuture.completedFuture(null);
                }
            }, Util.getMainWorkerExecutor()).thenCompose(future -> future));
        }

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    public String getName() {
        return "Structure NBT Datafixer";
    }

    private static String getLocation(Path inputPath, Path filePath) {
        return inputPath.relativize(filePath).toString().replaceAll("\\\\", "/");
    }

    @Nullable
    public static Path datafixNBTFile(DataWriter writer, Path inputPath, String filename, Path outputPath) {
        try {
            try (InputStream is = new FixedBufferInputStream(Files.newInputStream(inputPath))) {
                NbtCompound read = NbtIo.readCompressed(is /*? if >=1.20.4 {*/, NbtSizeTracker.ofUnlimitedBytes() /*?}*/);

                StructureTemplate structureTemplate = new StructureTemplate();
                int dataVersion = NbtHelper.getDataVersion(read, 500);

                NbtCompound fixed = DataFixTypes.STRUCTURE.update(Schemas.getFixer(), read, dataVersion);
                structureTemplate.readNbt(Registries.BLOCK/*? if <1.21.2 {*//*.getReadOnlyWrapper()*//*?}*/, fixed);
                NbtCompound out = structureTemplate.writeNbt(new NbtCompound());
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
    public static void writeTo(DataWriter writer, Path path, NbtCompound content) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HashingOutputStream hashing = new HashingOutputStream(Hashing.sha1(), out);
        NbtIo.writeCompressed(content, hashing);
        byte[] bytes = out.toByteArray();
        HashCode sha1 = hashing.hash();

        try {
            writer.write(path, bytes, sha1);
        } catch (IOException e) {
            LOGGER.error("Couldn't write structure file {}", path, e);
        }
    }
}
