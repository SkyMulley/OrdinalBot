package uk.noxiousbot.NoxiousBot.Games;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import uk.noxiousbot.NoxiousBot.PlayerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Roulette {
    private PlayerManager pm;
    private Integer bet;
    private MessageReceivedEvent mainevent;
    private IMessage message;
    private Random rand = new Random();
    private int number;
    private List<Emoji> emojis = new ArrayList<>();
    private EmbedBuilder builder = new EmbedBuilder();
    public Roulette(PlayerManager pm, Integer bet, MessageReceivedEvent mainevent) {
        this.pm = pm;
        this.bet = bet;
        this.mainevent = mainevent;
        builder.withAuthorName("Slot");
        builder.withFooterText(mainevent.getAuthor().getName()+"'s game | Bet: "+bet);
        pm.loseCoins(mainevent.getAuthor().getLongID(),bet);
        mainevent.getChannel().sendMessage("Bet has been collected, let's get going!");
        emojis.add(EmojiManager.getForAlias("lemon"));
        emojis.add(EmojiManager.getForAlias("chocolate_bar"));
        emojis.add(EmojiManager.getForAlias("heart"));
        emojis.add(EmojiManager.getForAlias("large_blue_diamond"));
        start();
    }

    private void start() {
        try {
            Emoji emoji1 = emojis.get(rand.nextInt(3 + 1));
            Emoji emoji2 = emojis.get(rand.nextInt(3 + 1));
            Emoji emoji3 = emojis.get(rand.nextInt(3 + 1));
            Emoji emoji4 = emojis.get(rand.nextInt(3 + 1));
            Emoji emoji5 = emojis.get(rand.nextInt(3 + 1));
            Emoji emoji6 = emojis.get(rand.nextInt(3 + 1));
            Emoji emoji7 = emojis.get(rand.nextInt(3 + 1));
            Emoji emoji8 = emojis.get(rand.nextInt(3 + 1));
            Emoji emoji9 = emojis.get(rand.nextInt(3 + 1));
            int moneyback = 0;
            if(emoji1.equals(emoji2) | emoji2.equals(emoji3) | emoji4.equals(emoji5) | emoji5.equals(emoji6) | emoji7.equals(emoji8) | emoji8.equals(emoji9)) {
                moneyback = moneyback + (int)(bet*(40.0f/100.0f));
            }
            if(emoji1.equals(emoji4) | emoji4.equals(emoji7) | emoji2.equals(emoji5) | emoji5.equals(emoji8) | emoji3.equals(emoji6) | emoji6.equals(emoji9)) {
                moneyback = moneyback + (int)(bet*(40.0f/100.0f));
            }
            if(emoji1.equals(emoji5) | emoji5.equals(emoji9) | emoji3.equals(emoji5) | emoji5.equals(emoji7)) {
                moneyback = moneyback + (int)(bet*(20.0f/100.0f));
            }
            if((emoji1.equals(emoji2) && emoji2.equals(emoji3)) | (emoji4.equals(emoji5) && emoji5.equals(emoji6)) | (emoji7.equals(emoji8) && emoji8.equals(emoji9))) {
                moneyback = moneyback + (int)(bet*(70.0f/100.0f));
            }
            if((emoji1.equals(emoji4) && emoji4.equals(emoji7))| (emoji2.equals(emoji5) && emoji5.equals(emoji8)| (emoji3.equals(emoji6) && emoji6.equals(emoji9)))) {
                moneyback = moneyback + (int)(bet*(70.0f/100.0f));
            }
            if((emoji1.equals(emoji5) && emoji5.equals(emoji9)| (emoji3.equals(emoji5) && emoji5.equals(emoji7)))) {
                moneyback = moneyback + (int)(bet*(70.0f/100.0f));
            }
            Math.ceil(moneyback);
            builder.withDescription(emoji1.getUnicode() + "|" + emoji2.getUnicode() + "|" + emoji3.getUnicode() + "\n" + emoji4.getUnicode() + "|" + emoji5.getUnicode() + "|" + emoji6.getUnicode() + "\n" + emoji7.getUnicode() + "|" + emoji8.getUnicode() + "|" + emoji9.getUnicode());
            builder.appendField("Lets see what you got!", "You bet in " + bet + " and got back " + moneyback, false);
            builder.withColor(255, 255, 255);
            RequestBuffer.request(() -> message = mainevent.getChannel().sendMessage(builder.build()));
            pm.addCoins(mainevent.getAuthor().getLongID(), moneyback);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
