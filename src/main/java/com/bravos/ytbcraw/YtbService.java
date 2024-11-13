package com.bravos.ytbcraw;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class YtbService {

    private String apiKey;

    public YtbService(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public VideoInfo getVideoInfo(String videoId) {
        String urlString = "https://www.googleapis.com/youtube/v3/videos?id=" + videoId
                + "&key=" + apiKey + "&fields=items(snippet(title,description,thumbnails))&part=snippet";
        try {
            URI uri = new URI(urlString);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int resCode = connection.getResponseCode();
            if (resCode == 200) {
                Scanner scanner = new Scanner(uri.toURL().openStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();
                ObjectMapper objectMapper = new ObjectMapper();
                YouTubeResponse youTubeResponse = objectMapper.readValue(response.toString(), YouTubeResponse.class);
                if(youTubeResponse != null) {
                    List<Item> item = youTubeResponse.getItems();
                    if(item != null && !item.isEmpty()) {
                        Snippet snippet = item.getFirst().getSnippet();
                        if(snippet != null) {
                            VideoInfo videoInfo = new VideoInfo();
                            videoInfo.setVideoId(videoId);
                            videoInfo.setTitle(snippet.getTitle());
                            videoInfo.setDescription(snippet.description);
                            videoInfo.setThumbnails(snippet.getThumbnails());
                            return videoInfo;
                        }

                    }
                }
            }
            return null;
        } catch (URISyntaxException | IOException e) {
            System.err.println("Error when call YTB API: " + e.getMessage());
            return null;
        }
    }

    public String convertUrlToVideoId(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        url = url.replaceFirst("https://", "");
        url = url.replaceFirst("www\\.", "");
        url = url.replaceFirst("m\\.", "");
        if (url.startsWith("youtube.com/")) {
            try {
                int cut = url.lastIndexOf("?v=") + 3;
                return url.substring(cut, cut + 11);
            } catch (Exception e) {
                return null;
            }
        }
        if (url.startsWith("youtu.be/")) {
            String result = url.substring("youtu.be/".length(), url.indexOf("?si"));
            if (result.length() != 11) return null;
            return result;
        }
        return null;
    }

    private static class YouTubeResponse {
        private List<Item> items;

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }
    }

    private static class Item {
        private Snippet snippet;

        public Snippet getSnippet() {
            return snippet;
        }

        public void setSnippet(Snippet snippet) {
            this.snippet = snippet;
        }
    }

    public static class Snippet {

        private String title;
        private String description;
        private Map<String, Thumbnail> thumbnails;

        public String getTitle() {
            return title;
        }

        public Map<String, Thumbnail> getThumbnails() {
            return thumbnails;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setThumbnails(Map<String, Thumbnail> thumbnails) {
            this.thumbnails = thumbnails;
        }
    }



}
