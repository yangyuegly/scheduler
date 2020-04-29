package edu.brown.cs.student.webscraper;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * By default, basic authentication is disabled when tunneling through an authenticating proxy since
 * Java 8u111. It must be turned on at the command line. On Linux:
 *
 * java -Djdk.http.auth.tunneling.disabledSchemes="" ProxyTest
 *
 * and on Windows:
 *
 * java -D"jdk.http.auth.tunneling.disabledSchemes"="" ProxyTest
 */

public class Proxy {
  // Create a trust manager that does not validate certificate chains.
  private static TrustManager[] trustAllCerts = new TrustManager[] {
      new X509TrustManager() {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
            String authType) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
            String authType) {
        }
      }
  };

  public static void main(String[] args) {
    final String proxyHost = "proxy-server.scraperapi.com";
    final int proxyPort = 8001;
    final String proxyUser = "scraperapi";
    final String proxyPassword = "719cbb99d309d504901ea3d8ec878862";

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
          .authenticator(authenticator).sslContext(sc).build();

      HttpRequest request = HttpRequest.newBuilder().GET()
          .uri(URI.create("https://cs.brown.edu/courses/cs0320/projects/index.html")).build();

      HttpResponse<String> response = httpClient.send(request,
          HttpResponse.BodyHandlers.ofString());
      // print response headers
      HttpHeaders headers = response.headers();
      headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

      // print status code
      System.out.println(response.statusCode());

      // print response body
      System.out.println(response.body());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}