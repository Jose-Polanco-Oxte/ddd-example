package com.toast.user.domain;

import com.toast.user.domain.vo.UserId;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    
    Optional<User> findById(UserId id);
}
