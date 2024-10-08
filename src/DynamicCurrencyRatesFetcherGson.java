import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class DynamicCurrencyRatesFetcherGson {

    public static void main(final String[] args) throws Exception {
        // Создаем объект для работы с вводом пользователя
        final UserInputHandler inputHandler = new UserInputHandler();
        // Получение кода валюты и количества дней от пользователя
        final String currencyCode = inputHandler.getCurrencyCode();
        final int daysBack = inputHandler.getDaysBack();
        // Закрываем сканнер после использования
        inputHandler.close();
        // Преобразование кода валюты в enum
        final Currency currency = Currency.valueOf(currencyCode);
        // Получение текущей даты и расчет начала и конца периода
        final LocalDate endDate = LocalDate.now();  // Сегодняшняя дата
        final LocalDate startDate = endDate.minusDays(daysBack - 1);  // Дата начала периода
        // Форматирование дат в формат YYYY-MM-DD для API
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final String startDateString = startDate.format(formatter);
        final String endDateString = endDate.format(formatter);
        // Создаем объект для выполнения HTTP запросов
        final HttpRequest httpRequest = new HttpRequest();
        // Формирование URL для запроса
        final String urlString = "https://api.nbrb.by/ExRates/Rates/Dynamics/" + currency.getId()
                                 + "?startDate=" + startDateString + "&endDate=" + endDateString;
        // Получаем данные о курсах валют
        final String jsonResponse = httpRequest.sendGetRequest(urlString);
        // Парсинг JSON ответа
        final List<Rate> rates = parseRatesFromJson(jsonResponse);
        // Подсчет среднего курса
        final double averageRate = calculateAverageRate(rates);
        // Вывод курса для выбранной валюты
        for (final Rate rate : rates) {
            final String formattedDate = rate.Date.substring(0, 10);
            System.out.println(formattedDate + " | " + rate.Cur_OfficialRate);
        }
        System.out.printf("Средний курс: %s%n", String.format("%.4f", averageRate).replace(',', '.'));
    }

    // Метод для парсинга JSON в список объектов Rate
    private static List<Rate> parseRatesFromJson(final String jsonResponse) {
        final Gson gson = new Gson();
        return gson.fromJson(jsonResponse, new TypeToken<List<Rate>>() {
        }.getType());
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