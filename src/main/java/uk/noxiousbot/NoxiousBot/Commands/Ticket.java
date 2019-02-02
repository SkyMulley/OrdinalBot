package uk.noxiousbot.NoxiousBot.Commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ICategory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.EnumSet;

public class Ticket extends CommandCore {
    public Ticket () {
        commandName = "Ticket";
        helpMessage = "Create a ticket to talk directly with staff";
        Usage = "?ticket";
    }
    @Override
    public boolean executeCommand(MessageReceivedEvent event, String[] argArray) {
        ICategory category = event.getGuild().getCategoryByID(541249569015726101L);
        IChannel channel = event.getGuild().createChannel("ticket-"+event.getGuild().getChannels().size());
        channel.changeCategory(category);
        EnumSet<Permissions> permissions = EnumSet.of(Permissions.READ_MESSAGES, Permissions.SEND_MESSAGES, Permissions.MANAGE_CHANNEL);
        channel.overrideRolePermissions(event.getGuild().getEveryoneRole(),null,permissions);
        channel.overrideUserPermissions(event.getAuthor(),permissions,null);
        channel.overrideRolePermissions(event.getGuild().getRoleByID(539570948924375041L),permissions,null);
        EmbedBuilder builder = new EmbedBuilder();
        builder.withAuthorName("Ticket System");
        builder.appendField("Welcome to your ticket","As default all support members have been added. You have full control of this channel and may add anyone as required. However if you abuse this right it will be taken away from you",false);
        RequestBuffer.request(() -> channel.sendMessage(builder.build()));
        EmbedBuilder builder1 = new EmbedBuilder();
        builder1.withAuthorName("Ticket Created");
        builder1.withDescription("Ticket can be found under "+channel.mention());
        RequestBuffer.request(() -> event.getChannel().sendMessage(builder1.build()));
        return true;
    }
}
