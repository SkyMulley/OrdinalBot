package uk.noxiousbot.NoxiousBot.Commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public abstract class CommandCore {
    protected String commandName;
    protected String helpMessage;
    protected String Usage;

    public String getCommandName() {return commandName;}
    public String getHelpMessage() {return helpMessage;}
    public String getUsage() {return Usage;}

    public boolean executeCommand(MessageReceivedEvent event, String[] argArray) { return true;}
}
