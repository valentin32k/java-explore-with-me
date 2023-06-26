package ru.practicum.mainservice.users;

import java.util.List;

public interface UserService {
    User createUser(User newUser);

    List<User> getUsers(List<Long> ids);

    void removeUserById(Long userId);
}
