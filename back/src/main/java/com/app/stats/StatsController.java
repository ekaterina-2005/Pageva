package com.app.stats;

import com.app.book.Book;
import com.app.book.BookService;
import com.app.book.converter.BookToBookDtoConverter;
import com.app.book_comment.BookComment;
import com.app.book_ordering.BookOrdering;
import com.app.book_ordering.BookOrderingService;
import com.app.category.Category;
import com.app.category.CategoryService;
import com.app.enums.BookAge;
import com.app.enums.BookOrderingStatus;
import com.app.system.Result;
import com.app.system.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.app.util.Global.ADMIN;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Secured({ADMIN})
public class StatsController {

    private final CategoryService categoryService;
    private final BookOrderingService orderingService;
    private final BookToBookDtoConverter bookToBookDtoConverter;
    private final BookService bookService;

    @GetMapping("/books/categories/quantity")
    public Result booksCategoriesQuantity() {
        Map<String, List<?>> res = new HashMap<>();

        List<Category> categories = categoryService.findAll();

        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (Category category : categories) {
            names.add(category.getName());
            values.add(category.getBooks().stream().reduce(0, (i, book) -> i + book.getQuantity(), Integer::sum));
        }

        res.put("names", names);
        res.put("values", values);

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Books Categories Quantity",
                Collections.unmodifiableMap(res)
        );
    }

    @GetMapping("/books/age")
    public Result booksAge(@RequestParam(defaultValue = "") String age, @RequestParam(defaultValue = "") String categoryId) {
        Map<String, List<?>> res = new HashMap<>();

        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        List<Book> books = bookService.findAll();

        if (!age.isEmpty()) {
            try {
                BookAge bookAge = BookAge.valueOf(age);
                books = books.stream().filter(book -> book.getAge() == bookAge).toList();
            } catch (Exception e) {
                return new Result(
                        true,
                        StatusCode.SUCCESS,
                        "Success Books Age"
                );
            }
        }

        if (!categoryId.isEmpty()) {
            try {
                Category category = categoryService.find(categoryId);
                books = books.stream().filter(book -> book.getCategory().getId().equals(category.getId())).toList();
            } catch (Exception e) {
                return new Result(
                        true,
                        StatusCode.SUCCESS,
                        "Success Books Age"
                );
            }
        }

        for (BookAge i : BookAge.values()) {
            names.add(i.getName());
            values.add(books.stream().reduce(0, (integer, book) -> {
                if (book.getAge() == i) return integer + 1;
                return integer;
            }, Integer::sum));
        }

        res.put("names", names);
        res.put("values", values);

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Books Age",
                Collections.unmodifiableMap(res)
        );
    }

    @GetMapping("/books/orderings/size")
    public Result booksOrderingsSize(@RequestParam(defaultValue = "") String age, @RequestParam(defaultValue = "") String categoryId) {
        Map<String, List<?>> res = new HashMap<>();

        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        List<Book> books = bookService.findAll();

        books.sort(Comparator.comparing(Book::getOrderingsSize).reversed());

        if (!age.isEmpty()) {
            try {
                BookAge bookAge = BookAge.valueOf(age);
                books = books.stream().filter(book -> book.getAge() == bookAge).toList();
            } catch (Exception e) {
                return new Result(
                        true,
                        StatusCode.SUCCESS,
                        "Success Books Age"
                );
            }
        }

        if (!categoryId.isEmpty()) {
            try {
                Category category = categoryService.find(categoryId);
                books = books.stream().filter(book -> book.getCategory().getId().equals(category.getId())).toList();
            } catch (Exception e) {
                return new Result(
                        true,
                        StatusCode.SUCCESS,
                        "Success Books Age"
                );
            }
        }

        for (int i = 0; i < books.size(); i++) {
            if (i == 10) break;

            names.add(books.get(i).getName());
            values.add(books.get(i).getOrderingsSize());
        }

        res.put("names", names);
        res.put("values", values);

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Books Orderings Size",
                Collections.unmodifiableMap(res)
        );
    }

    @GetMapping("/books/score/up")
    public Result booksScoreUp(@RequestParam(defaultValue = "") String age, @RequestParam(defaultValue = "") String categoryId) {
        List<Book> books = bookService.findAll();

        books.sort(Comparator.comparing(Book::getScore).reversed());

        if (!age.isEmpty()) {
            try {
                BookAge bookAge = BookAge.valueOf(age);
                books = books.stream().filter(book -> book.getAge() == bookAge).toList();
            } catch (Exception e) {
                return new Result(
                        true,
                        StatusCode.SUCCESS,
                        "Success Books Age"
                );
            }
        }

        if (!categoryId.isEmpty()) {
            try {
                Category category = categoryService.find(categoryId);
                books = books.stream().filter(book -> book.getCategory().getId().equals(category.getId())).toList();
            } catch (Exception e) {
                return new Result(
                        true,
                        StatusCode.SUCCESS,
                        "Success Books Age"
                );
            }
        }

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Books Score Up",
                books.stream().limit(10).map(bookToBookDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @GetMapping("/books/score/down")
    public Result booksScoreDown(@RequestParam(defaultValue = "") String age, @RequestParam(defaultValue = "") String categoryId) {
        List<Book> books = bookService.findAll();

        books.sort(Comparator.comparing(Book::getScore));

        if (!age.isEmpty()) {
            try {
                BookAge bookAge = BookAge.valueOf(age);
                books = books.stream().filter(book -> book.getAge() == bookAge).toList();
            } catch (Exception e) {
                return new Result(
                        true,
                        StatusCode.SUCCESS,
                        "Success Books Age"
                );
            }
        }

        if (!categoryId.isEmpty()) {
            try {
                Category category = categoryService.find(categoryId);
                books = books.stream().filter(book -> book.getCategory().getId().equals(category.getId())).toList();
            } catch (Exception e) {
                return new Result(
                        true,
                        StatusCode.SUCCESS,
                        "Success Books Age"
                );
            }
        }

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Books Score Up",
                books.stream().limit(10).map(bookToBookDtoConverter::convert).collect(Collectors.toList())
        );
    }

    @GetMapping("/books/orderings/status")
    public Result orderingsStatus() {
        Map<String, List<?>> res = new HashMap<>();

        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (BookOrderingStatus i : BookOrderingStatus.values()) {
            names.add(i.getName());
            values.add(orderingService.findAllByStatus(i).size());
        }

        res.put("names", names);
        res.put("values", values);

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Orderings Status",
                Collections.unmodifiableMap(res)
        );
    }

    @GetMapping("/books/count")
    public Result booksCount() {
        Map<String, List<?>> res = new HashMap<>();

        List<Book> books = bookService.findAll();

        books.sort(Comparator.comparing(Book::getCount));
        Collections.reverse(books);

        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (int i = 0; i < books.size(); i++) {
            if (i == 10) break;

            names.add(books.get(i).getName());
            values.add(books.get(i).getCount());
        }

        res.put("names", names);
        res.put("values", values);

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Books Count",
                Collections.unmodifiableMap(res)
        );
    }

    @GetMapping("/books/orderings/sum")
    public Result booksOrderingsSum() {
        Map<String, List<?>> res = new HashMap<>();

        List<BookOrdering> orderings = orderingService.findAll();

        orderings.sort(Comparator.comparing(BookOrdering::getDate));

        if (orderings.isEmpty()) {
            return new Result(
                    true,
                    StatusCode.SUCCESS,
                    "Success Books Ordering Sum",
                    new HashMap<>()
            );
        }

        LinkedHashMap<String, Float> sum = new LinkedHashMap<>();

        for (BookOrdering i : orderings) {
            String date = i.getDate().substring(0, 7);
            if (sum.containsKey(date)) {
                sum.put(date, sum.get(date) + i.getSum());
            }
            sum.put(date, i.getSum());
        }

        List<String> names = new ArrayList<>(sum.keySet());
        List<Float> values = new ArrayList<>(sum.values());

        res.put("names", names);
        res.put("values", values);

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Books Orderings Sum",
                Collections.unmodifiableMap(res)
        );
    }

    @GetMapping("/books/ratio/time/score/scatter")
    public Result booksRatioTimeScoreScatter() {
        record Data(String name, List<List<Integer>> data) {
        }

        List<Data> res = new ArrayList<>();

        for (Book i : bookService.findAll()) {
            List<List<Integer>> temp = new ArrayList<>();
            for (BookComment c : i.getComments()) {
                temp.add(List.of(i.getReadMinute(), c.getScore()));
            }
            res.add(new Data(i.getName(), temp));
        }

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Books Ratio Time Score Scatter",
                res
        );
    }

    @GetMapping("/books/ratio/time/score/bubble")
    public Result booksRatioTimeScoreBubble() {
        record Data(String name, List<List<Integer>> data) {
        }

        List<Data> res = new ArrayList<>();

        for (Book i : bookService.findAll()) {
            List<List<Integer>> temp = new ArrayList<>();
            for (BookComment c : i.getComments()) {
                temp.add(List.of(i.getReadMinute(), c.getScore(), i.getReadMinute() / c.getScore()));
            }
            res.add(new Data(i.getName(), temp));
        }

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Books Ratio Time Score Bubble",
                res
        );
    }

    @GetMapping("/kpi/books-total")
    public Result kpiBooksTotal() {
        int total = bookService.findAll().stream().reduce(0,(i, book) -> i+book.getQuantity(),Integer::sum);

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success KPI Books Total",
                total
        );
    }

    @GetMapping("/kpi/books-total-in-library")
    public Result kpiBooksTotalInLibrary() {
        List<Book> books = bookService.findAll();
        List<BookOrdering> doneOrders = orderingService.findAllByStatus(BookOrderingStatus.DONE);

        // Считаем, сколько книг сейчас у клиентов
        Map<Long, Long> doneCounts = doneOrders.stream()
                .collect(Collectors.groupingBy(o -> o.getBook().getId(), Collectors.counting()));

        // Суммируем доступные книги: quantity - у клиентов (DONE)
        int total = books.stream()
                .mapToInt(b -> b.getQuantity() - doneCounts.getOrDefault(b.getId(), 0L).intValue())
                .sum();

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success KPI Books Total In Library",
                total
        );
    }

    @GetMapping("/kpi/books-avg-score")
    public Result kpiBooksAvgScore() {
        double avg = bookService.findAll()
                .stream()
                .flatMap(b -> b.getComments().stream())
                .mapToInt(BookComment::getScore)
                .average()
                .orElse(0);

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success KPI Avg Score",
                avg
        );
    }

    @GetMapping("/kpi/orderings-monthly")
    public Result kpiOrderingsMonthly() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        long count = orderingService.findAll()
                .stream()
                .filter(o -> {
                    String d = o.getDateStart(); // <--- ИЗМЕНЕНО
                    if (d == null || d.isBlank()) return false;
                    try {
                        LocalDate date = LocalDate.parse(d);
                        return date.getYear() == year && date.getMonthValue() == month;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();

        return new Result(true, StatusCode.SUCCESS,
                "Success KPI Orderings Monthly", count);
    }

    @GetMapping("/kpi/income-monthly")
    public Result kpiIncomeMonthly() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        double income = orderingService.findAll()
                .stream()
                .filter(o -> {
                    String d = o.getDateStart(); // <--- ИЗМЕНЕНО
                    if (d == null || d.isBlank()) return false;
                    try {
                        LocalDate date = LocalDate.parse(d);
                        return date.getYear() == year && date.getMonthValue() == month;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .mapToDouble(BookOrdering::getSum)
                .sum();

        return new Result(true, StatusCode.SUCCESS,
                "Success KPI Income Monthly", income);
    }
}
