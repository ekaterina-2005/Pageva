package com.app.fine.converter;

import com.app.book_ordering.BookOrdering;
import com.app.fine.Fine;
import com.app.fine.FineDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FineToFineDtoConverter implements Converter<Fine, FineDto> {
    @Override
    public FineDto convert(Fine source) {
        BookOrdering ordering = source.getOrdering();
        return new FineDto(
                source.getId(),

                source.getDaysOverdue(),
                source.getAmount(),

                source.getStatus().name(),
                source.getStatus().getName(),

                source.getDateCreated() == null ? "" : source.getDateCreated().toString(),
                source.getDatePaid() == null ? "" : source.getDatePaid().toString(),

                ordering.getId(),

                ordering.getBook().getId(),
                ordering.getBook().getName(),
                ordering.getInventoryNumber(),

                ordering.getOwner().getId(),
                ordering.getOwner().getFio()
        );
    }
}
