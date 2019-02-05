package mulley.sky.OrdinalBot;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;

public class Main {
    public static void main(String[] args) {
        DiscordClientBuilder builder = new DiscordClientBuilder("");
        DiscordClient cli = builder.build();
        cli.login();
        cli.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    ServerLink sl = new ServerLink((TextChannel) cli.getChannelById(Snowflake.of(470673417583067166L)).block());
                    System.out.println("Bot is online, logged in as "+event.getSelf().getUsername()+event.getSelf().getDiscriminator()+" and server link should be setup");
                });
    }
}
