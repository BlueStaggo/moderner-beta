package mod.bluestaggo.modernerbeta.settings.component.validation;

@FunctionalInterface
public interface ComponentValidator<T> {
    ValidationResult<T> validate(T value);
}
