# CommandManager
An annotation based command system for Sponge.

## How to use
### Initialize the manager and reigster commands
```java
    @Listener
    public void onServerStarting(GameStartingServerEvent e) {
        CommandManager.getInstance().initialize(this);
        CommandManager.getInstance().registerClassesOf("me.wes.command.test.impl");
    }
```
### Creating a command
```java
    @Command(name = "test", aliases = "t;t2", requiredArgs = 2, usage = "Put something;Do it again!", separator = ";")
    public void onCmd(CommandSource source) {
        source.sendMessage(Text.of("goat job m8"));
    }
```
Do be aware that you can create as many commands as you want in one class. Otherwise, `name` is the command name, and `aliases` are the aliases of the command. `aliases` and `uasages` are both split by `separator` that you can set. The default is ", " (comma space). `requiredArgs` is the required argument amount. If the source does not supply that amount of arguments, it will find the corresponding usage message (they should be in order). If there isn't a usage supplied, it will simply say "Not enough arguments." 

Also be aware that you do not have to use the required arguments and can handle that yourself like so.
```java
    @Command(name = "test", aliases = "t, t2")
    public void onCmd(CommandSource source, String[] args) {
        Arrays.stream(args).forEach(msg -> source.sendMessage(Text.of(msg)));
    }
```

Hope you use this well.

## "Credits"
TheMrGong - SafeReflections (even though I rewrote it for him)
