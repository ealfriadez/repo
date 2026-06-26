package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.response.BulkUploadResponse;
import com.example.bookstoreai.dto.response.BulkUploadResponse.BulkError;
import com.example.bookstoreai.model.Author;
import com.example.bookstoreai.model.Book;
import com.example.bookstoreai.repository.AuthorRepository;
import com.example.bookstoreai.repository.BookRepository;
import com.example.bookstoreai.service.IBookImportService;
import com.example.bookstoreai.util.ExcelParserUtil;
import com.example.bookstoreai.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookImportServiceImpl implements IBookImportService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public BulkUploadResponse processExcel(MultipartFile file) {
        int created = 0;
        int updated = 0;
        List<BulkError> errors = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String title = ExcelParserUtil.getString(row.getCell(0));
                    String authorName = ExcelParserUtil.getString(row.getCell(1));
                    String genre = ExcelParserUtil.getString(row.getCell(2));
                    BigDecimal price = ExcelParserUtil.getNumber(row.getCell(3));
                    int stock = ExcelParserUtil.getNumber(row.getCell(4)).intValue();
                    String description = ExcelParserUtil.getString(row.getCell(5));

                    if (title == null || title.isBlank()) {
                        errors.add(new BulkError(i + 1, "Titulo es requerido"));
                        continue;
                    }
                    if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                        errors.add(new BulkError(i + 1, "Precio invalido"));
                        continue;
                    }

                    Author author = findOrCreateAuthor(authorName);

                    Optional<Book> existingBook = bookRepository.findByTitle(title);
                    if (existingBook.isPresent()) {
                        Book book = existingBook.get();
                        book.setPrice(price);
                        book.setStock(stock);
                        if (description != null) book.setDescription(description);
                        bookRepository.save(book);
                        updated++;
                    } else {
                        String slug = SlugUtil.toSlug(title, author.getFirstName(), author.getLastName());
                        Book book = Book.builder()
                                .title(title)
                                .genre(genre)
                                .description(description)
                                .price(price)
                                .stock(stock)
                                .slug(slug)
                                .author(author)
                                .build();
                        bookRepository.save(book);
                        created++;
                    }
                } catch (Exception e) {
                    errors.add(new BulkError(i + 1, e.getMessage()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el archivo Excel: " + e.getMessage());
        }

        return new BulkUploadResponse(created, updated, errors);
    }

    private Author findOrCreateAuthor(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            fullName = "Autor Desconocido";
        }
        String[] parts = fullName.trim().split("\\s+", 2);
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[1] : "";

        return authorRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseGet(() -> {
                    Author author = Author.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .build();
                    return authorRepository.save(author);
                });
    }
}
