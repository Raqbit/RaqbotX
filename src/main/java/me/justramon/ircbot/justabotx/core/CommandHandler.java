package me.justramon.ircbot.justabotx.core;

import java.util.LinkedList;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import me.justramon.ircbot.justabotx.commands.Clear;
import me.justramon.ircbot.justabotx.commands.Disable;
import me.justramon.ircbot.justabotx.commands.Enable;
import me.justramon.ircbot.justabotx.commands.Help;
import me.justramon.ircbot.justabotx.commands.Quit;
import me.justramon.ircbot.justabotx.commands.TestCommand;
import me.justramon.ircbot.justabotx.util.IDevCommand;
import me.justramon.ircbot.justabotx.util.Operators;

public class CommandHandler extends ListenerAdapter
{
	public static LinkedList<ICommand<MessageEvent>> opcommands = new LinkedList<ICommand<MessageEvent>>();
	public static LinkedList<ICommand<MessageEvent>> commands = new LinkedList<ICommand<MessageEvent>>();
	private LinkedList<IDevCommand<MessageEvent>> devcommands = new LinkedList<IDevCommand<MessageEvent>>();

	public CommandHandler()
	{
		opcommands.add(new Enable());
		opcommands.add(new Disable());
		opcommands.add(new Quit());
		opcommands.add(new Clear());
		devcommands.add(new TestCommand());
		commands.add(new Help());
	}

	public void onMessage(MessageEvent event) throws Exception
	{
		String cmdName = event.getMessage().split(" ")[0];
		
		if(cmdName.startsWith("?"))
		{
			for (ICommand<MessageEvent> cmd : commands)
			{
				if (Core.enabled)
				{
					for (String s : cmd.getAliases())
					{
						if (cmdName.equalsIgnoreCase("?" + s))
						{
							cmd.exe(event);
							System.gc();
							return;
						}
					}
				}
			}

			for(ICommand<MessageEvent> cmd : opcommands)
			{
				if(Core.enabled || cmd instanceof Enable || cmd instanceof Disable)
				{
					for(String s : cmd.getAliases())
					{
						if (cmdName.equalsIgnoreCase("?" + s))
						{
							if(Operators.isOp(event))
							{
								cmd.exe(event);
								System.gc();
								return;
							}
						}
					}
				}
			}

			for(IDevCommand<MessageEvent> cmd : devcommands)
			{
				if(Core.enabled || cmd instanceof Enable || cmd instanceof Disable)
				{
					for(String s : cmd.getAliases())
					{
						if (cmdName.equalsIgnoreCase("?" + s))
						{
							if(Operators.isOp(event))
							{
								if(Core.dev)
								{
									cmd.exe(event);
									System.gc();
									return;
								}
							}
						}
					}
				}
			}
		}
	}
}
