package com.app.book_copy;

import com.app.enums.BookCopyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findAllByBookIdOrderById(Long bookId);

    Optional<BookCopy> findFirstByBookIdAndStatusOrderById(Long bookId, BookCopyStatus status);

    boolean existsByInventoryNumber(String inventoryNumber);
}
