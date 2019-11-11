package mulley.sky.OrdinalBot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import mulley.sky.OrdinalBot.Commands.MessageListener;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String token = "*bot token removed*";
        IDiscordClient cli = getBuiltDiscordClient(token);
        cli.getDispatcher().registerListener(new MessageListener(cli));
        cli.login();
        try {TimeUnit.SECONDS.sleep(10);}catch (Exception e) {}
        ServerLink sl = new ServerLink(cli.getChannelByID(470673417583067166L));
    }

    public static IDiscordClient getBuiltDiscordClient(String token) {
        return new ClientBuilder()
                .withToken(token)
                .build();
    }
}
