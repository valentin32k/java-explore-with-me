package ru.practicum.mainservice.categories.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.mainservice.categories.Category;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {
    public Category fromNewCategoryDto(NewCategoryDto newCategoryDto) {
        return new Category(0L, newCategoryDto.getName());
    }

    public CategoryDto toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDto(category.getId(), category.getName());
    }

    public List<CategoryDto> toCategoryDtoList(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}
