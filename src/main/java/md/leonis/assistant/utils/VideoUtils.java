package md.leonis.assistant.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoUtils {

    public static String getYoutubeVideoId(String youtubeUrl) {
        String video_id = "";
        if (youtubeUrl != null && youtubeUrl.startsWith("http")) {
            String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(youtubeUrl);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }

    public static String getYouTubeThumbnail(String youtubeId) {
        return getYouTubeThumbnail(youtubeId, "1");
    }

    public static String getYouTubeThumbnail(String youtubeId, String name) {
        return String.format("https://img.youtube.com/vi/%s/%s.jpg", youtubeId, name);
    }
}
