package com.example.bookstoreai.service.ai;

import com.example.bookstoreai.dto.ai.ChatResponse;
import com.example.bookstoreai.security.SecurityUtil;
import com.example.bookstoreai.service.IAgentService;
import com.example.bookstoreai.tools.BookSearchTool;
import com.example.bookstoreai.tools.ReportTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class AgentService implements IAgentService {

    private final ChatClient chatClient;
    private final BookSearchTool bookSearchTool;
    private final ReportTool reportTool;
    private final ChatMemory chatMemory;
    private final VectorStore vectorStore;
    private final SecurityUtil securityUtil;
    private final BeanOutputConverter<ChatResponse> responseConverter;

    public AgentService(ChatClient.Builder chatClientBuilder,
                        BookSearchTool bookSearchTool,
                        ReportTool reportTool,
                        ChatMemory chatMemory,
                        VectorStore vectorStore,
                        SecurityUtil securityUtil) {
        this.chatClient = chatClientBuilder.build();
        this.bookSearchTool = bookSearchTool;
        this.reportTool = reportTool;
        this.chatMemory = chatMemory;
        this.vectorStore = vectorStore;
        this.securityUtil = securityUtil;
        this.responseConverter = new BeanOutputConverter<>(ChatResponse.class);
    }

    @Override
    public ChatResponse chat(String userMessage, String conversationId) {
        String email = securityUtil.getAuthenticatedEmail();
        String convId = (conversationId != null && !conversationId.isBlank()) ? conversationId : email;

        try {
            String raw = chatClient.prompt()
                .system(buildSystemPrompt())
                .user(userMessage)
                .tools(bookSearchTool, reportTool)
                .advisors(
                    MessageChatMemoryAdvisor.builder(chatMemory)
                        .conversationId(convId)
                        .build(),
                    QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(SearchRequest.builder().topK(5).similarityThreshold(0.3).build())
                        .build()
                )
                .call()
                .content();

            ChatResponse response = responseConverter.convert(raw);

            if (response != null) {
                return new ChatResponse(
                    response.success(),
                    response.summary(),
                    response.books() != null ? response.books() : List.of(),
                    response.chartData() != null ? response.chartData() : List.of(),
                    email
                );
            }

            return new ChatResponse(false, raw, List.of(), List.of(), email);

        } catch (Exception e) {
            log.error("Error en AgentService usuario={} conv={}", email, convId, e);
            return new ChatResponse(false, "Error al procesar tu consulta", List.of(), List.of(), email);
        }
    }

    @Override
    public void deleteConversation(String conversationId) {
        chatMemory.clear(conversationId);
        log.info("Conversacion eliminada: {}", conversationId);
    }

    private String buildSystemPrompt() {
        return """
            Eres BookstoreAI, el asistente virtual de nuestra libreria.

            IDENTIDAD:
            - Nombre: BookstoreAI
            - Idioma: Espanol
            - Tono: Profesional pero cercano
            - Fecha actual: %s

            ROL:
            - Para CLIENTES: ayudas a descubrir libros y recomiendas basado en historial de compras
            - Para el OWNER: generas reportes de ventas de la libreria

            REGLAS ESTRICTAS:
            1. SOLO responde con datos reales obtenidos de las herramientas disponibles
            2. NUNCA inventes libros, autores, precios ni estadisticas
            3. Si no encuentras datos, indica claramente que no hay resultados
            4. Responde UNICAMENTE con JSON valido. NO incluyas texto antes ni despues del JSON. NO uses markdown ni ```
            5. El campo summary debe ser un resumen claro y conciso en espanol

            REGLAS PARA GRAFICOS (chartData):
            6. Cuando el resultado de generateSalesReport contenga ventas mensuales, SIEMPRE debes llenar chartData
            7. Cada mes de ventas = un objeto en chartData con label (MM/YYYY) y value (totalRevenue como numero)
               Ejemplo: [{"label":"01/2025","value":150.00},{"label":"02/2025","value":223.00}]
            8. NUNCA dejes chartData vacio si hay datos de ventas mensuales en el reporte
            9. Si no hay datos para graficos (busqueda de libros, recomendaciones), deja chartData como []

            HERRAMIENTAS DISPONIBLES:
            - searchBooksBySimilarity: buscar libros por tematica usando busqueda semantica
            - getRecommendations: recomendar libros basado en historial de compras del usuario
            - generateSalesReport: reporte de ventas con top libros vendidos (solo para libreros)

            %s
            """.formatted(LocalDate.now(), responseConverter.getFormat());
    }
}
