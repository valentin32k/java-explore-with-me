package ru.practicum.mainservice.categories;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);

    List<Category> getCategories(int from, int size);

    Category getCategoryById(long categoryId);

    void removeCategoryById(long categoryId);

    Category updateCategory(Category category);
}
