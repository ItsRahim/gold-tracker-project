package com.rahim.common.service.http.implementation;

import com.rahim.common.service.http.HttpClientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class HttpClientImplementation implements HttpClientService {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientImplementation.class);
    private final LoadBalancerClient loadBalancer;
    private final RestTemplate restTemplate;

    @Override
    public <T> ResponseEntity<T> callService(String serviceName, Class<T> responseType, String endpoint) {
        return callService(serviceName, endpoint, responseType, (Object[]) null);
    }

    @Override
    public <T> ResponseEntity<T> callService(String serviceName, String endpoint, Class<T> responseType, Object... pathVariables) {
        ServiceInstance instance = loadBalancer.choose(serviceName);

        if (instance == null) {
            LOG.error("No instance available for service: {}", serviceName);
            return ResponseEntity.status(503).body(null);
        }

        String baseUrl = instance.getUri().toString();
        String url = baseUrl + endpoint;

        if (pathVariables != null && pathVariables.length > 0) {
            url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .path(endpoint)
                    .buildAndExpand(pathVariables)
                    .toUriString();
        }

        LOG.info("Calling service: {}, URL: {}", serviceName, url);

        return restTemplate.getForEntity(url, responseType);
    }
}
