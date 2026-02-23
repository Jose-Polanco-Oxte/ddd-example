package com.toast.shared.app;

public interface CommandHandler<C extends Command> {
    void handle(C command);
}
