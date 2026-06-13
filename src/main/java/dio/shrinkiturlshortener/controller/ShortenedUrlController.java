package dio.shrinkiturlshortener.controller;

import dio.shrinkiturlshortener.dto.ShortenedUrlRequest;
import dio.shrinkiturlshortener.dto.ShortenedUrlResponse;
import dio.shrinkiturlshortener.service.ShortenedUrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShortenedUrlController {

    private final ShortenedUrlService shortenedUrlService;

    public ShortenedUrlController(ShortenedUrlService shortenedUrlService) {
        this.shortenedUrlService = shortenedUrlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenedUrlResponse> createShortenedUrl(@Valid @RequestBody ShortenedUrlRequest shortenedUrlRequest) {
        var response = shortenedUrlService.createShortenedUrl(shortenedUrlRequest);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{hash}")
    public ResponseEntity<Void> redirectByHash(@PathVariable String hash) {
        var response = shortenedUrlService.findUrlByHash(hash);
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", response.url()).build();
    }



}
