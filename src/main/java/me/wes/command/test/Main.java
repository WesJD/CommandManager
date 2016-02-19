package me.wes.command.test;

import me.wes.command.CommandManager;
import me.wes.command.test.util.Logging;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

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
@Plugin(id = Main.PLUGIN_ID, name = Main.PLUGIN_NAME, version = Main.PLUGIN_VERSION)
public class Main {

    public static final String PLUGIN_ID = "spongetest";
    public static final String PLUGIN_NAME = "Sponge Testing";
    public static final String PLUGIN_VERSION = "None";

    private static Main instance;

    @Listener
    public void onPluginConstruct(GameConstructionEvent e) {
        Logging.logDebug("Construction...");
        instance = this;
    }

    @Listener
    public void onServerStarting(GameStartingServerEvent e) {
        Logging.logDebug("Starting Server..");
        CommandManager.getInstance().initialize(this);
        CommandManager.getInstance().registerClassesOf("me.wes.command.test.impl");
    }

    @Listener
    public void onServerStopping(GameStoppingServerEvent e) {
        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }

}
