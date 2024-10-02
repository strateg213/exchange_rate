import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class DynamicCurrencyRatesFetcherGson {

    public static void main(final String[] args) throws Exception {
        final UserInputHandler inputHandler = new UserInputHandler();

        // Получение кода валюты и количества дней от пользователя
        final String currencyCode = inputHandler.getCurrencyCode();
        final int daysBack = inputHandler.getDaysBack();

        final Currency currency = Currency.valueOf(currencyCode);

        // Получение текущей даты и расчет начала и конца периода
        final LocalDate endDate = LocalDate.now();  // Сегодняшняя дата
        final LocalDate startDate = endDate.minusDays(daysBack - 1);  // Дата начала периода

        // Форматирование дат в формат YYYY-MM-DD для API
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final String startDateString = startDate.format(formatter);
        final String endDateString = endDate.format(formatter);

        // Вывод данных для выбранной валюты
        final List<Rate> rates = fetchAndPrintRatesForCurrency(currency.getId(), startDateString, endDateString);
        // Подсчет среднего курса
        final double averageRate = calculateAverageRate(rates);
        // Вывод данных о курсах
        for (final Rate rate : rates) {
            final String formattedDate = rate.Date.substring(0, 10);
            System.out.println(formattedDate + " | " + rate.Cur_OfficialRate);
        }
        System.out.printf("Средний курс: %.4f%n", averageRate);
    }

    // Метод для получения и вывода курсов валют
    private static List<Rate> fetchAndPrintRatesForCurrency(final int currencyId,
                                                            final String startDate,
                                                            final String endDate) throws Exception {
        // Формирование URL для API запроса
        final String urlString = "https://api.nbrb.by/ExRates/Rates/Dynamics/" + currencyId
                                 + "?startDate=" + startDate + "&endDate=" + endDate;

        // Выполнение запроса к API и получение JSON ответа
        final String jsonResponse = getJsonResponse(urlString);

        // Парсинг JSON ответа в список объектов Rate с использованием Gson
        final Gson gson = new Gson();
        return gson.fromJson(jsonResponse, new TypeToken<List<Rate>>() {
        }.getType());
    }

    // Метод для выполнения GET запроса и получения ответа в виде строки
    private static String getJsonResponse(final String urlString) throws Exception {
        final URL url = new URL(urlString);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    // Метод для расчета среднего курса валюты
    private static double calculateAverageRate(final List<Rate> rates) {
        double sum = 0.0;
        for (final Rate rate : rates) {
            sum += rate.Cur_OfficialRate;  // Суммируем все курсы
        }
        return sum / rates.size();  // Делим на количество дней
    }
}