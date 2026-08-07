package mod.bluestaggo.modernerbeta.util;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class AtomicFile {
    private AtomicFile() {}

    public static void write(Path path, byte[] bytes) throws IOException {
        write(path, bytes, true);
    }

    public static void create(Path path, byte[] bytes) throws IOException {
        write(path, bytes, false);
    }

    private static void write(Path path, byte[] bytes, boolean replace) throws IOException {
        Path target = path.toAbsolutePath();
        Path parent = target.getParent();
        if (!replace && Files.exists(target)) {
            throw new FileAlreadyExistsException(target.toString());
        }

        Files.createDirectories(parent);
        Path temp = Files.createTempFile(parent, target.getFileName().toString(), ".tmp");

        try {
            Files.write(temp, bytes);
            move(temp, target, replace);
        } catch (IOException exception) {
            try {
                Files.deleteIfExists(temp);
            } catch (IOException deleteException) {
                exception.addSuppressed(deleteException);
            }
            throw exception;
        }
    }

    private static void move(Path temp, Path path, boolean replace) throws IOException {
        CopyOption[] atomicOptions = replace ?
            new CopyOption[] { StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING } :
            new CopyOption[] { StandardCopyOption.ATOMIC_MOVE };

        try {
            Files.move(temp, path, atomicOptions);
        } catch (AtomicMoveNotSupportedException exception) {
            if (replace) {
                Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.move(temp, path);
            }
        }
    }
}
