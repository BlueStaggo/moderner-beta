package mod.bluestaggo.modernerbeta.util.function;

@FunctionalInterface
public interface BiIntegerFunction<R> {
    R apply(int i, int j);
}
