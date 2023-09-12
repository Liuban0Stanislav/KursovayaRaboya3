package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.BotListenerRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private BotListenerRepository botListenerRepository;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            String messageText = update.message().text();
            if (messageText.equals("/start")) {
                long chatId = update.message().chat().id();
                SendResponse response = telegramBot.execute(new SendMessage(chatId, "Hello!"));
            }

            NotificationTask notificationTask = new NotificationTask();
            Long chatId = update.message().chat().id();
            notificationTask.setChatId(chatId);
            notificationTask.setTextMessage(messageText);
            Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
            Matcher matcher = pattern.matcher(messageText);
            CharSequence dateTime = null;
//            String text = null;
            if (matcher.matches()) {
                dateTime = matcher.group(1);
//                text = matcher.group(2);
            }

            LocalDateTime localDateTime = LocalDateTime.parse(dateTime,
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

            notificationTask.setScheduleDateTime(localDateTime);
            botListenerRepository.save(notificationTask);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
