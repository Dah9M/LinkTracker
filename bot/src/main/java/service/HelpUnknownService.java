package service;


import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static service.MessageName.*;

@Slf4j
@Service
public class HelpUnknownService {

    private final SendMessageInterface sendMessageService;

    public HelpUnknownService(@Autowired SendMessageInterface sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    public void sendNoText(String chatId) throws TelegramApiException {
        log.info("Отправка ответа на непонятный текст пользователю с chatId = {}", chatId);
        sendMessageService.sendMessage(chatId, NO_MESSAGE.getMessageName());
    }

    public void sendUnknownText(String chatId) throws TelegramApiException {
        log.info("Отправка ответа на неизвестную команду пользователю с chatId = {}", chatId);
        sendMessageService.sendMessage(chatId, UNTRACK_MESSAGE.getMessageName());
        sendMessageService.sendMessage(chatId, UNKNOWN_MESSAGE.getMessageName());

    }

    public void sendHelpText(String chatId) throws TelegramApiException {
        log.info("Отправка справки пользователю с chatId = {}", chatId);
        sendMessageService.sendMessage(chatId, HELP_MESSAGE.getMessageName());
    }
}
