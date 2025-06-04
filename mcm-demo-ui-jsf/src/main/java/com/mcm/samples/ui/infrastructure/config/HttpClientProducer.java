package com.mcm.samples.ui.infrastructure.config;

import java.net.http.HttpClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class HttpClientProducer {

    @Produces
    public HttpClient getHttpClient() {
        return HttpClient.newHttpClient();
    }
}
