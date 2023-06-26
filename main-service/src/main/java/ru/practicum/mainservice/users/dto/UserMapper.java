package ru.practicum.mainservice.users.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.users.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public User fromInputUserDto(InputUserDto inputUserDto) {
        return new User(0,
                inputUserDto.getName(),
                inputUserDto.getEmail());
    }

    public OutputUserDto toOutputUserDto(User user) {
        if (user == null) {
            return null;
        }
        return new OutputUserDto(user.getId(),
                user.getName(),
                user.getEmail());
    }

    public List<OutputUserDto> toOutputUserDtoList(List<User> users) {
        if(users == null) {
            return null;
        }
        return users.stream()
                .map(UserMapper::toOutputUserDto)
                .collect(Collectors.toList());
    }

}
