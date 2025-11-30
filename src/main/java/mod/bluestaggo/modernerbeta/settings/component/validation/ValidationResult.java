package mod.bluestaggo.modernerbeta.settings.component.validation;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ValidationResult<T> {
    Type getResultType();

    @NotNull Optional<T> result();

    default T resultOrThrow() {
        return result().orElseThrow();
    }

    boolean hasMessage();
    Component message();

    enum Type {
        VALID,
        PARTIAL,
        INVALID;
    }

    record Valid<T>(T resultValue) implements ValidationResult<T> {
        @Override
        public Type getResultType() {
            return Type.VALID;
        }

        @Override
        public @NotNull Optional<T> result() {
            return Optional.ofNullable(resultValue);
        }

        @Override
        public boolean hasMessage() {
            return false;
        }

        @Override
        public Component message() {
            return null;
        }
    }

    record Invalid<T>(Component message) implements ValidationResult<T> {
        @Override
        public Type getResultType() {
            return Type.INVALID;
        }

        @Override
        public  @NotNull Optional<T> result() {
            return Optional.empty();
        }

        @Override
        public boolean hasMessage() {
            return message != null;
        }
    }

    record Partial<T>(Component message, T resultValue) implements ValidationResult<T> {
        @Override
        public Type getResultType() {
            return Type.PARTIAL;
        }

        @Override
        public @NotNull Optional<T> result() {
            return Optional.ofNullable(resultValue);
        }

        @Override
        public boolean hasMessage() {
            return false;
        }
    }
}
