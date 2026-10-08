package com.app.book_ordering;

import com.app.enums.BookOrderingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookOrderingRepository extends JpaRepository<BookOrdering, Long> {
    List<BookOrdering> findAllByStatus(BookOrderingStatus status);
}
