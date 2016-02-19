package me.wes.command.util.reflections;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReflectIgnore {
}
