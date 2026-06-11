package dio.shrinkiturlshortener.dto;

public record ShortenedUrlResponse(
        String url,
        String hashUrl
) {
}
