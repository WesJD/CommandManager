package me.wes.command.test.impl;

import me.wes.command.Command;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Arrays;

/**
 * ******************************************************************************************************
 * Copyright (C) 2016 Wesley Smith under the Copyright Law of the United States. ALl rights reserved.   *
 * All code contained in this document is sole property of Wesley Smith. Do NOT distribute, reproduce,  *
 * take snippets, or take this code under your name without exclusive permission from Wesley Smith.     *
 * Not following this statement will result in all agreements made, and possibly legal action.          *
 * If you have any questions or concerns, please contact me via Skype with the name of wesmaster123, or *
 * via email at wes@buildstatic.net. Please do note that Skype is the fastest way of communication.     *
 * Thanks for your cooperation.                                                                         *
 * ******************************************************************************************************
 */
public class TestCmd {

    @Command(name = "test", aliases = "t, t2")
    public void onCmd(CommandSource source, String[] args) {
        Arrays.stream(args).forEach(msg -> source.sendMessage(Text.of(msg)));
    }

}
