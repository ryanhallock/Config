package cc.kermanispretty.config.common;

public class ConfigOptions {
    public final static ConfigOptions DEFAULT = new ConfigOptions(); //stored so a new object is not stored in memory everytime.

    public final boolean searchInterfaces;
    public final boolean searchSuperClasses;
    public final boolean searchDeclaredClasses;
    public final boolean searchCurrentClass;
    public final boolean searchReversibly;

    public final boolean checkDefaultForValidation;
    public final boolean suppressValidationErrors;

    private ConfigOptions(){
        this(
                true,
                true,
                true,
                true,
                false,
                true,
                false
        );
    }

    public ConfigOptions(boolean searchInterfaces,
                         boolean searchSuperClasses,
                         boolean searchDeclaredClasses,
                         boolean searchCurrentClass,
                         boolean searchReversibly,
                         // split for readability
                         boolean checkDefaultForValidation,
                         boolean suppressValidationErrors){
        this.searchInterfaces = searchInterfaces;
        this.searchSuperClasses = searchSuperClasses;
        this.searchDeclaredClasses = searchDeclaredClasses;
        this.searchCurrentClass = searchCurrentClass;
        this.searchReversibly = searchReversibly;

        this.checkDefaultForValidation = checkDefaultForValidation;
        this.suppressValidationErrors = suppressValidationErrors;
    }
}
