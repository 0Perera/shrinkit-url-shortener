package dio.shrinkiturlshortener.controller;

import dio.shrinkiturlshortener.dto.ShortenedUrlRequest;
import dio.shrinkiturlshortener.dto.ShortenedUrlResponse;
import dio.shrinkiturlshortener.handler.NotFoundException;
import dio.shrinkiturlshortener.service.ShortenedUrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = ShortenedUrlController.class)
class ShortenedUrlControllerTest {

    private static final Long DEFAULT_SHORTENED_URL_ID = 1L;
    private static final String DEFAULT_URL = "https://www.google.com";
    private static final String DEFAULT_HASH_URL = "abc123";
    private static final String DEFAULT_HASH_URL_NOT_FOUND = "notfound";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ShortenedUrlService shortenedUrlService;

    private ShortenedUrlRequest createDefaultRequest() {
        return new ShortenedUrlRequest(DEFAULT_URL);
    }

    private ShortenedUrlResponse createDefaultResponse() {
        return new ShortenedUrlResponse(DEFAULT_URL, DEFAULT_HASH_URL);
    }

    @Test
    @DisplayName("Deve retornar 201 Created ao criar uma URL encurtada com sucesso")
    void createShortenedUrlCase1() throws Exception {
        ShortenedUrlRequest shortenedUrlRequest = createDefaultRequest();
        ShortenedUrlResponse expectedResponse = createDefaultResponse();
        String requestBody = objectMapper.writeValueAsString(shortenedUrlRequest);

        when(shortenedUrlService.createShortenedUrl(shortenedUrlRequest)).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/shorten")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar criar uma URL encurtada com URL inválida")
    void createShortenedUrlCase2() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new  ShortenedUrlRequest("Invalid URL"));

        mockMvc.perform(MockMvcRequestBuilders.post("/shorten")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar criar uma URL encurtada com URL vazia")
    void createShortenedUrlCase3() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new  ShortenedUrlRequest(""));

        mockMvc.perform(MockMvcRequestBuilders.post("/shorten")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Deve retornar 302 Found e redirecionar para URL original ao acessar a URL encurtada com hash válido")
    void redirectByHashCase1() throws Exception {
        when(shortenedUrlService.findUrlByHash(DEFAULT_HASH_URL)).thenReturn(createDefaultResponse());

        mockMvc.perform(MockMvcRequestBuilders.get("/" + DEFAULT_HASH_URL))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", DEFAULT_URL))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao acessar a URL encurtada com hash inválido")
    void redirectByHashCase2() throws Exception {
        when(shortenedUrlService.findUrlByHash(DEFAULT_HASH_URL_NOT_FOUND)).thenThrow(new NotFoundException("URL não encontrada"));

        mockMvc.perform(MockMvcRequestBuilders.get("/" + DEFAULT_HASH_URL_NOT_FOUND))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

}
