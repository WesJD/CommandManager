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

public class SafeReflections {

    private final Reflections internal;
    private final boolean noArgs;

    public SafeReflections(String pkg) {
        this(pkg, false);
    }

    public SafeReflections(String pkg, boolean noArgs) {
        this.noArgs = noArgs;
        this.internal = new Reflections(
                new ConfigurationBuilder()
                        .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                        .setUrls(ClasspathHelper.forClassLoader(new ClassLoader[] { ClasspathHelper.contextClassLoader(), ClasspathHelper.staticClassLoader() }))
                        .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(pkg)))
        );
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        return internal.getSubTypesOf(type)
                .stream()
                .filter(clazz -> !clazz.isAnnotationPresent(ReflectIgnore.class))
                .filter(clazz -> Arrays.stream(clazz.getConstructors()).anyMatch(c -> c.getParameterTypes().length == 0 || !noArgs))
                .collect(Collectors.toSet());
    }

    public Reflections getWrapped() {
        return internal;
    }

}
