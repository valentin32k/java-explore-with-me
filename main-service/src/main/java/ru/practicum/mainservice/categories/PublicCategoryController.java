package ru.practicum.mainservice.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.categories.dto.CategoryMapper;
import ru.practicum.mainservice.categories.dto.OutputCategoryDto;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {
    private final CategoryService service;

    @GetMapping
    public List<OutputCategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("Request received GET /categories from = {}, size = {}", from, size);
        return CategoryMapper.toOutputCategoryDtoList(service.getCategories(from, size));
    }

    @GetMapping("/{categoryId}")
    public OutputCategoryDto getCategoryById(@PathVariable long categoryId) {
        log.info("Request received GET /categories/{}", categoryId);
        return CategoryMapper.toOutputCategotyDto(service.getCategoryById(categoryId));
    }
}
