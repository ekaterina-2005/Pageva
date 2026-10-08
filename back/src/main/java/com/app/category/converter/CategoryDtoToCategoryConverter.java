package com.app.category.converter;

import com.app.category.Category;
import com.app.category.CategoryDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoToCategoryConverter implements Converter<CategoryDto, Category> {
    @Override
    public Category convert(CategoryDto source) {
        return new Category(
                source.name()
        );
    }
}
