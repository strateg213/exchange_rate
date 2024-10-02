import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInputHandler {

    private final Scanner scanner;

    // Конструктор
    public UserInputHandler() {
        scanner = new Scanner(System.in);
    }

    // Метод для получения валюты от пользователя
    public String getCurrencyCode() {
        String currencyCode = null;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Введите валюту (USD, EUR, CNY): ");
                currencyCode = scanner.next().toUpperCase();

                // Проверка, является ли введенная строка одной из ожидаемых валют
                if (currencyCode.equals("USD") || currencyCode.equals("EUR") || currencyCode.equals("CNY")) {
                    validInput = true;  // Устанавливаем флаг для завершения цикла
                } else {
                    System.out.println("Ошибка: введена некорректная валюта. Попробуйте снова.");
                }
            } catch (final Exception e) {
                System.out.println("Ошибка при вводе. Попробуйте снова.");
                scanner.next();  // Очищаем некорректный ввод
            }
        }

        return currencyCode;
    }

    // Метод для получения количества дней
    public int getDaysBack() {
        int daysBack = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Введите количество дней назад от текущей даты: ");
                daysBack = scanner.nextInt();

                // Проверка, что введенное значение положительное
                if (daysBack > 0) {
                    validInput = true;  // Устанавливаем флаг для завершения цикла
                } else {
                    System.out.println("Ошибка: количество дней должно быть положительным. Попробуйте снова.");
                }
            } catch (final InputMismatchException e) {
                System.out.println("Ошибка: необходимо ввести целое число. Попробуйте снова.");
                scanner.next();  // Очищаем некорректный ввод
            }
        }

        return daysBack;
    }

    // Закрываем сканнер, когда он больше не нужен
    public void close() {
        scanner.close();
    }
}