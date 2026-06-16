package dio.shrinkiturlshortener.repository;

import dio.shrinkiturlshortener.domain.ShortenedUrl;
import dio.shrinkiturlshortener.dto.ShortenedUrlRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
class ShortenedUrlRepositoryTest {

    private static final String DEFAULT_URL = "https://www.google.com";
    private static final String DEFAULT_HASH_URL = "abc123";
    private static final String DEFAULT_HASH_URL_NOT_FOUND = "notfound";

    @Autowired
    ShortenedUrlRepository shortenedUrlRepository;

    @Autowired
    EntityManager entityManager;

    private ShortenedUrl createShortenedUrl(ShortenedUrlRequest request) {
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setUrl(request.url());
        shortenedUrl.setHashUrl(DEFAULT_HASH_URL);
        entityManager.persist(shortenedUrl);
        entityManager.flush();
        return shortenedUrl;
    }

    @Test
    @DisplayName("Deve salvar uma URL encurtada com sucesso")
    void saveCase1() {
        ShortenedUrl created =  new ShortenedUrl();
        created.setUrl(DEFAULT_URL);
        created.setHashUrl(DEFAULT_HASH_URL);

        ShortenedUrl result = shortenedUrlRepository.save(created);

        assertNotNull(result.getId());
        assertEquals(created.getUrl(), result.getUrl());
        assertEquals(created.getHashUrl(), result.getHashUrl());
    }

    @Test
    @DisplayName("Deve encontrar uma URL encurtada pelo hash")
    void findByHashUrlCase1() {
        ShortenedUrl created = createShortenedUrl(new ShortenedUrlRequest(DEFAULT_URL));

        Optional<ShortenedUrl> result = shortenedUrlRepository.findShortenedUrlByHashUrl(created.getHashUrl());

        assertTrue(result.isPresent());
        assertEquals(created.getUrl(), result.get().getUrl());
        assertEquals(created.getHashUrl(), result.get().getHashUrl());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar por um hash inexistente")
    void findByHashUrlCase2() {
        createShortenedUrl(new ShortenedUrlRequest(DEFAULT_URL));

        Optional<ShortenedUrl> result = shortenedUrlRepository.findShortenedUrlByHashUrl(DEFAULT_HASH_URL_NOT_FOUND);

        assertFalse(result.isPresent());
    }
}
