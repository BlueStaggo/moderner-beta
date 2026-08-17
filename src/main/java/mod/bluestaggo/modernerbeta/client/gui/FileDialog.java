package mod.bluestaggo.modernerbeta.client.gui;

//? if >=26.3
//import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.Window;
//? if <26.3
import net.minecraft.util.Util;
import org.lwjgl.system.MemoryStack;
//? if >=26.3 {
/*import org.lwjgl.sdl.SDLDialog;
import org.lwjgl.sdl.SDLError;
import org.lwjgl.sdl.SDL_DialogFileFilter;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Pointer;
*///? } else {
import org.lwjgl.PointerBuffer;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
//? }

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
//? if <26.3
import java.util.concurrent.CompletableFuture;

public final class FileDialog {
    public static void openFileDialog(Window window, String title, Path folder, List<Filter> filters,
                                      boolean multiChoice, ResultCallback<List<String>> callback) {
        //~ if >=26.3 '_tinyFD' -> '_SDL'
        openFileDialog_tinyFD(window, title, folder, filters, multiChoice, callback);
    }

    public static void saveFileDialog(Window window, String title, Path folder,
                                      List<Filter> filters, ResultCallback<String> callback) {
        //~ if >=26.3 '_tinyFD' -> '_SDL'
        saveFileDialog_tinyFD(window, title, folder, filters, callback);
    }

    //? if >=26.3 {
    /*private static void openFileDialog_SDL(Window window, String title, Path folder, List<Filter> filters,
                                           boolean multiChoice, ResultCallback<List<String>> callback) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            SDL_DialogFileFilter.Buffer pFilters = SDL_DialogFileFilter.calloc(filters.size(), stack);
            for (int i = 0; i < filters.size(); i++) {
                Filter filter = filters.get(i);
                //noinspection resource
                pFilters.get(i).name(stack.UTF8(filter.description())).pattern(stack.UTF8(filter.pattern()));
            }

            SDLDialog.SDL_ShowOpenFileDialog(
                (userdata, filelist, filter) ->
                    openDialogFileCallback(userdata, filelist, filter != -1 ? filters.get(filter) : null, callback),
                0L,
                window.handle(),
                pFilters,
                folder.toString(),
                multiChoice
            );
        }
    }

    private static void openDialogFileCallback(long userData, long fileList, Filter filter, ResultCallback<List<String>> callback) {
        if (fileList == 0) {
            throw new RuntimeException("Failed to get files: " + SDLError.SDL_GetError());
        }

        ImmutableList.Builder<String> builder = ImmutableList.builder();

        long p = fileList;
        long fp = MemoryUtil.memGetAddress(p);
        while (fp != 0) {
            builder.add(MemoryUtil.memUTF8(fp));

            p += Pointer.POINTER_SIZE;
            fp = MemoryUtil.memGetAddress(p);
        }

        ImmutableList<String> paths = builder.build();
        if (!paths.isEmpty()) {
            callback.onCallback(true, paths);
            return;
        }

        callback.onCallback(false, null);
    }

    private static void saveFileDialog_SDL(Window window, String title, Path folder, List<Filter> filters, ResultCallback<String> callback) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            SDL_DialogFileFilter.Buffer pFilters = SDL_DialogFileFilter.calloc(filters.size(), stack);
            for (int i = 0; i < filters.size(); i++) {
                Filter filter = filters.get(i);
                //noinspection resource
                pFilters.get(i).name(stack.UTF8(filter.description())).pattern(stack.UTF8(filter.pattern()));
            }

            SDLDialog.SDL_ShowSaveFileDialog(
                (userdata, filelist, filter) ->
                    saveDialogFileCallback(userdata, filelist, filter != -1 ? filters.get(filter) : null, callback),
                0L,
                window.handle(),
                pFilters,
                folder.toString()
            );
        }
    }

    private static void saveDialogFileCallback(long userData, long fileList, Filter filter, ResultCallback<String> callback) {
        if (fileList == 0) {
            throw new RuntimeException("Failed to get files: " + SDLError.SDL_GetError());
        }

        long fp = MemoryUtil.memGetAddress(fileList);
        if (fp == 0) {
            callback.onCallback(false, null);
            return;
        }

        String append = filter.suffix().isPresent() ? filter.suffix().get() : "";
        callback.onCallback(true, MemoryUtil.memUTF8(fp) + append);
    }
    *///? }

    //? if <26.3 {
    private static void openFileDialog_tinyFD(Window window, String title, Path folder, List<Filter> filters,
                                              boolean multiChoice, ResultCallback<List<String>> callback) {
        CompletableFuture.runAsync(() -> {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                PointerBuffer pointers = stack.mallocPointer(filters.size());
                for (Filter filter : filters) {
                    pointers.put(stack.UTF8("*." + filter.pattern()));
                    pointers.flip();
                }

                String res = TinyFileDialogs.tinyfd_openFileDialog(
                    title,
                    folder.toString(),
                    pointers,
                    null,
                    multiChoice
                );

                callback.onCallback(res != null, res != null ? List.of(res.split("\\|")) : null);
            }
        }, Util.backgroundExecutor()).thenAccept(ignored -> {});
    }

    private static void saveFileDialog_tinyFD(Window window, String title, Path folder,
                                              List<Filter> filters, ResultCallback<String> callback) {
        CompletableFuture.runAsync(() -> {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                PointerBuffer pointers = stack.mallocPointer(filters.size());
                for (Filter filter : filters) {
                    pointers.put(stack.UTF8("*." + filter.pattern()));
                    pointers.flip();
                }

                String res = TinyFileDialogs.tinyfd_saveFileDialog(
                    title,
                    folder.toString(),
                    pointers,
                    null
                );

                callback.onCallback(res != null, res);
            }
        }, Util.backgroundExecutor()).thenAccept(ignored -> {});
    }
    //? }

    public record Filter(String pattern, String description, Optional<String> suffix) {
        public Filter(String pattern, String description) {
            this(pattern, description, Optional.of(pattern));
        }
    }

    @FunctionalInterface
    public interface ResultCallback<R> {
        void onCallback(boolean hasResult, R result);
    }
}
