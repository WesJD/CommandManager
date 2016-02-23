package me.wes.command.test.impl;

import me.wes.command.Command;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Arrays;

public class TestCmd {

    @Command(name = "test", aliases = "t, t2")
    public void onCmd(CommandSource source, String[] args) {
        Arrays.stream(args).forEach(msg -> source.sendMessage(Text.of(msg)));
    }

}
