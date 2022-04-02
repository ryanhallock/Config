# Config

Annotationed Configuration system. Written in Java

## Supports

* Bukkit/Bukkit Based 1.7.10 - 1.18.2

## Example for Bukkit

```java
//Import all classes.
import cc.kermanispretty.config.*;

@Header("Test Header") // Multi-line acceptable
@Footer("Test Footer") //  by passing an array.
@Configurable("configTest") // puts a prefix on all fields.
@Comment("This is a test config for configtest")
public class ConfigTest extends JavaPlugin {
    @Configurable("test")
    @InlineComment("This is inline test")
    @Comment({
            "This is comment test",
            "This is second comment test"
    })
    //We can call this field for our config value after load
    private final String test = "weTestingstuff"; // default value "weTestingStuff"
    // Sections are divided up to. 
    // (Interface Class -> Super Class -> Declaring Class -> Class -> Field)

    private BukkitConfig config; // our config instance		

    @Override  // Enable the plugin
    public void onEnable() {
        config = new BukkitConfigBuilder(this, "config.yml")
                .registerAndLoad(this);  // register and load the objects
    }

    @Override
    public void onDisable() {
        config.unload(); // shutdown (not required).
    }
}
```

# Packages

> Download the source and compile with gradle using publishShadowPublicationToMavenLocal

* `common` for writing your own handler
* `bukkit` under 1.18.1+
* `bukkit-compatibility` for 1.7.10 - 1.18.2

# Jitpack 
You can use JitPack as your repository. [https://jitpack.io/#KermanIsPretty/Config/](https://jitpack.io/#KermanIsPretty/Config/)

# TODO

- [x] Upload to GitHub
- [x] Working
- [ ] Clean up Code (partially ~done)
- [ ] Bungee & Velocity
- [x] Config Validation
- [ ] Add support for own sterilizers
- [ ] Add better README.md detailing how to do more things.

