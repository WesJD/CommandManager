package me.wes.command.util.reflections;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~
 * Copyright TheMrGong (c) 2015. All Rights Reserved.
 * Any code contained within this java file is the sole property of TheMrGong.
 * You may not distribute, take snippets, reproduce, or claim any code
 * as your own. Doing so will result in void with any agreements with you.
 * Stop flopping around.
 * ---------------------
 * File Info
 * File created: 1/7/2016
 * ~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~~=~
 */
public class SafeReflections {

    private Reflections internal;
    private boolean noArgs;
    private String pkg;

    public SafeReflections(String pkg) {
        this(pkg, false);
    }

    public SafeReflections(String pkg, boolean noArgs) {
        this.pkg = pkg;
        this.noArgs = noArgs;
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        updateInternal(!type.isAssignableFrom(Object.class));
        return internal.getSubTypesOf(type)
                .stream()
                .filter(clazz -> !clazz.isAnnotationPresent(ReflectIgnore.class))
                .filter(clazz -> Arrays.stream(clazz.getConstructors()).anyMatch(c -> c.getParameterTypes().length == 0 || !noArgs))
                .collect(Collectors.toSet());
    }

    private void updateInternal(boolean stc) {
        internal = new Reflections(
                new ConfigurationBuilder()
                        .setScanners(new SubTypesScanner(stc), new ResourcesScanner())
                        .setUrls(ClasspathHelper.forClassLoader(new ClassLoader[] { ClasspathHelper.contextClassLoader(), ClasspathHelper.staticClassLoader() }))
                        .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(pkg)))
        );
    }

    public Reflections getWrapped() {
        return internal;
    }

}
