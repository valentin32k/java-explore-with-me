package ru.practicum.mainservice.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.users.dto.InputUserDto;
import ru.practicum.mainservice.users.dto.OutputUserDto;
import ru.practicum.mainservice.users.dto.UserMapper;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public OutputUserDto createUser(@RequestBody @Valid InputUserDto userDto) {
        log.info("Creating user {}", userDto);
        return UserMapper
                .toOutputUserDto(
                service.createUser(
                        UserMapper
                                .fromInputUserDto(userDto)));
    }

    @GetMapping
    public List<OutputUserDto> getUsers(@RequestParam(name = "ids") List<Long> ids) {
        log.info("Request receive GET /admin/users ids = {}", ids);
        return UserMapper.toOutputUserDtoList(service.getUsers(ids));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeUserById(@PathVariable Long userId) {
        log.info("Request received DELETE /users with id = {}", userId);
        service.removeUserById(userId);
    }
}
