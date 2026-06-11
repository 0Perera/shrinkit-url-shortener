package dio.shrinkiturlshortener.repository;

import dio.shrinkiturlshortener.domain.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {

    Optional<ShortenedUrl> findShortenedUrlByHashUrl(String hashUrl);

}
