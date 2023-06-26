package ru.practicum.mainservice.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.exceptions.NotFoundException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    @Override
    public Category createCategory(Category category) {
        return repository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategories(int from, int size) {
        return repository.findAll(PageRequest.of(from / size, size)).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(long categoryId) {
        return repository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена или недоступна"));
    }

    @Override
    public void removeCategoryById(long categoryId) {
        repository.deleteById(categoryId);
    }

    @Override
    public Category updateCategory(Category category) {
        Category currentCategory = getCategoryById(category.getId());
        return currentCategory.withName(category.getName());
    }
}
