package dio.shrinkiturlshortener.mapper;

import dio.shrinkiturlshortener.domain.ShortenedUrl;
import dio.shrinkiturlshortener.dto.ShortenedUrlRequest;
import dio.shrinkiturlshortener.dto.ShortenedUrlResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShortenedUrlMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hashUrl", ignore = true)
    ShortenedUrl toEntity(ShortenedUrlRequest shortenedUrlRequest);

    ShortenedUrlResponse toResponse(ShortenedUrl entity);
}
