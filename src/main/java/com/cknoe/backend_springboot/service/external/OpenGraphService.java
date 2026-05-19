package com.cknoe.backend_springboot.service.external;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.cknoe.backend_springboot.dto.OpenGraphResponseDTO;

@Service
public class OpenGraphService {

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

        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL is required");
        }

        if (!url.startsWith("http://")
                && !url.startsWith("https://")) {

            throw new IllegalArgumentException(
                    "Invalid URL scheme");
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