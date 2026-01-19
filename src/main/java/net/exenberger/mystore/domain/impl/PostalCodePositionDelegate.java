package net.exenberger.mystore.domain.impl;

import java.net.http.HttpClient;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.BiFunction;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import net.exenberger.mystore.domain.LatLongPosition;

@Component("postalCodePositionDelegate")
public class PostalCodePositionDelegate implements BiFunction<String, Integer, LatLongPosition> {

    private static SSLContext sslContext;

    protected static final TrustManager[] TM = {
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                    //default
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                    //default
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } };

    static {
        try {
            sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null, TM, null);

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            //not the end of the world if we get an exception here
        }
    }

    private final String url;

    public PostalCodePositionDelegate(@Value("${dependency.pdok.url}") String url) {
        this.url = url;
    }

    public LatLongPosition resolveLocationFromAddress(String postalCode, int houseNumber) {

        HttpClient client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .connectTimeout(Duration.ofMillis(30000))
                .build();


        try {
            var result = RestClient.builder()
                    .requestFactory(new JdkClientHttpRequestFactory(client))
                    .baseUrl(url)
                    .build()

                    .get()
                    .uri("/free?q=\"{postalCode} {houseNumber}\"&fl=centroide_ll&fq=type:(gemeente OR woonplaats OR weg OR postcode OR adres)&df=tekst&bq=type:postcode^1&start=0&rows=10&sort=score desc,sortering asc,weergavenaam asc&wt=json",
                            postalCode, houseNumber)
                    .retrieve()
                    .body(Map.class);

            var locationMap = (Map<?, ?>) result.get("response");
            var docs = ((List<Map<String, String>>) locationMap.get("docs")).get(0).get("centroide_ll");
            var tokenizer = new StringTokenizer(docs, " POINT()");
            var lat = Double.valueOf(tokenizer.nextToken());
            var lon = Double.valueOf(tokenizer.nextToken());
            return new LatLongPosition(lon, lat);
        } catch (IndexOutOfBoundsException e) {
            //will happen when no response is found for the postal code
            return null;
        }

    }

    @Override
    public LatLongPosition apply(String s, Integer integer) {
        return resolveLocationFromAddress(s, integer);
    }
}
