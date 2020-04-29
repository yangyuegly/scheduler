package edu.brown.cs.student.webscraper;
import java.net.*;
import java.net.http.*;
import javax.net.ssl.*;

/**
 * By default, basic authentication is disabled when tunneling through an
 * authenticating proxy since Java 8u111.  It must be turned on at the
 * command line.  On Linux:
 *
 * java -Djdk.http.auth.tunneling.disabledSchemes="" ProxyTest
 *
 * and on Windows:
 *
 * java -D"jdk.http.auth.tunneling.disabledSchemes"="" ProxyTest
 */

public class Proxy {
  // Create a trust manager that does not validate certificate chains.
  private static TrustManager[] trustAllCerts = new TrustManager[]{
    new X509TrustManager() {
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
      }
      public void checkClientTrusted(
        java.security.cert.X509Certificate[] certs, String authType) {
      }
      public void checkServerTrusted(
        java.security.cert.X509Certificate[] certs, String authType) {
      }
    }
  };

  public static void main(String[] args) {
    final String proxyHost = "proxy-server.scraperapi.com";
    final int proxyPort = 8001;
    final String proxyUser = "scraperapi";
    final String proxyPassword = "51c2dbec3c6fdcfd263d55113cc32cf3";

    Authenticator authenticator = new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
      }
    };

    try {
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());

      HttpClient httpClient = HttpClient.newBuilder()
        .proxy(ProxySelector.of(new InetSocketAddress(proxyHost, proxyPort)))
        .authenticator(authenticator)
        .sslContext(sc)
        .build();

      HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create("https://httpbin.org/ip"))
        .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      // print response headers
      HttpHeaders headers = response.headers();
      headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

      // print status code
      System.out.println(response.statusCode());

      // print response body
      System.out.println(response.body());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}