package mulley.sky.OrdinalBot.Commands;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.object.util.Snowflake;
import mulley.sky.OrdinalBot.Main;
import mulley.sky.OrdinalBot.PlayerManager;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class MessageListener {
    private Main main;
    private boolean hasStarted = false;
    private List<CommandCore> commandList = new ArrayList<>();
    private static String BOT_PREFIX = "?";
    private PlayerManager pm;
    private Random rand = new Random();
    public void addCommand(CommandCore command) {commandList.add(command);}
    public List<CommandCore> getCommandList() {return commandList;}
    public MessageListener(DiscordClient client) {
        pm = new PlayerManager(client);
        getCommands();
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .subscribe(messageCreateEvent -> {
                    if(pm.isBlacklisted(messageCreateEvent.getMember().get())) { return;}
                    checkOrdinals(messageCreateEvent);
                    if(!messageCreateEvent.getMember().get().isBot()) {
                        pm.addCoins(messageCreateEvent.getMember().get(),rand.nextInt(5));
                        String[] argArray = messageCreateEvent.getMessage().getContent().get().split(" ");
                        if (argArray.length == 0 || !argArray[0].startsWith(BOT_PREFIX)) {
                            return;
                        }
                        String commandStr = argArray[0].substring(BOT_PREFIX.length());
                        for (CommandCore command : commandList) {
                            if (commandStr.toLowerCase().contains(command.getCommandName().toLowerCase())) {
                                System.out.println(messageCreateEvent.getMember().get().getDisplayName() + " ran command " + command.getCommandName());
                                command.executeCommand(messageCreateEvent, argArray);
                                return;
                            }
                        }
                    }
                });
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

    private void checkOrdinals(MessageCreateEvent event) {
        try {
            HashMap<Long, Integer> sorted = pm.getPlayers()
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));
            Map.Entry<Long, Integer> entry = sorted.entrySet().iterator().next();
            Member ordinal = event.getClient().getUserById(Snowflake.of(entry.getKey())).block().asMember(event.getGuildId().get()).block();
            Role ordinalRole = event.getClient().getRoleById(event.getGuildId().get(),Snowflake.of(541711864120344587L)).block();
            if(!ordinal.getRoles().collectList().block().contains(ordinalRole)) {
                event.getClient().updatePresence(Presence.online(Activity.watching("the Ordinal "+ordinal.getUsername())));
                ordinal.addRole(ordinalRole.getId());
                for (Member user : event.getGuild().block().getMembers().collectList().block()) {
                    user.removeRole(ordinalRole.getId());
                }
                event.getGuild().block().getSystemChannel().block().createMessage("**"+ordinal.getDisplayName()+"** has become the new Ordinal!");
            }
        }catch(Exception e) {
        }
    }
}
