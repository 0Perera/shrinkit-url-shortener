package dio.shrinkiturlshortener.service;

import dio.shrinkiturlshortener.domain.ShortenedUrl;
import dio.shrinkiturlshortener.dto.ShortenedUrlRequest;
import dio.shrinkiturlshortener.dto.ShortenedUrlResponse;
import dio.shrinkiturlshortener.handler.NotFoundException;
import dio.shrinkiturlshortener.mapper.ShortenedUrlMapper;
import dio.shrinkiturlshortener.repository.ShortenedUrlRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortenedUrlServiceTest {

    private static final Long DEFAULT_SHORTENED_URL_ID = 1L;
    private static final String DEFAULT_URL = "https://www.google.com";
    private static final String DEFAULT_HASH_URL = "abc123";
    private static final String DEFAULT_HASH_URL_NOT_FOUND = "notfound";


    @Mock
    ShortenedUrlRepository shortenedUrlRepository;

    @Mock
    ShortenedUrlMapper shortenedUrlMapper;

    @InjectMocks
    ShortenedUrlService shortenedUrlService;

    private ShortenedUrl createDefaultEntity(){
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setId(DEFAULT_SHORTENED_URL_ID);
        shortenedUrl.setUrl(DEFAULT_URL);
        shortenedUrl.setHashUrl(DEFAULT_HASH_URL);
        return shortenedUrl;
    }

    private ShortenedUrlRequest createDefaultRequest(){
        return new ShortenedUrlRequest(DEFAULT_URL);
    }

    private ShortenedUrlResponse createDefaultResponse(){
        return new ShortenedUrlResponse(DEFAULT_URL, DEFAULT_HASH_URL);
    }

    @Test
    @DisplayName("Deve criar uma URL encurtada com sucesso")
    void createShortenedUrlCase1() {
        ShortenedUrlRequest shortenedUrlRequest = createDefaultRequest();
        ShortenedUrl shortenedUrl = createDefaultEntity();
        ShortenedUrlResponse expectedResponse = createDefaultResponse();

        when(shortenedUrlMapper.toEntity(shortenedUrlRequest)).thenReturn(shortenedUrl);
        when(shortenedUrlRepository.save(shortenedUrl)).thenReturn(shortenedUrl);
        when(shortenedUrlMapper.toResponse(shortenedUrl)).thenReturn(expectedResponse);

        ShortenedUrlResponse result = shortenedUrlService.createShortenedUrl(shortenedUrlRequest);

        assertEquals(expectedResponse, result);
        verify(shortenedUrlMapper).toEntity(shortenedUrlRequest);
        verify(shortenedUrlRepository).save(shortenedUrl);
        verify(shortenedUrlMapper).toResponse(shortenedUrl);
    }

    @Test
    @DisplayName("Deve encontrar uma URL encurtada pelo hash com sucesso")
    void findUrlByHashCase1() {
        ShortenedUrl shortenedUrl = createDefaultEntity();
        ShortenedUrlResponse expectedResponse = createDefaultResponse();

        when(shortenedUrlRepository.findShortenedUrlByHashUrl(DEFAULT_HASH_URL)).thenReturn(Optional.of(shortenedUrl));
        when(shortenedUrlMapper.toResponse(shortenedUrl)).thenReturn(expectedResponse);

        ShortenedUrlResponse result = shortenedUrlService.findUrlByHash(DEFAULT_HASH_URL);

        assertEquals(expectedResponse, result);
        verify(shortenedUrlRepository).findShortenedUrlByHashUrl(DEFAULT_HASH_URL);
        verify(shortenedUrlMapper).toResponse(shortenedUrl);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar por um hash inexistente")
    void findUrlByHashCase2() {
        when(shortenedUrlRepository.findShortenedUrlByHashUrl(DEFAULT_HASH_URL_NOT_FOUND)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> shortenedUrlService.findUrlByHash(DEFAULT_HASH_URL_NOT_FOUND));
        verify(shortenedUrlRepository).findShortenedUrlByHashUrl(DEFAULT_HASH_URL_NOT_FOUND);
    }

}
