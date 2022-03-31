package cc.kermanispretty.config.common;

public class ConfigOptions {
    public final static ConfigOptions DEFAULT = new ConfigOptions(); //stored so a new object is not stored in memory everytime.

    public final boolean searchInterfaces;
    public final boolean searchSuperClasses;
    public final boolean searchDeclaredClasses;
    public final boolean searchCurrentClass;
    public final boolean searchReversibly; //Expermental: TODO - remake

    private ConfigOptions(){
        this(true, true, true, true, false);
    }

    public ConfigOptions(boolean searchInterfaces, boolean searchSuperClasses, boolean searchDeclaredClasses, boolean searchCurrentClass, boolean searchReversibly){
        this.searchInterfaces = searchInterfaces;
        this.searchSuperClasses = searchSuperClasses;
        this.searchDeclaredClasses = searchDeclaredClasses;
        this.searchCurrentClass = searchCurrentClass;
        this.searchReversibly = searchReversibly;
    }
}
