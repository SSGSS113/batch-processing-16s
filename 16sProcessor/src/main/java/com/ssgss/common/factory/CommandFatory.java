package com.ssgss.common.factory;

import com.ssgss.common.command.Command;

public interface CommandFatory <T, R> {
    Command getCommand(Class<T> clazz, R r);
}
