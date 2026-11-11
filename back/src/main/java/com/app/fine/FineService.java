package com.app.fine;

import com.app.appUser.AppUser;
import com.app.appUser.UserService;
import com.app.book_ordering.BookOrdering;
import com.app.enums.FineStatus;
import com.app.enums.Role;
import com.app.system.exception.BadRequestException;
import com.app.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FineService {

    // минимальный штраф за день просрочки, BYN
    public static final BigDecimal MIN_DAILY_RATE = new BigDecimal("0.50");

    private final FineRepository repository;
    private final UserService userService;

    public List<Fine> findAll() {
        AppUser user = userService.getCurrentUser();
        if (user.getRole() == Role.USER) return repository.findAllByOrderingOwnerIdOrderByIdDesc(user.getId());
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Fine find(String id) {
        Fine fine = repository.findById(Long.parseLong(id)).orElseThrow(() -> new ObjectNotFoundException("Не найден штраф по ИД: " + id));
        AppUser user = userService.getCurrentUser();
        if (user.getRole() == Role.USER && !fine.getOrdering().getOwner().getId().equals(user.getId())) {
            throw new ObjectNotFoundException("Не найден штраф по ИД: " + id);
        }
        return fine;
    }

    public boolean hasUnpaid(AppUser user) {
        return repository.existsByOrderingOwnerIdAndStatus(user.getId(), FineStatus.UNPAID);
    }

    // сумма: дни просрочки × дневная стоимость аренды, но не меньше MIN_DAILY_RATE в день
    public static BigDecimal amount(BookOrdering ordering, int daysOverdue) {
        BigDecimal rate = BigDecimal.valueOf(ordering.getPrice()).max(MIN_DAILY_RATE);
        return rate.multiply(BigDecimal.valueOf(daysOverdue)).setScale(2, RoundingMode.HALF_UP);
    }

    public Fine charge(BookOrdering ordering) {
        int days = ordering.getOverdueDays();
        if (days == 0 || ordering.getFine() != null) return ordering.getFine();
        Fine fine = repository.save(new Fine(ordering, days, amount(ordering, days)));
        ordering.setFine(fine);
        return fine;
    }

    public Fine pay(String id) {
        Fine fine = find(id);
        if (fine.getStatus() != FineStatus.UNPAID) throw new BadRequestException("Штраф уже закрыт: " + fine.getStatus().getName());
        fine.setStatus(FineStatus.PAID);
        fine.setDatePaid(LocalDate.now());
        return repository.save(fine);
    }

    public Fine cancel(String id) {
        Fine fine = find(id);
        if (fine.getStatus() != FineStatus.UNPAID) throw new BadRequestException("Штраф уже закрыт: " + fine.getStatus().getName());
        fine.setStatus(FineStatus.CANCELLED);
        return repository.save(fine);
    }

}
