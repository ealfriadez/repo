package com.example.bookstoreai.service.ai;

import com.example.bookstoreai.model.Book;
import com.example.bookstoreai.repository.BookRepository;
import com.example.bookstoreai.service.IIngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngestionService implements IIngestionService {

    private final VectorStore vectorStore;
    private final BookRepository bookRepository;

    @Override
    public int ingestBooks() {
        List<Book> books = bookRepository.findAllWithAuthor();

        List<Document> documents = books.stream()
                .filter(b -> b.getDescription() != null && !b.getDescription().isBlank())
                .map(this::toDocument)
                .toList();

        if (documents.isEmpty()) {
            log.warn("No se encontraron libros con descripcion para indexar");
            return 0;
        }

        vectorStore.add(documents);
        log.info("Ingesta completada: {} libros indexados", documents.size());
        return documents.size();
    }

    private Document toDocument(Book book) {
        String content = "%s de %s %s | %s | S/. %s | %s".formatted(
                book.getTitle(),
                book.getAuthor().getFirstName(), book.getAuthor().getLastName(),
                book.getGenre(), book.getPrice().toPlainString(),
                book.getDescription());

        String docId = UUID.nameUUIDFromBytes(("book-" + book.getId()).getBytes()).toString();
        return new Document(docId, content,
                Map.of("bookId", String.valueOf(book.getId()), "type", "book"));
    }
}
