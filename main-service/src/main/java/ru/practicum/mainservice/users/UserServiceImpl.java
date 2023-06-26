package ru.practicum.mainservice.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    @Override
    public User createUser(User newUser) {
        return repository.save(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers(List<Long> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public void removeUserById(Long userId) {
        repository.deleteById(userId);
    }
}
