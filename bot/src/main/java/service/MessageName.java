package service;

import lombok.Getter;

@Getter
public enum MessageName {
    START_MESSAGE("Привет. Я LinkTracker Bot. Я помогу тебе быть в курсе последних " +
            "обновлений тех ресурсов, которые тебе интересны."),

    HELP_MESSAGE("/start - регистрация пользователя.\n" +
            "/help - вывод списка доступных команд.\n" +
            "/track - начать отслеживание ссылки.\n" +
            "/untrack - прекратить отслеживание ссылки.\n" +
            "/list - показать список отслеживаемых ссылок (cписок ссылок, полученных при /track)"),

    NO_MESSAGE("Я поддерживаю команды, начинающиеся со слеша(/).\n"
            + "Чтобы посмотреть список команд введите /help"),

    UNKNOWN_MESSAGE("Не понимаю вас, напишите /help чтобы узнать что я понимаю."),
    TRACK_MESSAGE("Начал отслеживание этого ресурса."),
    UNTRACK_MESSAGE("Деактивировал отслеживание этого ресурса."),
    DUPLICATE_MESSAGE("Вы уже подписаны на этот ресурс."),
    NO_SUBS_MESSAGE("У вас нет подписок"),
    NO_SUCH_URL("У вас нет такой подписки"),
    NO_URL_MESSAGE("Неправильное использование команды. /command URL"),
    NO_VALID_URL("Вы ввели неправильный URL. Попробуйте ещё раз"),
    LIST_MESSAGE("Отслеживаемые ресурсы: "),
    ADD_USER("Пользователь успешно добавлен"),
    ALREADY_EXIST("Пользователь уже существует");

    private final String messageName;

    MessageName(String messageName) {
        this.messageName = messageName;
    }

}
