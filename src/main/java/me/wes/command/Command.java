package me.wes.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String name();

    String aliases() default "";

    int requiredArgs() default 0;

    String usage() default "";

    String separator() default  ", ";

}
