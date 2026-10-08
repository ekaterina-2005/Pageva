package com.app.category.converter;

import com.app.category.Category;
import com.app.category.CategoryDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryDtoConverter implements Converter<Category, CategoryDto> {
    @Override
    public CategoryDto convert(Category source) {
        return new CategoryDto(
                source.getId(),
                source.getName()
        );
    }
}
