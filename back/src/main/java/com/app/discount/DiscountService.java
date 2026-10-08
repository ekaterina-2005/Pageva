package com.app.discount;

import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository repository;

    public List<Discount> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Discount find(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new ObjectNotFoundException("Не найдена скидка по ИД: " + id));
    }

    public Discount save(Discount save) {
        return repository.save(save);
    }

    public Discount update(String id, Discount update) {
        Discount old = find(id);
        old.update(update);
        return repository.save(old);
    }

    public void delete(String id) {
        repository.deleteById(find(id).getId());
    }

}
