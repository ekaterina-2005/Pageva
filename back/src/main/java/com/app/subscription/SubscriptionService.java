package com.app.subscription;

import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository repository;

    public List<Subscription> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Subscription find(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new ObjectNotFoundException("Не найдена подписка по ИД: " + id));
    }

    public Subscription save(Subscription save) {
        return repository.save(save);
    }

    public Subscription update(String id, Subscription update) {
        Subscription old = find(id);
        old.update(update);
        return repository.save(old);
    }

    public void delete(String id) {
        repository.deleteById(find(id).getId());
    }

}
