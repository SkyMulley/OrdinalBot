package mulley.sky.OrdinalBot.Commands;

import mulley.sky.OrdinalBot.Games.AIShitFest;
import mulley.sky.OrdinalBot.Games.LowerHigher;
import mulley.sky.OrdinalBot.PlayerManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class TwentyOne extends CommandCore {
    private PlayerManager pm;
    public TwentyOne (PlayerManager pm) {
        this.pm = pm;
        commandName = "TwentyOne";
        helpMessage = "Play against the bot, draw numbers to get closer to 21! (25 coins to enter)";
        Usage = "?twentyone";
    }

    @Override
    public boolean executeCommand(MessageReceivedEvent event, String[] argArray) {
        if(pm.getCoins(event.getAuthor()) < 25) {
            notEnoughCoins(event);
            return true;
        }
        event.getClient().getDispatcher().registerListener(new AIShitFest(event,pm));
        return true;
    }

    public void notEnoughCoins(MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withDescription("You do not have enough coins for this transaction.");
        builder.withFooterText("Not enough coins");
        RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(builder.build()));
    }
}
