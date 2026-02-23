/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.user.app.handler.command;

import com.toast.shared.app.CommandHandler;
import com.toast.shared.app.EventBus;
import com.toast.shared.domain.types.result.Result;
import com.toast.user.app.request.command.CreateUserCommand;
import com.toast.user.domain.User;
import com.toast.user.domain.UserRepository;
import com.toast.user.domain.vo.Age;
import com.toast.user.domain.vo.Name;
import com.toast.user.domain.vo.UserId;

public final class CreateUserCommandHandler implements CommandHandler<CreateUserCommand> {
    private final UserRepository userRepository;
    private final EventBus eventBus;
    
    public CreateUserCommandHandler(UserRepository userRepository, EventBus eventBus) {
        this.userRepository = userRepository;
        this.eventBus = eventBus;
    }
    
    @Override
    public void handle(CreateUserCommand command) {
        UserId id = UserId.fromUuid(command.userId());
        User newUser = Result.chain(Name.of(command.firstName(), command.lastName()))
                               .and(() -> Age.of(command.age()))
                               .mapOrElseThrow(
                                       (name, age) -> {
                                           User user = User.create(id, name, age);
                                           userRepository.save(user);
                                           eventBus.publish(user.pullDomainEvents());
                                           return user;
                                       },
                                       errors -> new RuntimeException("User validation failed: " + errors.toString())
                               );
        userRepository.save(newUser);
        eventBus.publish(newUser.pullDomainEvents());
    }
}
