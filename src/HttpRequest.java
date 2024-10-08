import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class HttpRequest {

    // Метод для выполнения HTTP GET запроса
    public String sendGetRequest(final String uriString) throws Exception {
        // Создание URI объекта из строки
        final URI uri = new URI(uriString);

        // Преобразование URI в URL для создания соединения
        final URL url = uri.toURL();

        // Открытие соединения
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Чтение ответа от сервера
        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuilder response = new StringBuilder();
        String inputLine;

        // Чтение по строкам
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Возвращаем результат в виде строки
        return response.toString();
    }
}
