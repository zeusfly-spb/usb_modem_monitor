import java.net.*;

public class TlsCheck {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://dl.google.com/dl/android/maven2/com/android/tools/build/gradle-api/9.5.2/gradle-api-9.5.2.pom");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        System.out.println("HTTP status: " + connection.getResponseCode());
    }
}