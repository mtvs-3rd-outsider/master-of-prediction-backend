package com.outsider.masterofpredictionbackend.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;


import java.util.Arrays;

@Configuration
public class ElasticsearchConfig {

    @Value("${spring.elasticsearch.uris}")
    private String[] elasticsearchUris;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(
                Arrays.stream(elasticsearchUris)
                        .map(this::createHttpHost)
                        .toArray(HttpHost[]::new)
        );
        RestClient restClient = builder.build();

        RestClientTransport transport = new RestClientTransport(restClient, new co.elastic.clients.json.jackson.JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }

    private HttpHost createHttpHost(String uri) {
        String[] parts = uri.replace("http://", "").split(":");
        String host = parts[0];
        int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 9200; // 기본 포트는 9200
        return new HttpHost(host, port, "http");
    }
}

