public enum Currency {
    USD(431),
    EUR(451),
    RUB(456);

    private final int id;

    // Конструктор для enum, принимающий ID валюты
    Currency(final int id) {
        this.id = id;
    }

    // Метод для получения ID валюты
    public int getId() {
        return id;
    }
}
