package uk.noxiousbot.NoxiousBot.Commands;


import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class Help extends CommandCore {

    private List<CommandCore> GC;

    public Help(List<CommandCore> GC) {

        commandName = "Help";

        helpMessage = "See all the commands in place with the bot";

        Usage = "?Help";

        this.GC = GC;

    }



    @Override

    public boolean executeCommand(MessageReceivedEvent event, String[] argArray) {

        try {

            EmbedBuilder builder = new EmbedBuilder();

            builder.withAuthorName("Help Menu");

            builder.withColor(0,0,255);

            builder.withAuthorIcon(event.getClient().getOurUser().getAvatarURL());

            for(CommandCore GG : GC) {

                builder.appendField(""+GG.getCommandName(),"Description: "+GG.getHelpMessage()+"\nUsage: "+GG.getUsage(),false);

            }

            RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(builder.build()));

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            event.getChannel().sendMessage("Sorry, a problem went wrong our side! "+event.getGuild().getUserByID(217335916455395329L).mention());

            return true;

        }

    }

}