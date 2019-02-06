package mulley.sky.OrdinalBot.Commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class Info extends CommandCore{
    public Info () {
        commandName = "Info";
        helpViewable = false;
    }

    @Override
    public boolean executeCommand(MessageReceivedEvent event, String[] argArray) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withAuthorName("Information");
        builder.withDescription("This bot was made for Gladius Gaming as a little fun thing, the whole point is to become the highest ranking, the ordinal" +
                ". Becoming the ordinal basically plasters your name everywhere but who doesn't want that. Gain coins by playing minigames, talking in text and voice channels," +
                "and also duelling other players for their coins. For more information use the ?help command");
        builder.withFooterText("Made by Sky#2134");
        builder.withImage(event.getClient().getOurUser().getAvatarURL());
        RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(builder.build()));
        return true;
    }
}
