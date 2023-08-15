package cc.kermanispretty.config.common;

import java.util.EnumSet;
import java.util.stream.Collectors;

public enum ConfigOptionEnum {
    SEARCH_INTERFACES(true),
    SEARCH_SUPER_CLASSES(true),
    SEARCH_DECLARED_CLASSES(true),
    SEARCH_CURRENT_CLASS(true),
    SEARCH_RECURSIVELY(false),
    CHECK_DEFAULT_FOR_VALIDATION(true),
    CHECK_DEFAULT_TRANSFORMED_FOR_VALIDATION(true),
    CHECK_CONFIG_FOR_VALIDATION(true),
    CHECK_CONFIG_TRANSFORMED_FOR_VALIDATION(true),
    SUPPRESS_VALIDATION_ERRORS(false),
    CHECK_DEFAULT_NULL(false);

    private final boolean defaultValue;

    ConfigOptionEnum(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public static EnumSet<ConfigOptionEnum> getDefaultOptions() {
        return EnumSet.allOf(ConfigOptionEnum.class).stream()
                .filter(ConfigOptionEnum::getDefaultValue)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(ConfigOptionEnum.class)));
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }
}
