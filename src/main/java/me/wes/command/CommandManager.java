package me.wes.command;

import me.wes.command.util.reflections.SafeReflections;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
public class CommandManager {

    private static CommandManager instance = new CommandManager();
    private Set<CommandData> commands = new HashSet<>();
    private boolean initialized = false;

    private CommandManager() {}

    public Optional<CommandData> getCommand(String supplied) {
        CommandData ret = null;
        for(CommandData data : commands) {
            Command command = data.getCommand();
            if(supplied.equalsIgnoreCase(command.name()) || getAliases(data).stream().anyMatch(supplied::equalsIgnoreCase)) {
                if(ret != null) throw new RuntimeException("Found two commands with the name or alias... what?");
                ret = data;
            }
        }
        return Optional.ofNullable(ret);
    }

    public Collection<String> getAliases(CommandData data) {
        return Arrays.asList(data.getCommand().aliases().split(data.getCommand().separator()));
    }

    public void registerClassesOf(String pkg) {
        if(!initialized) throw new RuntimeException("The CommandManager must be initialized before use.");
        new SafeReflections(pkg).getSubTypesOf(Object.class)
                .stream()
                .filter(clazz -> Arrays.stream(clazz.getMethods()).anyMatch(m -> m.isAnnotationPresent(Command.class)))
                .forEach(clazz -> {
                    try {
                        Object instance = clazz.newInstance();
                        Arrays.stream(clazz.getMethods()).forEach(m -> {
                            if(m.isAnnotationPresent(Command.class)) commands.add(new CommandData(m.getAnnotation(Command.class), m, instance));
                        });
                    } catch (InstantiationException | IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                });
    }

    public void initialize(Object plugin) {
        if(initialized) throw new RuntimeException("The CommandManager has already been initialized.");
        Sponge.getEventManager().registerListeners(plugin, new ListenUp());
        initialized = true;
    }

    public static CommandManager getInstance() {
        return instance;
    }

    public class CommandData {

        private Command command;
        private Method method;
        private Object object;

        public CommandData(Command command, Method method, Object object) {
            this.command = command;
            this.method = method;
            this.object = object;

            method.setAccessible(true);
        }

        public Command getCommand() {
            return command;
        }

        public Method getMethod() {
            return method;
        }

        public Object getInstantiatedClass() {
            return object;
        }

    }

    public class ListenUp {

        @Listener
        public void onCommandSend(SendCommandEvent e) {
            try {
                Optional<CommandData> correspondingCommand = getCommand(e.getCommand());
                if(correspondingCommand.isPresent()) {
                    e.setCancelled(true);
                    Optional<CommandSource> sourceOptional = e.getCause().first(CommandSource.class);
                    CommandData data = correspondingCommand.get();
                    Method method = data.getMethod();
                    if(sourceOptional.isPresent() && method.getParameterCount() > 0) {
                        CommandSource source = sourceOptional.get();
                        String[] arguments = (e.getArguments().length() > 0 ? e.getArguments().split(" ") : new String[0]);
                        String[] usages = (data.getCommand().usage().length() > 0 ? data.getCommand().usage().split(data.getCommand().separator()) : new String[0]);
                        if(arguments.length >= data.getCommand().requiredArgs()) {
                            Object[] suppliedParams = new Object[method.getParameterCount()];
                            Arrays.stream(method.getParameters()).forEach(param -> {
                                Object supply = null;
                                if(param.getType().equals(CommandSource.class)) supply = source;
                                else if(param.getType().equals(String[].class)) supply = arguments;

                                int cur = 0;
                                for(Object obj : suppliedParams) if(obj != null) cur++; else break;
                                suppliedParams[cur] = supply;
                            });
                            method.invoke(data.getInstantiatedClass(), suppliedParams);
                        } else {
                            String message = (usages.length > arguments.length ? usages[arguments.length] : "Not enough arguments.");
                            source.sendMessage(Text.builder(message).color(TextColors.RED).build());
                        }
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }

    }

}
