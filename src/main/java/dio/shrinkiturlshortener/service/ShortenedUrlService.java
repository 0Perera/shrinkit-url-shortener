package dio.shrinkiturlshortener.service;

import dio.shrinkiturlshortener.domain.ShortenedUrl;
import dio.shrinkiturlshortener.dto.ShortenedUrlRequest;
import dio.shrinkiturlshortener.dto.ShortenedUrlResponse;
import dio.shrinkiturlshortener.handler.NotFoundException;
import dio.shrinkiturlshortener.mapper.ShortenedUrlMapper;
import dio.shrinkiturlshortener.repository.ShortenedUrlRepository;
import io.seruco.encoding.base62.Base62;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShortenedUrlService {

    private final ShortenedUrlRepository shortenedUrlRepository;

    private final ShortenedUrlMapper shortenedUrlMapper;

    public ShortenedUrlService(ShortenedUrlRepository shortenedUrlRepository,  ShortenedUrlMapper shortenedUrlMapper) {
        this.shortenedUrlRepository = shortenedUrlRepository;
        this.shortenedUrlMapper = shortenedUrlMapper;
    }

    private String generateHash(ShortenedUrl shortenedUrl){
        Long id = shortenedUrl.getId();
        Base62 base62 = Base62.createInstance();
        final byte[] hash = base62.encode(id.toString().getBytes());
        return new String(hash);
    }

    @Transactional
    public ShortenedUrlResponse createShortenedUrl(ShortenedUrlRequest shortenedUrlRequest) {
        var entity = shortenedUrlMapper.toEntity(shortenedUrlRequest);
        var savedEntity = shortenedUrlRepository.save(entity);
        var hashUrl = generateHash(savedEntity);
        savedEntity.setHashUrl(hashUrl);
        return shortenedUrlMapper.toResponse(savedEntity);
    }

    @Cacheable(cacheNames = "urls", key = "#hashUrl")
    public ShortenedUrlResponse findUrlByHash(String hashUrl) {
       ShortenedUrl urlEntity = shortenedUrlRepository.findShortenedUrlByHashUrl(hashUrl)
               .orElseThrow(() -> new NotFoundException("Url não encontrada"));
       return shortenedUrlMapper.toResponse(urlEntity);
    }

}
