package pro.sky.telegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue
    private Long notificationTaskId;
    private Long chatId;
    private String textMessage;
    private LocalDateTime scheduleDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(notificationTaskId, that.notificationTaskId) && Objects.equals(chatId, that.chatId) && Objects.equals(textMessage, that.textMessage) && Objects.equals(scheduleDateTime, that.scheduleDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationTaskId, chatId, textMessage, scheduleDateTime);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "notificationTaskId=" + notificationTaskId +
                ", chatId='" + chatId + '\'' +
                ", textMessage='" + textMessage + '\'' +
                ", scheduleDateTime=" + scheduleDateTime +
                '}';
    }
}
