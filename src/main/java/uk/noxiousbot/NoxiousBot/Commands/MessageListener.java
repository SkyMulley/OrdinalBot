package uk.noxiousbot.NoxiousBot.Commands;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import uk.noxiousbot.NoxiousBot.Main;
import uk.noxiousbot.NoxiousBot.PlayerManager;
import uk.noxiousbot.NoxiousBot.VoiceListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MessageListener {
    private Main main;
    private boolean hasStarted = false;
    private List<CommandCore> commandList = new ArrayList<>();
    private static String BOT_PREFIX = "?";
    private PlayerManager pm;
    private Random rand = new Random();
    private VoiceListener vl;
    public void addCommand(CommandCore command) {commandList.add(command);}
    public List<CommandCore> getCommandList() {return commandList;}

    @EventSubscriber
    public void onMessageRecieved(MessageReceivedEvent event) {
        if (!hasStarted) {
            pm = new PlayerManager(event.getClient());
            vl = new VoiceListener(event.getClient(), pm);
            getCommands();
            hasStarted = true;
        }
        if(!event.getAuthor().isBot()) {
            pm.rebuildPlayerLists();
            HashMap<Long, Integer> players = pm.getPlayers();
            int score = players.get(event.getAuthor().getLongID());
            players.remove(event.getAuthor().getLongID());
            players.put(event.getAuthor().getLongID(), score + rand.nextInt(10));
            pm.setPlayers(players);
            String[] argArray = event.getMessage().getContent().split(" ");
            if (argArray.length == 0 || !argArray[0].startsWith(BOT_PREFIX)) {
                return;
            }
            String commandStr = argArray[0].substring(BOT_PREFIX.length());
            for (CommandCore command : commandList) {
                if (commandStr.toLowerCase().contains(command.getCommandName().toLowerCase())) {
                    System.out.println(event.getAuthor().getName() + " ran command " + command.getCommandName());
                    command.executeCommand(event, argArray);
                    return;
                }
            }
        }
    }

    private void getCommands() {
        addCommand(new Ping());
        addCommand(new Suggest());
        addCommand(new Ticket());
        addCommand(new Coins(pm.getPlayers()));
        addCommand(new Leaderboard(pm.getPlayers()));
        addCommand(new Help(getCommandList()));
    }
}
