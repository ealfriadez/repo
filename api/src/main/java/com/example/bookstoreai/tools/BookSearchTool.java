package com.example.bookstoreai.tools;

import com.example.bookstoreai.model.Book;
import com.example.bookstoreai.repository.BookRepository;
import com.example.bookstoreai.repository.SaleItemRepository;
import com.example.bookstoreai.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookSearchTool {

    private final VectorStore vectorStore;
    private final BookRepository bookRepository;
    private final SaleItemRepository saleItemRepository;
    private final SecurityUtil securityUtil;

    @Tool(description = "Busca libros por tematica usando busqueda semantica. Usar cuando el usuario quiere libros sobre un tema, emocion o estilo narrativo.")
    public String searchBooksBySimilarity(String query) {
        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder().query(query).topK(5).similarityThreshold(0.3).build());

        if (results.isEmpty()) {
            return "No se encontraron libros relacionados con: " + query;
        }

        return results.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n- ", "Libros encontrados:\n- ", ""));
    }

    @Tool(description = "Recomienda libros basado en el historial de compras del usuario. No requiere parametros.")
    public String getRecommendations() {
        String email = securityUtil.getAuthenticatedEmail();

        List<String> genres = saleItemRepository.findPurchasedGenresByUserEmail(email);
        if (genres.isEmpty()) {
            return "No tienes compras previas. Explora el catalogo para descubrir libros.";
        }

        List<Long> purchasedIds = saleItemRepository.findPurchasedBookIdsByUserEmail(email);
        if (purchasedIds.isEmpty()) {
            purchasedIds = List.of(-1L);
        }

        List<String> genresLower = genres.stream()
                .map(String::toLowerCase)
                .distinct()
                .toList();

        List<Book> recommendations = bookRepository.findRecommendationsByGenres(genresLower, purchasedIds);

        if (recommendations.isEmpty()) {
            return "No se encontraron nuevas recomendaciones en tus generos favoritos: " + String.join(", ", genres);
        }

        String libros = recommendations.stream()
                .map(b -> "- %s de %s %s | %s | S/. %s | Stock: %d".formatted(
                        b.getTitle(),
                        b.getAuthor().getFirstName(), b.getAuthor().getLastName(),
                        b.getGenre(), b.getPrice().toPlainString(), b.getStock()))
                .collect(Collectors.joining("\n"));

        return "Basado en tus compras de %s:\n%s".formatted(String.join(", ", genres), libros);
    }
}
