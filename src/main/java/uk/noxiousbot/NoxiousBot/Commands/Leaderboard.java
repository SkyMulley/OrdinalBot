package uk.noxiousbot.NoxiousBot.Commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class Leaderboard extends CommandCore {
    private HashMap<Long,Integer> players;
    public Leaderboard(HashMap<Long, Integer> players) {
        commandName = "Leaderboard";
        helpMessage = "View the top 10 people on the leaderboard";
        Usage = "?leaderboard";
        this.players = players;
    }

    @Override
    public boolean executeCommand(MessageReceivedEvent event, String[] argArray) {
        HashMap<Long,Integer> sorted = players
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        EmbedBuilder builder = new EmbedBuilder();
        builder.withAuthorName("Leaderboard");
        String players = "";
        for(int i = 0;i < 10; ++i) {
            Map.Entry<Long,Integer> entry = sorted.entrySet().iterator().next();
            int num = i + 1;
            players = players + "\n" + num +". " + event.getClient().getUserByID(entry.getKey()) + " - " + entry.getValue();
            sorted.remove(entry.getKey());
        }
        builder.appendField("Top Ten Users",players,false);
        RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(builder.build()));
        return true;
    }
}
