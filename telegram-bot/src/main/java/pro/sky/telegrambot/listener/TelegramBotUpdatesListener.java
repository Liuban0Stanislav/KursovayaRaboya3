package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.hibernate.type.LocalDateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.DateTimeParseException;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.BotListenerRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
            String messageText = update.message().text();   //get text of message
            long chatId = update.message().chat().id();     //get id of chat
            if (messageText.equals("/start")) {        //if bot get a message "/start" than it send back message "Hello"
                SendResponse response = telegramBot.execute(new SendMessage(chatId, "Hello!"));
            }
            //make pattern with regular expression
            Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
            //processing notifications with date and time
            Matcher matcher = pattern.matcher(messageText);                 //to compare string and regular expression
            System.out.println("matcher = " + matcher);
            CharSequence dateTime = null;
            if (matcher.find()) {           //find() looking first coincidence in the string and return true or false
                dateTime = matcher.group(1);                    /*returns a substring from the first occurrence of
                                                                    the specified group of characters in the string*/
                System.out.println("messageText = " + messageText);
                System.out.println("chatId = " + chatId);
                System.out.println("dateTime = " + dateTime);
            }
            if (dateTime != null) {
                LocalDateTime localDateTime = LocalDateTime.parse(
                        dateTime,
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                );                                          //putting charSequence object into a LocalDateTime variable
                NotificationTask notificationTask = new NotificationTask(
                        1l,
                        chatId,
                        messageText,
                        localDateTime
                );                                            //make notificationTask object
                botListenerRepository.save(notificationTask);//saving the object into a database
            }


        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void scheduledMessage() {
        logger.info("scheduleMessage() starts");

        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        logger.info("currentDateTime is installed = {}", currentDateTime);

        List<NotificationTask> notificationTasks = botListenerRepository.
                findByScheduleDateTime(currentDateTime);
        notificationTasks.stream()
                .forEach(notificationTask -> {
                    SendResponse responseMessage = telegramBot.execute(new SendMessage(
                            notificationTask.getChatId(),
                            notificationTask.getTextMessage()));
                    if (responseMessage.isOk()) {
                        logger.info("responseMessage = OK");
                    } else {
                        logger.info("responseMessage = BAD");
                    }
                });

    }
}
