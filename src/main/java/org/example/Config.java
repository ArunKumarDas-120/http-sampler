package org.example;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import java.nio.file.Paths;
import java.util.Objects;

public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private static RestTemplate publisher;

    public static synchronized void configure(String keyStore, char[] keyStorePassword,
                                              String trustStore, char[] trustStorePassword,
                                              boolean isSelfSigned) {
        if (Objects.isNull(publisher)) {
            RestTemplate restTemplate = new RestTemplate();
            try {
                SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
                        .loadTrustMaterial(Paths.get(trustStore).toUri().toURL(), trustStorePassword, new TrustSelfSignedStrategy())
                        .loadKeyMaterial(Paths.get(keyStore).toUri().toURL(), keyStorePassword, keyStorePassword).build(),
                        NoopHostnameVerifier.INSTANCE);
                HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
                        .setMaxConnTotal(5)
                        .setMaxConnPerRoute(5)
                        .build();
                HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
                requestFactory.setReadTimeout(10000);
                requestFactory.setConnectTimeout(10000);
                restTemplate.setRequestFactory(requestFactory);
            } catch (Exception exception) {
                log.error("{} {}",exception.getMessage(),exception);
            }
            log.info("{}",restTemplate);
            publisher = restTemplate;
        }
    }

    public static void hit(String url) {
        log.info("result {} ",publisher.getForEntity(url, String.class));
    }
}
