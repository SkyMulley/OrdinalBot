package mulley.sky.OrdinalBot.Commands;

import mulley.sky.OrdinalBot.Games.LowerHigher;
import mulley.sky.OrdinalBot.PlayerManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

public class HigherLower extends CommandCore {
    private PlayerManager pm;
    public HigherLower (PlayerManager pm) {
        this.pm = pm;
        commandName = "HigherLower";
        helpMessage = "Exacly what it says on the tin, I pick a number, you guess whether the next one will be higher or lower";
        Usage = "?higherlower <coins>";
    }

    @Override
    public boolean executeCommand(MessageReceivedEvent event, String[] argArray) {
        int bet;
        if(argArray.length!=2) {
            argsNotFound(event);
            return true;
        }
        try {
            bet = Integer.parseInt(argArray[1]);
        } catch (Exception e) {
            argsNotFound(event);
            return true;
        }
        if(pm.getCoins(event.getAuthor()) < bet) {
            notEnoughCoins(event);
            return true;
        }
        event.getClient().getDispatcher().registerListener(new LowerHigher(pm,bet,event));
        return true;
    }

    public void argsNotFound(MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withDescription("You haven't supplied the right args in your request. Check with ?help if you are unsure of the usage of this command.");
        builder.withFooterText("Not enough args");
        RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(builder.build()));
    }

    public void notEnoughCoins(MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withDescription("You do not have enough coins for this transaction.");
        builder.withFooterText("Not enough coins");
        RequestBuffer.request(() -> event.getMessage().getChannel().sendMessage(builder.build()));
    }
}
