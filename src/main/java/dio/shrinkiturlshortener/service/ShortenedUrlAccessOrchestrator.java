package dio.shrinkiturlshortener.service;

import dio.shrinkiturlshortener.dto.ShortenedUrlResponse;
import org.springframework.stereotype.Service;

@Service
public class ShortenedUrlAccessOrchestrator {

    private final ShortenedUrlService shortenedUrlService;

    public ShortenedUrlAccessOrchestrator(ShortenedUrlService shortenedUrlService) {
        this.shortenedUrlService = shortenedUrlService;
    }

    public ShortenedUrlResponse findAndCountAccess(String hashUrl){
        var response = shortenedUrlService.findUrlByHash(hashUrl);
        shortenedUrlService.incrementAccessCountByHashUrl(hashUrl);
        return response;
    }
}
