package com.cknoe.backend_springboot.service.external;

import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cknoe.backend_springboot.dto.OpenGraphResponseDTO;
import com.cknoe.backend_springboot.exception.InvalidUrlException;

@Service
public class OpenGraphService {
    private final Set<String> allowedDomains;

    public OpenGraphService(Set<String> allowedDomains) {
        this.allowedDomains = allowedDomains;
    }

    @Cacheable("opengraph")
    public OpenGraphResponseDTO fetch(String url) {

        validateUrl(url);

        try {

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .followRedirects(true)
                    .get();

            String title = getMeta(doc, "og:title");
            String image = getMeta(doc, "og:image");
            String siteName = getMeta(doc, "og:site_name");

            // fallback standards
            if (title == null) {
                title = doc.title();
            }

            image = resolveUrl(url, image);

            return new OpenGraphResponseDTO(
                    title,
                    image,
                    siteName,
                    url);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to fetch OpenGraph data",
                    e);
        }
    }

    private String getMeta(Document doc, String property) {
        Element meta = doc.selectFirst(
                "meta[property=" + property + "]");

        return meta != null
                ? meta.attr("content")
                : null;
    }

    private void validateUrl(String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if (!"http".equals(scheme) && !"https".equals(scheme)) {
                throw new IllegalArgumentException("Invalid scheme");
            }

            String host = uri.getHost();
            if (host == null || host.isBlank()) {
                throw new IllegalArgumentException("Invalid host");
            }

            boolean allowed = allowedDomains.stream().anyMatch(domain -> host.equals(domain) ||
                    host.endsWith("." + domain));

            if (!allowed) {
                throw new IllegalArgumentException("Domain not allowed");
            }

            InetAddress address = InetAddress.getByName(host);

            if (address.isAnyLocalAddress()
                    || address.isLoopbackAddress()
                    || address.isSiteLocalAddress()) {

                throw new IllegalArgumentException("Blocked internal IP");
            }

        } catch (Exception e) {
            throw new InvalidUrlException(url, e.getMessage());
        }
    }

    private String resolveUrl(String baseUrl, String maybeRelative) {

        if (maybeRelative == null) {
            return null;
        }

        try {
            return new URL(new URL(baseUrl), maybeRelative)
                    .toString();

        } catch (Exception e) {
            return maybeRelative;
        }
    }
}