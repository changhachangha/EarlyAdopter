package com.earlyadopter.backend.component.config;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.earlyadopter.backend.repository.*")
@ComponentScan(basePackages = {"com.earlyadopter.backend.*"})
public class ElasticsearchBaseConfig extends ElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.id}")
    private String id;

    @Value("${elasticsearch.password}")
    private String password;
    @Value("${elasticsearch.apiKey")
    private String apiKey;

    @Override
    public ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .withBasicAuth(id, password)
                .withSocketTimeout(20000)
                .withConnectTimeout(20000)
                .build();
    }
}
