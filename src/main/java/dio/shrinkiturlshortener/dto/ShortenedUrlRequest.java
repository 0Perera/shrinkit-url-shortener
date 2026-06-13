package dio.shrinkiturlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ShortenedUrlRequest(
        @NotBlank(message = "É campo URL é obrigatorio")
        @URL(message = "A URL deve ser válida")
        String url
) {
}
