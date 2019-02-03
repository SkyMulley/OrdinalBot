package uk.noxiousbot.NoxiousBot.Games;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Embed;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import uk.noxiousbot.NoxiousBot.PlayerManager;

import java.util.HashMap;
import java.util.Random;

public class LowerHigher {
    private PlayerManager pm;
    private Integer bet;
    private MessageReceivedEvent mainevent;
    private IMessage message;
    private Random rand = new Random();
    private int number;
    private EmbedBuilder builder = new EmbedBuilder();
    public LowerHigher(PlayerManager pm, Integer bet, MessageReceivedEvent mainevent) {
        this.pm = pm;
        this.bet = bet;
        this.mainevent = mainevent;
        builder.withAuthorName("Higher/Lower");
        builder.withFooterText(mainevent.getAuthor().getName()+"'s game | Bet: "+bet);
        pm.loseCoins(mainevent.getAuthor().getLongID(),bet);
        mainevent.getChannel().sendMessage("Bet has been collected, let's get going!");
        start();
    }

    private void start() {
        number = rand.nextInt(10);
        builder.appendField("Will the next number I pick be higher or lower than this one?","Number: "+number,false);
        RequestBuffer.request(() -> message = mainevent.getChannel().sendMessage(builder.build()));
    }

    @EventSubscriber
    public void higherlower(MessageReceivedEvent event) {
        if(event.getAuthor().equals(mainevent.getAuthor())) {
            if(event.getMessage().getContent().toLowerCase().equals("higher")) {
                int newnumber = rand.nextInt(10);
                if(newnumber > number) {
                    int coins = (int)(bet*(30.0f/100.0f));
                    Math.ceil(coins);
                    builder.appendField("New Number: "+newnumber, "You win! Adding "+coins+" to your balance!",false);
                    pm.addCoins(event.getAuthor().getLongID(),coins+bet);
                    builder.withColor(0,255,0);
                } if(newnumber < number) {
                    builder.appendField("New Number: "+newnumber,"Too bad... You lost the bet",false);
                    builder.withColor(255,0,0);
                } if(newnumber == number) {
                    builder.appendField("New Number: "+newnumber, "Draw! Both numbers were the same, refunding your coins",false);
                    pm.addCoins(event.getAuthor().getLongID(),bet);
                    builder.withColor(0,0,255);
                }
                RequestBuffer.request(() -> message.edit(builder.build()));
                event.getClient().getDispatcher().unregisterListener(this);
            }
            if(event.getMessage().getContent().toLowerCase().equals("lower")) {
                int newnumber = rand.nextInt(10);
                if(newnumber < number) {
                    int coins = (int)(bet*(30.0f/100.0f));
                    Math.ceil(coins);
                    builder.appendField("New Number: "+newnumber, "You win! Adding "+coins+" to your balance!",false);
                    pm.addCoins(event.getAuthor().getLongID(),coins+bet);
                    builder.withColor(0,255,0);
                } if(newnumber > number) {
                    builder.appendField("New Number: "+newnumber,"Too bad... You lost the bet",false);
                    builder.withColor(255,0,0);
                } if(newnumber == number) {
                    builder.appendField("New Number: "+newnumber, "Draw! Both numbers were the same, refunding your coins",false);
                    pm.addCoins(event.getAuthor().getLongID(),bet);
                    builder.withColor(0,0,255);
                }
                RequestBuffer.request(() -> message.edit(builder.build()));
                event.getClient().getDispatcher().unregisterListener(this);
            }
        }
    }
}
