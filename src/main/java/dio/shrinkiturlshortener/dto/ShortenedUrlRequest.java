package dio.shrinkiturlshortener.dto;

import jakarta.validation.constraints.NotBlank;

public record ShortenedUrlRequest(
        @NotBlank(message = "É campo url é obrigatorio")
        String url
) {
}
