package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.model.botcommandscope.BotCommandScope;
import com.pengrad.telegrambot.model.botcommandscope.BotCommandScopeDefault;
import com.pengrad.telegrambot.request.SetMyCommands;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;

import java.util.ArrayList;
import java.util.List;

@Data //creating constructors, getters, setters, toString, equals, hashCode
@Configuration
public class TelegramBotConfiguration {
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Value("${telegram.bot.token}")
    private String token;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token); //creating telegram bot
        bot.execute(new DeleteMyCommands()); //cleaning all commands of bot to reboot commands list and add new ones
        return bot;
    }

}
