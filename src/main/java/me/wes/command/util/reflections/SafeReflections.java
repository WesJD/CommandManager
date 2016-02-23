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
