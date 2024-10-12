package com.nhnacademy.miniDooray.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class RestApiClient {
    private final RestTemplate restTemplate;

    public <T> ResponseEntity<T> sendGetRequest(String url, Class<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    public <T, R> ResponseEntity<R> sendPostRequest(String url, T requestBody, Class<R> responseType) {
        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
    }

    public <T, R> ResponseEntity<R> sendPutRequest(String url, T requestBody, Class<R> responseType) {
        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody);
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType);
    }

    public ResponseEntity<Void> sendDeleteRequest(String url) {
        return restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    }

    public <T, R> ResponseEntity<R> sendRequestWithHeaders(String url, HttpMethod method, T requestBody, HttpHeaders headers, Class<R> responseType) {
        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(url, method, requestEntity, responseType);
    }
}
