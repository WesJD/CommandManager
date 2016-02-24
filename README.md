# CommandManager
An annotation based command system for [Sponge](https://www.spongepowered.org/).

## Dependencies
This relies on [Google Reflections](https://code.google.com/archive/p/reflections/) to register classes. There is currently no alternatives to this.

## How to use
### Initialize the manager and register commands
```java
    @Listener
    public void onServerStarting(GameStartingServerEvent e) {
        CommandManager.getInstance().initialize(this);
        CommandManager.getInstance().registerClassesOf("my.package.with.command.classes");
    }
```
Call `CommandManager#initialize(Object plugin)` with your class annotated with `@Plugin` as the parameter. Then call `CommandManager#registerClassesOf(String pkg)` with your package of command classes. They don't all have to be command classes though, they are filtered. If you do not want a class registered, annotate it with `@ReflectIgnore`. You can call `#registerClassesOf` as many times as you would like to reigster different packages. Do note that [Reflections](https://code.google.com/archive/p/reflections/) will search nested packages too, so it's got you covered.

### Creating a command
```java
    @Command(name = "test", aliases = "t;t2", requiredArgs = 2, usage = "Put something;Do it again!", separator = ";")
    public void onCmd(CommandSource source) {
        source.sendMessage(Text.of("goat job m8"));
    }
```
Do be aware that you can create as many commands as you want in one class. Otherwise, `name` is the command name, and `aliases` are the aliases of the command. `aliases` and `uasages` are both split by `separator` that you can set. The default is ", " (comma space). 

`requiredArgs` is the required argument amount. If the source does not supply that amount of arguments, it will find the corresponding usage message (they should be in order). If there isn't a usage supplied, it will simply say "Not enough arguments." 

Also be aware that you do not have to use the required arguments and can handle that yourself like so.
```java
    @Command(name = "test", aliases = "t, t2")
    public void onCmd(CommandSource source, String[] args) {
        Arrays.stream(args).forEach(msg -> source.sendMessage(Text.of(msg)));
    }
```

### Importing
Either put the source into your project or shade it in with Maven. 
```xml
<dependencies>
    <dependency>
        <groupId>me.wes</groupId>
        <artifactId>CommandManager</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>buildstatic-repo</id>
        <name>BuildStatic Repository</name>
        <url>http://serv.buildstatic.net/maven-repo</url>
    </repository>
</repositories>
```

Hope you use this well.

## License
This project is registered under the [MIT License](LICENSE.txt).
