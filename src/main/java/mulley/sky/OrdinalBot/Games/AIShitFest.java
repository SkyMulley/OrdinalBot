package mulley.sky.OrdinalBot.Games;

import mulley.sky.OrdinalBot.PlayerManager;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Message;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.Random;

public class AIShitFest {
    private MessageReceivedEvent mainevent;
    private PlayerManager pm;
    private IMessage message;
    private Random rand = new Random();
    private int pNumber = 0;
    private int cNumber = 0;
    private int iterations = 1;
    public AIShitFest(MessageReceivedEvent mainevent, PlayerManager pm) {
        this.mainevent = mainevent;
        this.pm = pm;
        pm.loseCoins(mainevent.getAuthor(),25);
        mainevent.getChannel().sendMessage("Bet has been collected, let's get going!");
        EmbedBuilder builder = new EmbedBuilder();
        builder.withAuthorName("TwentyOne");
        builder.withFooterText(mainevent.getAuthor().getName()+"'s game");
        pNumber = pNumber + rand.nextInt(12) + 1;
        cNumber = cNumber + rand.nextInt(12) + 1;
        builder.appendField("Current Number: "+pNumber,"Say `hit` to increase your number by a random amount, or `miss` to skip",false);
        RequestBuffer.request(() -> message = mainevent.getChannel().sendMessage(builder.build()));
    }

    @EventSubscriber
    public void onMessageRecieved(MessageReceivedEvent event) {
        if (event.getAuthor().equals(mainevent.getAuthor())) {
            if (event.getMessage().getContent().equals("hit") || event.getMessage().getContent().equals("miss")) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.withAuthorName("TwentyOne");
                builder.withFooterText(mainevent.getAuthor().getName() + "'s game | "+(iterations+1)+"/5 turns used");
                if (cNumber < 16) {
                    if (rand.nextInt(5) == 0) {
                        builder.withDescription("Bot has missed");
                    } else {
                        cNumber = cNumber + rand.nextInt(12) + 1;
                        builder.withDescription("Bot has hit");
                    }
                } else {
                    builder.withDescription("Bot has missed");
                }
                if(cNumber > 21) {
                    builder.appendField("Current Number: "+pNumber,"Bot has gone bust, you win!",false);
                    builder.withColor(0,255,0);
                    RequestBuffer.request(() -> message.edit(builder.build()));
                    event.getClient().getDispatcher().unregisterListener(this);
                }
                if (cNumber <= 21) {
                    if (event.getMessage().getContent().equals("hit")) {
                        pNumber = pNumber + rand.nextInt(12) + 1;
                        if (pNumber > 21) {
                            builder.appendField("Current Number: " + pNumber, "You've gone bust! Bot wins!", false);
                            builder.withColor(255, 0, 0);
                            RequestBuffer.request(() -> message.edit(builder.build()));
                            event.getClient().getDispatcher().unregisterListener(this);
                            iterations = 6;
                        }
                    }
                }
                iterations = iterations + 1;
                if(iterations==5) {
                    if(cNumber>pNumber) {
                        builder.withDescription("Bot's Final Number: "+cNumber);
                        builder.appendField("Final Number: "+pNumber,"Bot is closer to 21 than you, you lose!",false);
                        builder.withColor(255,0,0);
                    }
                    if(cNumber<pNumber) {
                        builder.withDescription("Bot's Final Number: "+cNumber);
                        builder.appendField("Final Number: "+pNumber,"You are closer to 21 than the bot, you win!",false);
                        builder.withColor(0,255,0);
                        pm.addCoins(event.getAuthor(),50);
                    }
                    if(cNumber==pNumber) {
                        builder.withDescription("Bot's Final Number: "+cNumber);
                        builder.appendField("Final Number: "+pNumber,"You both got the same number, it's a draw!",false);
                        pm.addCoins(event.getAuthor(),25);
                    }
                    RequestBuffer.request(() -> message.edit(builder.build()));
                    event.getClient().getDispatcher().unregisterListener(this);
                } else {
                    builder.appendField("Current Number: " + pNumber, "Say `hit` to increase your number by a random amount, or `miss` to skip", false);
                    RequestBuffer.request(() -> message.edit(builder.build()));
                }
                event.getMessage().delete();
            }
        }
    }
}
