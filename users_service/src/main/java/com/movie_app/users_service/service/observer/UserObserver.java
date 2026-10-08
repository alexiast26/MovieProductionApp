package com.movie_app.users_service.service.observer;

import com.movie_app.users_service.domain.User;

public interface UserObserver {
    void onUserUpdated(User user);
}
