package ru.practicum.statsservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.practicum.statsservice.dto.InputHitDto;
import ru.practicum.statsservice.dto.OutputHitDto;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public List<OutputHitDto> getStats(Timestamp beginStatTime,
                                       Timestamp endStatTime,
                                       List<String> uris,
                                       boolean isUnique) throws IOException, InterruptedException {
        if (beginStatTime == null || endStatTime == null) {
            return null;
        }
        List<String> params = new ArrayList<>();
        addParam("start", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beginStatTime), params);
        addParam("end", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endStatTime), params);
        if (uris != null && uris.size() > 0) {
            for (String uri : uris) {
                addParam("uris", uri, params);
            }
        }
        addParam("unique", String.valueOf(isUnique), params);
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create(url + "/stats?" + String.join("&", params)))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readerForListOf(OutputHitDto.class).readValue(response.body());
    }

    private void addParam(String paramName, String paramValue, List<String> params) throws UnsupportedEncodingException {
        String charset = Charset.defaultCharset().name();
        params.add(URLEncoder.encode(paramName, charset) + "=" + URLEncoder.encode(paramValue, charset));
    }
}