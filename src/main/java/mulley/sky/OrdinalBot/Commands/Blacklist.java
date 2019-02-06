package mulley.sky.OrdinalBot.Commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import mulley.sky.OrdinalBot.PlayerManager;

import javax.sql.rowset.Predicate;
import java.util.List;

public class Blacklist extends CommandCore {
    private PlayerManager pm;
    private User user;
    public Blacklist (PlayerManager pm) {
        commandName = "Blacklist";
        helpViewable = false;
        this.pm = pm;
    }
    @Override
    public boolean executeCommand(MessageCreateEvent event, String[] argArray) {
        if(event.getMember().get().getRoles().collectList().block().contains(event.getGuild().block().getRoleById(Snowflake.of(468527128049090575L)).block())) {
            if(argArray.length == 2) {
                if(argArray[1].contains("@")) {
                    long id = Long.parseLong(argArray[1].replaceAll("\\D+",""));
                    user = event.getClient().getUserById(Snowflake.of(id)).block();
                } else {
                    //client.getGuildById(Snowflake.of(guildId)).flatMapMany(Guild::getMembers).filter(member -> member.getUsername().equals(name));
                    List<User> users = event.getClient().getUsers().filter(u -> u.getUsername().equals(argArray[1])).collectList().block();
                    if(users.size()!= 1) {
                        event.getMessage().getChannel().block().createMessage("Either your selected user doesn't exist or there are multiple results, please refine your search. (Please note my developer is retarded so it is caps sensitive)");
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
                event.getMessage().getChannel().block().createMessage("Player has been removed from the blacklist and may use the bot.");
            } else {
                pm.blacklistPlayer(user);
                event.getMessage().getChannel().block().createMessage("Player has been added to the blacklist and will be completely ignored by the bot.");
            }
        } else {
            event.getMessage().getChannel().block().createMessage("This is an admin only command, get out of here.");
        }
        return true;
    }

    public void argsNotFound(MessageCreateEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withDescription("You haven't supplied enough args in your request. Check with ?help if you are unsure of the usage of this command.");
        builder.withFooterText("Not enough args");
        RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(builder.build()));
    }
}
