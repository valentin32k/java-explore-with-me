package ru.practicum.mainservice.compilations.dto;

import lombok.Value;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Value
public class NewCompilationDto {
    @NotBlank(message = "The field title can not be blank")
    @Size(min = 1, max = 50, message = "Title must be longer then 1 and shorter then 50 characters")
    String title;
    boolean pinned;
    @UniqueElements(message = "Events ids are not unique")
    List<Long> events;
}
