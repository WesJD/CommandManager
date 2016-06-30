/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Wesley Smith
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


package net.wesjd.commandmanager;

import org.reflections.Reflections;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CommandManager {

    private static final CommandManager instance = new CommandManager();
    private final Set<CommandData> commands = new HashSet<>();
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
        new Reflections(pkg).getSubTypesOf(Object.class)
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

        private CommandData(Command command, Method method, Object object) {
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
                final Optional<CommandData> correspondingCommand = getCommand(e.getCommand());
                if(correspondingCommand.isPresent()) {
                    e.setCancelled(true);
                    final Optional<CommandSource> sourceOptional = e.getCause().first(CommandSource.class);
                    final CommandData data = correspondingCommand.get();
                    final Method method = data.getMethod();
                    if(sourceOptional.isPresent() && method.getParameterCount() > 0) {
                        final CommandSource source = sourceOptional.get();
                        final String[] arguments = (e.getArguments().length() > 0 ? e.getArguments().split(" ") : new String[0]);
                        final String[] usages = (data.getCommand().usage().length() > 0 ? data.getCommand().usage().split(data.getCommand().separator()) : new String[0]);
                        if(arguments.length >= data.getCommand().requiredArgs()) {
                            final Object[] suppliedParams = new Object[method.getParameterCount()];
                            final int[] cur = new int[1];
                            Arrays.stream(method.getParameters()).forEach(param -> {
                                Object supply = null;
                                if(param.getType().equals(CommandSource.class)) supply = source;
                                else if(param.getType().equals(String[].class)) supply = arguments;

                                suppliedParams[cur[0]++] = supply;
                            });
                            method.invoke(data.getInstantiatedClass(), suppliedParams);
                        } else {
                            final String message = (usages.length > arguments.length ? usages[arguments.length] : "Not enough arguments.");
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
