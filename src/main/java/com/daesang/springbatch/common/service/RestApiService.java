package com.daesang.springbatch.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 자주 사용되는 RestTemplate Get, Post, Put, Delete 의
 * IO Param Generic으로 설정
 * @param <T>
 */
@Service
public class RestApiService<T> {

    private RestTemplate restTemplate;

    @Autowired
    public RestApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * @param url (GET)
     * @param httpHeaders
     * @return
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<T> get(String url, HttpHeaders httpHeaders) throws RestClientException {
        return callApiEndpoint(url, HttpMethod.GET, httpHeaders, null, (Class<T>)Object.class);
    }

    /**
     * @param url (GET)
     * @param httpHeaders
     * @param clazz
     * @return
     */
    public ResponseEntity<T> get(String url, HttpHeaders httpHeaders, Class<T> clazz) throws RestClientException {
        return callApiEndpoint(url, HttpMethod.GET, httpHeaders, null, clazz);
    }

    /**
     * @param url (POST)
     * @param httpHeaders
     * @param body
     * @return
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<T> post(String url, HttpHeaders httpHeaders, Object body) throws RestClientException {
        return callApiEndpoint(url, HttpMethod.POST, httpHeaders, body,(Class<T>)Object.class);
    }

    /**
     * @param url (POST)
     * @param httpHeaders
     * @param body
     * @param clazz
     * @return
     */
    public ResponseEntity<T> post(String url, HttpHeaders httpHeaders, Object body, Class<T> clazz) throws RestClientException {
        return callApiEndpoint(url, HttpMethod.POST, httpHeaders, body, clazz);
    }

    /**
     * @param url (PUT)
     * @param httpHeaders
     * @param body
     * @return
     */
    @SuppressWarnings("unchecked")
    public ResponseEntity<T> put(String url, HttpHeaders httpHeaders, Object body) throws RestClientException {
        return callApiEndpoint(url, HttpMethod.PUT, httpHeaders, body,(Class<T>)Object.class);
    }

    /**
     * @param url (PUT)
     * @param httpHeaders
     * @param body
     * @param clazz
     * @return
     */
    public ResponseEntity<T> put(String url, HttpHeaders httpHeaders, Object body, Class<T> clazz) throws RestClientException {
        return callApiEndpoint(url, HttpMethod.PUT, httpHeaders, body, clazz);
    }

    /**
     * @param url (DELETE)
     * @param httpHeaders
     */
    @SuppressWarnings("unchecked")
    public void delete(String url, HttpHeaders httpHeaders) throws RestClientException {
        callApiEndpoint(url, HttpMethod.DELETE, httpHeaders, null, (Class<T>)Object.class);
    }

    /**
     * @param url (DELETE)
     * @param httpHeaders
     * @param clazz
     */
    public void delete(String url, HttpHeaders httpHeaders, Class<T> clazz) throws RestClientException {
        callApiEndpoint(url, HttpMethod.DELETE, httpHeaders, null, clazz);
    }

    /**
     * resttemplate exchange
     * @param url
     * @param httpMethod
     * @param httpHeaders
     * @param body
     * @param clazz
     * @return
     */
    private ResponseEntity<T> callApiEndpoint(String url, HttpMethod httpMethod, HttpHeaders httpHeaders, Object body, Class<T> clazz) throws RestClientException {
        return restTemplate.exchange(url, httpMethod, new HttpEntity<>(body, httpHeaders), clazz);
    }

    /**
     * 헤더 정보 세팅
     *
     * @return
     */
    public HttpHeaders setHeaderInfo(String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", contentType);
        return headers;
    }

}
