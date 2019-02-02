package uk.noxiousbot.NoxiousBot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class Main {
    public static void main(String[] args) {
        String token = "NTMwNzY4ODQ5NTk1NzkzNDE5.Dzcuew.WTJtV2TCLjbaTAeDzwZIxWsY8oI";
        IDiscordClient cli = getBuiltDiscordClient(token);
        cli.login();
        ServerLink sl = new ServerLink(cli.getChannelByID(519601289626124299L));
    }

    public static IDiscordClient getBuiltDiscordClient(String token) {
        return new ClientBuilder()
                .withToken(token)
                .build();
    }
}
