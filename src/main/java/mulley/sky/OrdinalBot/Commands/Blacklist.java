package mulley.sky.OrdinalBot.Commands;

import mulley.sky.OrdinalBot.PlayerManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class Blacklist extends CommandCore {
    private PlayerManager pm;
    private IUser user;
    public Blacklist (PlayerManager pm) {
        commandName = "Blacklist";
        helpViewable = false;
        this.pm = pm;
    }
    @Override
    public boolean executeCommand(MessageReceivedEvent event, String[] argArray) {
        if(event.getAuthor().hasRole(event.getGuild().getRoleByID(468527128049090575L))) {
            if(argArray.length == 2) {
                if(argArray[1].contains("@")) {
                    long id = Long.parseLong(argArray[1].replaceAll("\\D+",""));
                    user = event.getClient().getUserByID(id);
                } else {
                    List<IUser> users = event.getGuild().getUsersByName(argArray[1]);
                    if(users.size()!= 1) {
                        event.getChannel().sendMessage("Either your selected user doesn't exist or there are multiple results, please refine your search. (Please note my developer is retarded so it is caps sensitive)");
                        return false;
                    } else {
                        user = users.get(0);
                    }
                }
            } else {
                argsNotFound(event);
            }
            if(pm.isBlacklisted(user)) {
                pm.unblacklistPlayer(user);
                event.getChannel().sendMessage("Player has been removed from the blacklist and may use the bot.");
            } else {
                pm.blacklistPlayer(user);
                event.getChannel().sendMessage("Player has been added to the blacklist and will be completely ignored by the bot.");
            }
        } else {
            event.getChannel().sendMessage("This is an admin only command, get out of here.");
        }
        return true;
    }

    public void argsNotFound(MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withDescription("You haven't supplied enough args in your request. Check with ?help if you are unsure of the usage of this command.");
        builder.withFooterText("Not enough args");
        RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(builder.build()));
    }
}
