package mulley.sky.OrdinalBot.Commands;


import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class Help extends CommandCore {

    private List<CommandCore> GC;

    public Help(List<CommandCore> GC) {

        commandName = "Help";

        helpViewable = false;

        this.GC = GC;

    }



    @Override

    public boolean executeCommand(MessageReceivedEvent event, String[] argArray) {

        try {

            EmbedBuilder builder = new EmbedBuilder();

            builder.withAuthorName("Help Menu");

            builder.withColor(255,255,255);

            builder.withAuthorIcon(event.getClient().getOurUser().getAvatarURL());

            String message = "";

            for(CommandCore GG : GC) {
                if(GG.isHelpViewable()) {
                    message = message + "\n**"+GG.getCommandName()+" - **"+GG.getHelpMessage()+"\nUsage: "+GG.getUsage();
                }
            }
            builder.appendField("Commands",message,false);
            RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(builder.build()));

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            event.getChannel().sendMessage("Sorry, a problem went wrong our side! "+event.getGuild().getUserByID(217335916455395329L).mention());

            return true;

        }

    }

}