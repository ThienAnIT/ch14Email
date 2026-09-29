package murach.email;

import jakarta.mail.MessagingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MailUtilRender {

    public static void sendMail(String to, String from,
                                String subject, String body, boolean bodyIsHTML)
            throws MessagingException {

        try {
            // 1. Đọc API Key từ Biến môi trường trên Render
            String apiKey = System.getenv("BREVO_API_KEY");

            if (apiKey == null || apiKey.trim().isEmpty()) {
                throw new MessagingException("Chưa cấu hình biến môi trường BREVO_API_KEY trên Render!");
            }

            // 2. Escape triệt để các ký tự đặc biệt cho JSON
            String cleanFrom = from.trim();
            String cleanTo = to.trim();
            String cleanSubject = escapeJson(subject);
            String cleanBody = escapeJson(body);

            // 3. Đóng gói dữ liệu dạng JSON
            String jsonPayload = "{"
                    + "\"sender\":{\"email\":\"" + cleanFrom + "\"},"
                    + "\"to\":[{\"email\":\"" + cleanTo + "\"}],"
                    + "\"subject\":\"" + cleanSubject + "\","
                    + (bodyIsHTML ? "\"htmlContent\":\"" : "\"textContent\":\"") + cleanBody + "\""
                    + "}";

            // 4. Gọi API Brevo qua HTTPS
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", apiKey)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // 5. Gửi request và kiểm tra phản hồi
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                // In log chi tiết phản hồi từ Brevo ra Render Log để dễ theo dõi nếu có lỗi
                System.err.println("Brevo Error Response: " + response.body());
                throw new MessagingException("Lỗi Brevo API (Mã " + response.statusCode() + "): " + response.body());
            }

        } catch (Exception e) {
            throw new MessagingException("Không thể gửi mail qua HTTP API: " + e.getMessage(), e);
        }
    }

    // Hàm hỗ trợ escape ký tự đặc biệt chuẩn JSON
    private static String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}