package mulley.sky.OrdinalBot.Commands;

import discord4j.core.event.domain.message.MessageCreateEvent;

public abstract class CommandCore {
    protected String commandName;
    protected String helpMessage;
    protected String Usage;
    protected boolean helpViewable = true;

    public String getCommandName() {return commandName;}
    public String getHelpMessage() {return helpMessage;}
    public String getUsage() {return Usage;}
    public boolean isHelpViewable() {return helpViewable;}

    public boolean executeCommand(MessageCreateEvent event, String[] argArray) { return true;}
}
