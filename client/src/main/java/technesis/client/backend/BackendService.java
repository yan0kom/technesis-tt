package technesis.client.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.time.temporal.ChronoUnit.MILLIS;

public class BackendService {
    private static final String API_URL = "http://127.0.0.1:8080/";
    private static final int REQUEST_TIMEOUT_MS = 1000;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public CompletableFuture<Float> getCurrentTemperature() {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(new URI(API_URL + "current"))
                    .timeout(Duration.of(REQUEST_TIMEOUT_MS, MILLIS))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .header("test", "20240726")
                    .build();

            return responseFuture(request).thenApply(res -> (float) assertApiResponse(res).get("value").asDouble());
        } catch (URISyntaxException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<List<Float>> getTemperatures(int count, int offset) {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(new URI(String.format("%slist?count=%d&offset=%d", API_URL, count, offset)))
                    .timeout(Duration.of(REQUEST_TIMEOUT_MS, MILLIS))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .header("test", "20240726")
                    .build();

            return responseFuture(request).thenApply(res -> {
                var result = new ArrayList<Float>();
                assertApiResponse(res).get("values").iterator()
                        .forEachRemaining(node -> result.add((float) node.asDouble()));
                return result;
            });
        } catch (URISyntaxException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<Void> setTemperature(float value) {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(new URI(String.format("%sset?value=%f", API_URL, value)))
                    .timeout(Duration.of(REQUEST_TIMEOUT_MS, MILLIS))
                    .method("POST", HttpRequest.BodyPublishers.noBody())
                    .header("test", "20240726")
                    .build();

            return responseFuture(request).thenApply(res -> {
                assertApiResponse(res);
                return null;
            });
        } catch (URISyntaxException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    private JsonNode assertApiResponse(HttpResponse<String> res) {
        if (res.statusCode() != 200) {
            throw new ApiCallException(res.statusCode(), 0);
        }
        JsonNode apiRes = null;
        try {
            apiRes = objectMapper.readTree(res.body());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        int errorCode = apiRes.get("errorCode").asInt();
        if (errorCode != 0) {
            throw new ApiCallException(200, errorCode);
        }
        return apiRes;
    }

    private CompletableFuture<HttpResponse<String>> responseFuture(HttpRequest request) {
        return HttpClient.newBuilder()
                .build()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
