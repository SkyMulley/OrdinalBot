package mulley.sky.OrdinalBot.Commands;

import mulley.sky.OrdinalBot.PlayerManager;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class MessageListener {
    private List<CommandCore> commandList = new ArrayList<>();
    private static String BOT_PREFIX = "?";
    private PlayerManager pm;
    private Random rand = new Random();
    public void addCommand(CommandCore command) {commandList.add(command);}
    public List<CommandCore> getCommandList() {return commandList;}
    public MessageListener(IDiscordClient client) {
        pm = new PlayerManager(client);
        getCommands();
    }

    @EventSubscriber
    public void onMessageRecieved(MessageReceivedEvent event) {
        checkOrdinals(event);
        if(!event.getAuthor().isBot()) {
            pm.addCoins(event.getAuthor(),rand.nextInt(5));
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
        addCommand(new Coins(pm));
        addCommand(new Slot(pm));
        addCommand(new TwentyOne(pm));
        addCommand(new Leaderboard(pm.getPlayers()));
        addCommand(new Help(getCommandList()));
        addCommand(new Blacklist(pm));
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
            IRole role = event.getGuild().getRoleByID(541711864120344587L);
            IUser user1 = event.getClient().getUserByID(entry.getKey());
            event.getClient().changePresence(StatusType.ONLINE, ActivityType.WATCHING,"the Ordinal "+user1.getName());
            for (IUser user : event.getClient().getUsers()) {
                if (user.hasRole(role) && !user.equals(user1)) {
                    user.removeRole(role);
                }
            }
            user1.addRole(event.getGuild().getRoleByID(541711864120344587L));
        }catch(Exception e) {
        }
    }
}
