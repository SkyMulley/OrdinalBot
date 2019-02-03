package uk.noxiousbot.NoxiousBot.Commands;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.obj.IUser;
import uk.noxiousbot.NoxiousBot.Main;
import uk.noxiousbot.NoxiousBot.PlayerManager;
import uk.noxiousbot.NoxiousBot.VoiceListener;

import java.util.*;

import static java.util.stream.Collectors.toMap;

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
        checkOrdinals(event);
        if(!event.getAuthor().isBot()) {
            pm.addCoins(event.getAuthor().getLongID(),rand.nextInt(5));
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
        addCommand(new Info());
        addCommand(new Suggest());
        addCommand(new Ticket());
        addCommand(new HigherLower(pm));
        addCommand(new Coins(pm.getPlayers()));
        addCommand(new Slot(pm));
        addCommand(new Leaderboard(pm.getPlayers()));
        addCommand(new Help(getCommandList()));
    }

    private void checkOrdinals(MessageReceivedEvent event) {
        try {
            HashMap<Long, Integer> sorted = pm.getPlayers()
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));
            Map.Entry<Long, Integer> entry = sorted.entrySet().iterator().next();
            event.getClient().getUserByID(entry.getKey()).addRole(event.getGuild().getRoleByID(541711864120344587L));
            for (IUser user : event.getClient().getUsers()) {
                user.removeRole(event.getGuild().getRoleByID(541711864120344587L));
            }
        }catch(Exception e) {
        }
    }
}
