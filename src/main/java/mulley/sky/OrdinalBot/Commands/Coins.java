package mulley.sky.OrdinalBot.Commands;

import com.koloboke.collect.impl.hash.Hash;
import mulley.sky.OrdinalBot.PlayerManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.HashMap;
import java.util.List;

public class Coins extends CommandCore {
    private PlayerManager players;
    private IUser user;
    public Coins(PlayerManager players) {
        commandName = "Coins";
        helpMessage = "View the amount of coins you or another person have";
        Usage = "?coins [Player]";
        this.players = players;
    }

    @Override
    public boolean executeCommand(MessageReceivedEvent event, String[] argArray) {
        if(argArray.length == 2) {
            if(argArray[1].contains("@")) {
                long id = Long.parseLong(argArray[1].replaceAll("\\D+",""));
                user = event.getClient().getUserByID(id);
            } else {
                List<IUser> users = event.getClient().getUsersByName(argArray[1]);
                if(users.size()!= 1) {
                    event.getChannel().sendMessage("Either your selected user doesn't exist or there are multiple results, please refine your search. (Please note my developer is retarded so it is caps sensitive)");
                    return false;
                } else {
                    user = users.get(0);
                }
            }
        } else {
            user = event.getAuthor();
        }
        if(user.isBot()) {
            event.getChannel().sendMessage("You're trying to get a bots coins, bots don't have rights and can't get the lovely coins you do. Sorry about that");
            return true;
        } else {
            EmbedBuilder builder = new EmbedBuilder();
            builder.withAuthorName(user.getName() + "'s coins");
            builder.withDescription(""+players.getCoins(user));
            RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(builder.build()));
            return true;
        }
    }
}
