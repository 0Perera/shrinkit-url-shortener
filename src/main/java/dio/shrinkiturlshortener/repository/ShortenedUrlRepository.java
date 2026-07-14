package dio.shrinkiturlshortener.repository;

import dio.shrinkiturlshortener.domain.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {

    @Modifying
    @Query("UPDATE ShortenedUrl s SET s.accessCount = s.accessCount + 1 WHERE s.hashUrl = :hashUrl")
    int incrementAccessCountByHash(String hashUrl);

    Optional<ShortenedUrl> findShortenedUrlByHashUrl(String hashUrl);

}
