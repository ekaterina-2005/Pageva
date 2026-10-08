package com.app.category;

import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public List<Category> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Category find(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new ObjectNotFoundException("Не найдена категория по ИД: " + id));
    }

    public Category save(Category save) {
        return repository.save(save);
    }

    public Category update(String id, Category update) {
        Category old = find(id);
        old.update(update);
        return repository.save(old);
    }

    public void delete(String id) {
        repository.deleteById(find(id).getId());
    }

}
