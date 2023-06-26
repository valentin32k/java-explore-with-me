package ru.practicum.mainservice.users;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public List<User> getUsers(List<Long> ids, Integer from, Integer size) {
        if (ids.size() == 0) {
            return repository
                    .findAll(PageRequest.of(from / size, size))
                    .getContent();
        }
        return repository
                .findAllByIdIn(ids, PageRequest.of(from / size, size))
                .getContent();
    }

    @Override
    public void removeUserById(Long userId) {
        repository.deleteById(userId);
    }
}
