package ru.practicum.mainservice.categories.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.categories.Category;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {
    public Category fromInputCategoryDto(InputCategoryDto inputCategoryDto) {
        return Category.builder()
                .name(
                        inputCategoryDto
                                .getName())
                .build();
    }

    public OutputCategoryDto toOutputCategotyDto(Category category) {
        return new OutputCategoryDto(category.getId(), category.getName());
    }

    public List<OutputCategoryDto> toOutputCategoryDtoList(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toOutputCategotyDto)
                .collect(Collectors.toList());
    }
}
