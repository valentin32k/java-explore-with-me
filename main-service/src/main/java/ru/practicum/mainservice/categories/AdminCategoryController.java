package ru.practicum.mainservice.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.categories.dto.CategoryMapper;
import ru.practicum.mainservice.categories.dto.InputCategoryDto;
import ru.practicum.mainservice.categories.dto.OutputCategoryDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {

    private final CategoryService service;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public OutputCategoryDto createCategory(@RequestBody @Valid InputCategoryDto categoryDto) {
        log.info("Creating category {}", categoryDto);
        return CategoryMapper
                .toOutputCategotyDto(
                        service
                                .createCategory(
                                        CategoryMapper
                                                .fromInputCategoryDto(categoryDto)));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable Long catId) {
        log.info("Request received DELETE /admin/categories/{}", catId);
        service.removeCategoryById(catId);
    }

    @PatchMapping("/{catId}")
    public OutputCategoryDto updateCategory(@RequestBody @Valid InputCategoryDto categoryDto,
                                            @PathVariable Long catId) {
        log.info("Request received PATCH /admin/categories/{}", catId);
        Category category = CategoryMapper.fromInputCategoryDto(categoryDto).withId(catId);
        return CategoryMapper.toOutputCategotyDto(category);
    }
}
