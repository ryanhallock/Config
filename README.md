# Config

Annotated Configuration system. Written in Java.

## Supports

* Bukkit/Bukkit Based 1.7.10 - 1.20.1
* Java 1.8 - 17

## Example for Bukkit

```java
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
    private String test = "weTestingstuff"; // default value "weTestingStuff"

    // If you added a @Configurable annotation to the parent class it would 
    // -search all parents in this reversed and sort them to.
    // (Interface Class -> Super Class -> Declaring Class -> Class -> Field)
    private BukkitConfig config; // our config instance		

    @Override  // Enable the plugin
    public void onEnable() {
        config = new BukkitConfigBuilder(this, "config.yml")
                .register(this) // class or instance
                .buildAndLoad();  // build and load the config
    }
}
```

### Produces

```yaml
# Test Header

# This is a test config for configtest
configTest:
  # This is comment test
  # This is second comment test
  test: weTestingstuff # This is inline test
# Test Footer
```

# Example transformer

Transformer that converts "null" to Java null

```java

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Transform(value = StringNullTransformer.class, type = String.class)
public @interface NullTransform {
}

public class StringNullTransformer implements Transformer<String, Object, TestAnnotation> {
    @Override
    public Object transform(Field field, String string, TestAnnotation testAnnotation, Config config) throws InvalidTransformExpectation {
        if (string != null && string.equals("null")) {
            return null;
        }

        return string;
    }
}
```

Make sure to register it

```java
 BukkitConfigBuilder.registerTransformer(TestAnnotation.class)
```

# Packages

> Download the source and compile with gradle using publishShadowPublicationToMavenLocal

* `common` for writing your own handler
* `bukkit` under 1.18.1+
* `bukkit-compatibility` for 1.7.10 - 1.18.1+

# Jitpack 
You can use JitPack as the repository for gradle/maven. [https://jitpack.io/#KermanIsPretty/Config/](https://jitpack.io/#KermanIsPretty/Config/)

# Supported Formats

- [x] YAML (SnakeYAML)

# Want to contribute?

Feel free to open a pull request or issue. I will try to respond as soon as possible.

# TODO

- [x] Upload to GitHub
- [x] Working
- [x] Code cleanup/v2
- [ ] Bungee & Velocity
- [ ] JSON (SoonTM)
- [x] Config transformers
- [x] Config Validation
- [x] Add support for own sterilizers
- [ ] Add better README.md detailing how to do more things.

