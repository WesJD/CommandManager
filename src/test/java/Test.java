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

import me.wes.command.Command;
import me.wes.command.CommandManager;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.util.Tristate;

import java.beans.MethodDescriptor;
import java.lang.reflect.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Assert;
import testCmds.TestCmd;

public class Test {

    @Rule
    public final SystemOutRule rule = new SystemOutRule().enableLog();

    @org.junit.Test
    public void test() {
        try {
            //I literally hacked my own code......

            final Class<?> commandManagerClazz = Class.forName("me.wes.command.CommandManager");
            final Constructor<?> commandManagerConstructor = commandManagerClazz.getDeclaredConstructor();
            commandManagerConstructor.setAccessible(true);
            final CommandManager commandManagerObject = (CommandManager) commandManagerConstructor.newInstance();

            final Field initializedField = commandManagerClazz.getDeclaredField("initialized");
            initializedField.setAccessible(true);
            initializedField.set(commandManagerObject, true);

            final Field commandsField = commandManagerClazz.getDeclaredField("commands");
            commandsField.setAccessible(true);
            final Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(commandsField, commandsField.getModifiers() & ~Modifier.FINAL);
            final Set localCommands = (Set) commandsField.get(commandManagerObject);

            final Class<?> commandDataClazz = Class.forName("me.wes.command.CommandManager$CommandData");
            final Constructor<?> commandDataConstrcutor = commandDataClazz.getDeclaredConstructor(commandManagerClazz, Command.class, Method.class, Object.class);
            commandDataConstrcutor.setAccessible(true);
            final TestCmd testCmd = new TestCmd();
            final Method testCmdMethod = testCmd.getClass().getMethods()[0];
            final Object commandDataObject = commandDataConstrcutor.newInstance(commandManagerObject, testCmdMethod.getAnnotation(Command.class), testCmdMethod, testCmd);

            localCommands.add(commandDataObject);

            final Class<?> listenUpClazz = commandManagerClazz.getDeclaredClasses()[0];
            final Constructor<?> listenUpConstrcutor = listenUpClazz.getDeclaredConstructor(commandManagerClazz);
            listenUpConstrcutor.setAccessible(true);
            final Object listenUpObject = listenUpConstrcutor.newInstance(commandManagerObject);
            final Method onCommandSendMethod = listenUpClazz.getDeclaredMethod("onCommandSend", SendCommandEvent.class);

            final SendCommandEvent spoofedSendCommandEvent = new SpoofedSendCommandEvent();
            onCommandSendMethod.invoke(listenUpObject, spoofedSendCommandEvent);
            System.out.println("event cancelled (should be): " + spoofedSendCommandEvent.isCancelled());
            Assert.assertTrue("Final check", spoofedSendCommandEvent.isCancelled());
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public class SpoofedSendCommandEvent implements SendCommandEvent {

        public boolean cancelled = false;

        public boolean getCancelled() {
            return cancelled;
        }

        @Override
        public String getCommand() {
            return "test";
        }

        @Override
        public String getArguments() {
            return "i love you";
        }

        @Override
        public CommandResult getResult() {
            return null;
        }

        @Override
        public void setResult(CommandResult commandResult) {

        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean b) {
            Assert.assertTrue("setCancelled check", b);
            this.cancelled = b;
        }

        @Override
        public Cause getCause() {
            return Cause.of(NamedCause.source(new SpoofedCommandSource()));
        }

    }

    private class SpoofedCommandSource implements CommandSource {

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getIdentifier() {
            return null;
        }

        @Override
        public Set<Context> getActiveContexts() {
            return null;
        }

        @Override
        public void sendMessage(Text text) {

        }

        @Override
        public MessageChannel getMessageChannel() {
            return null;
        }

        @Override
        public void setMessageChannel(MessageChannel messageChannel) {

        }

        @Override
        public Optional<CommandSource> getCommandSource() {
            return null;
        }

        @Override
        public SubjectCollection getContainingCollection() {
            return null;
        }

        @Override
        public SubjectData getSubjectData() {
            return null;
        }

        @Override
        public SubjectData getTransientSubjectData() {
            return null;
        }

        @Override
        public boolean hasPermission(Set<Context> set, String s) {
            return false;
        }

        @Override
        public boolean hasPermission(String s) {
            return false;
        }

        @Override
        public Tristate getPermissionValue(Set<Context> set, String s) {
            return null;
        }

        @Override
        public boolean isChildOf(Subject subject) {
            return false;
        }

        @Override
        public boolean isChildOf(Set<Context> set, Subject subject) {
            return false;
        }

        @Override
        public List<Subject> getParents() {
            return null;
        }

        @Override
        public List<Subject> getParents(Set<Context> set) {
            return null;
        }

    }

}
