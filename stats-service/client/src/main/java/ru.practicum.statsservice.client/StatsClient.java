package ru.practicum.statsservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.practicum.statsservice.dto.InputHitDto;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class StatsClient {
    private final ObjectMapper mapper;
    String url;
    HttpClient client;

    public StatsClient(String url) throws IOException, InterruptedException {
        this.url = url;
        mapper = new ObjectMapper();
        client = HttpClient.newHttpClient();
    }

    public String createHit(InputHitDto hitDto) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(hitDto);
        HttpRequest request = HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url + "/hit"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public /*List<OutputHitDto>*/String getStats(Timestamp beginStatTime,
                                                 Timestamp endStatTime,
                                                 List<String> uris,
                                                 boolean isUnique) throws IOException, InterruptedException {
        if (beginStatTime == null || endStatTime == null) {
            return null;
        }
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beginStatTime);
        String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endStatTime);
        url += "/stats?";
        String params = "start=" + start + "&end=" + end;
        if (uris != null && uris.size() > 0) {
            params += "&uris=" + mapper.writeValueAsString(uris);
        }
        params += "&unique=" + isUnique;
        System.out.println(url + params);
        params = URLEncoder.encode(params, StandardCharsets.UTF_8);
        System.out.println(url + params);
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create(url + params))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}