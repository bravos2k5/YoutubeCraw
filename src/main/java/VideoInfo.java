import java.util.Map;

public class VideoInfo {

    private String videoId;
    private String title;
    private String description;
    private Map<String,Thumbnail> thumbnails;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Map<String, Thumbnail> thumbnails) {
        this.thumbnails = thumbnails;
    }

}
