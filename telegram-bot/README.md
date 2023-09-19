# My first telegram bot
### COURSE WORK #3

### Description
The telegram bot can react on a "/start" command.
Also, the bot use a database to save reminder
messages, which contains date and time at first,
and then text of reminding.
Once a minute a method starts which looking into 
the database reminders with suitable time, and
compare this time to a time from all reminders. 
If current time and some time from the database are
matching, then method make a message and sent it to user.

### Following technologies was used in this project
* com.github.pengrad
* org.projectlombok
* org.liquibase