package dio.shrinkiturlshortener.service;

import dio.shrinkiturlshortener.dto.ShortenedUrlResponse;
import dio.shrinkiturlshortener.handler.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortenedUrlAccessOrchestratorTest {

    private static final String DEFAULT_URL = "https://www.google.com";
    private static final String DEFAULT_HASH_URL = "abc123";
    private static final String DEFAULT_HASH_URL_NOT_FOUND = "notfound";

    @Mock
    ShortenedUrlService shortenedUrlService;

    @InjectMocks
    ShortenedUrlAccessOrchestrator shortenedUrlAccessOrchestrator;

    private ShortenedUrlResponse createDefaultResponse() {
        return new ShortenedUrlResponse(DEFAULT_URL, DEFAULT_HASH_URL);
    }

    @Test
    @DisplayName("Deve buscar a URL e incrementar o contador de acessos, nessa ordem")
    void findAndCountAccessCase1() {
        ShortenedUrlResponse expectedResponse = createDefaultResponse();
        when(shortenedUrlService.findUrlByHash(DEFAULT_HASH_URL)).thenReturn(expectedResponse);

        ShortenedUrlResponse result = shortenedUrlAccessOrchestrator.findAndCountAccess(DEFAULT_HASH_URL);

        assertEquals(expectedResponse, result);
        InOrder order = inOrder(shortenedUrlService);
        order.verify(shortenedUrlService).findUrlByHash(DEFAULT_HASH_URL);
        order.verify(shortenedUrlService).incrementAccessCountByHashUrl(DEFAULT_HASH_URL);
    }

    @Test
    @DisplayName("Não deve incrementar o contador quando o hash não é encontrado")
    void findAndCountAccessCase2() {
        when(shortenedUrlService.findUrlByHash(DEFAULT_HASH_URL_NOT_FOUND))
                .thenThrow(new NotFoundException("Url não encontrada"));

        assertThrows(NotFoundException.class,
                () -> shortenedUrlAccessOrchestrator.findAndCountAccess(DEFAULT_HASH_URL_NOT_FOUND));

        verify(shortenedUrlService, never()).incrementAccessCountByHashUrl(DEFAULT_HASH_URL_NOT_FOUND);
    }

}