package com.app.fine;

import com.app.enums.FineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findAllByOrderingOwnerIdOrderByIdDesc(Long ownerId);

    boolean existsByOrderingOwnerIdAndStatus(Long ownerId, FineStatus status);
}
