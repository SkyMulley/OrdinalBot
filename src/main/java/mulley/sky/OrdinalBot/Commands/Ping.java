package mulley.sky.OrdinalBot.Commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.rest.json.request.MessageEditRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Ping extends CommandCore {

    public Ping () {
        commandName = "Ping";
        helpMessage = "Find out the latency between the server and client";
        Usage = "?ping";
    }



    @Override

    public boolean executeCommand(MessageCreateEvent event, String[] argArray) {
        LocalDateTime sentTime = LocalDateTime.ofInstant(event.getMessage().getTimestamp(), ZoneId.systemDefault());
        Message probe = event.getMessage().getChannel().block().createMessage("Waiting for reply..").block();
        LocalDateTime repliedTime = LocalDateTime.ofInstant(probe.getTimestamp(), ZoneId.systemDefault());
        long ping = Duration.between(sentTime,repliedTime).toMillis();
        probe.edit(spec -> String.format("Pong! %s (%d ms)",event.getMember().get().getMention(),ping)).block();
        return true;
    }

}
