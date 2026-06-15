package dio.shrinkiturlshortener.config;


import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IpRateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> requestCounts = new ConcurrentHashMap<>();

    private  Bucket createBucket () {
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(20).refillGreedy(1, Duration.ofSeconds(40)))
                .build();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var ipAddress = request.getRemoteAddr();
        var bucket = requestCounts.computeIfAbsent(ipAddress, ip -> createBucket());

        if (bucket.tryConsume(1)) {
            return true;
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Muitas tentativas. Tente novamente mais tarde.");
            return false;
        }

    }


}
