package uk.noxiousbot.NoxiousBot.Commands;


import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.concurrent.TimeUnit;

public class Suggest extends CommandCore {

    private static Emoji cross = EmojiManager.getForAlias("negative_squared_cross_mark");

    private static Emoji tick = EmojiManager.getForAlias("white_check_mark");

    public Suggest() {
        commandName = "Suggest";
        helpMessage = "Suggest an addition for the community!";
        Usage = "?suggest <Suggestion>";
    }



    @Override

    public boolean executeCommand(MessageReceivedEvent event, String[] argArray) {
        try {
            if(argArray.length >= 2) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.withAuthorIcon(event.getAuthor().getAvatarURL());
                builder.withAuthorName("Suggestion by "+event.getAuthor().getName());
                builder.withDescription(event.getMessage().getContent().substring(9));
                builder.withColor(0,0,255);
                IMessage message = event.getGuild().getChannelByID(510489556462862378L).sendMessage(builder.build());
                message.addReaction(tick);
                TimeUnit.SECONDS.sleep(1);
                message.addReaction(cross);
                event.getChannel().sendMessage("Thanks for the suggestion!");
                return true;
            } else {
                argsNotFound(event);
                return true;
            }
        } catch(Exception e) {
            event.getChannel().sendMessage("Sorry, a problem went wrong our side! "+event.getGuild().getUserByID(217335916455395329L).mention());
            e.printStackTrace();
            return true;
        }
    }



    public void argsNotFound(MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withDescription("You haven't supplied enough args in your request. Check with !help if you are unsure of the usage of this command.");
        builder.withFooterText("Not enough args");
        RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(builder.build()));
    }
}