package uk.noxiousbot.NoxiousBot.Commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import uk.noxiousbot.NoxiousBot.Games.LowerHigher;
import uk.noxiousbot.NoxiousBot.Games.Roulette;
import uk.noxiousbot.NoxiousBot.PlayerManager;

public class Slot extends CommandCore {
    private PlayerManager pm;
    public Slot (PlayerManager pm) {
        this.pm = pm;
        commandName = "Slot";
        helpMessage = "Take a bet and see how your roll with take out";
        Usage = "?slot <coins>";
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
        if(pm.getPlayers().get(event.getAuthor().getLongID()) < bet) {
            notEnoughCoins(event);
            return true;
        }
        Roulette roulette = new Roulette(pm,bet,event);
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
