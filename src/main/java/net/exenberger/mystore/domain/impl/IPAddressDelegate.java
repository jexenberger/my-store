package net.exenberger.mystore.domain.impl;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component("ipAddressDelegate")
public class IPAddressDelegate implements Supplier<String> {

    private String url;

    public IPAddressDelegate(@Value("${dependency.ipify.url}") String url) {
        this.url = url;
    }

    String resolveIPAddress() {
        return RestClient.builder().build()
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    @Override
    public String get() {
        return resolveIPAddress();
    }
}
