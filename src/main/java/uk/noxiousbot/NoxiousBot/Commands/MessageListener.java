package uk.noxiousbot.NoxiousBot.Commands;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import uk.noxiousbot.NoxiousBot.Main;

import java.util.ArrayList;
import java.util.List;

public class MessageListener {
    private Main main;
    private boolean hasStarted = false;
    private List<CommandCore> commandList = new ArrayList<>();
    private static String BOT_PREFIX = "?";

    public void addCommand(CommandCore command) {commandList.add(command);}
    public List<CommandCore> getCommandList() {return commandList;}

    @EventSubscriber
    public void onMessageRecieved(MessageReceivedEvent event) {
        if (!hasStarted) {
            getCommands();
            hasStarted = true;
        }
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

    private void getCommands() {
        addCommand(new Ping());
        addCommand(new Suggest());
        addCommand(new Ticket());
        addCommand(new Help(getCommandList()));
    }
}
