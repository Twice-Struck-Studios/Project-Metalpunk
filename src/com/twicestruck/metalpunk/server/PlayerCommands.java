package com.twicestruck.metalpunk.server;

import com.twicestruck.metalpunk.common.*;

import java.util.*;
import java.io.*;

final class PlayerCommands
{
	private PlayerCommands(){}
	public static void setup(Player player)
	{
		Command cl = new Command(player)
		{
			public void execute(String[] args)
			{
				String cmdstr = "";
				for(int i = 0; i < args.length; i++)
				{
					cmdstr += args[i] + " ";
				}
				cmdstr = cmdstr.trim();
				player.getClientSocket().println("$" + cmdstr);
			}
			public String usage(String commandname)
			{
				player.getClientSocket().println("#$" + commandname);
				return player.getClientSocket().readln();
			}
		};
		player.registerCommand("exec", cl);
		player.registerCommand("exit", cl);
		player.registerCommand("signature", cl);
		player.registerCommand("help", new Command(player)
		{
			public void execute(String[] args)
			{
				if(args.length == 1)
				{
					player.getClientSocket().println("Player commands:");
					for(Map.Entry<String, Command> entry : player.getCommandEntries())
					{
						player.getClientSocket().println("\t"+entry.getKey());
					}
					System.out.println("Type 'help COMMAND' for help on a specific command");
				}
				else if(args.length == 2)
				{
					player.getClientSocket().println(player.getCommand(args[1]).usage(args[1]));
				}
			}
			public String usage(String name)
			{
				return "Lists commands and provides help on individual commands";
			}
		});
		player.registerCommand("alias", new Command(player)
		{
			public void execute(String[] args)
			{
				if(args.length < 3)
				{
					player.getClientSocket().println(usage(args[0]));
					return;
				}
				String commandstring = "";
				String[] commands;
				for(int i = 2; i < args.length; i++)
				{
					commandstring += args[i] + " ";
				}
				commands = commandstring.trim().split(";");
				for(int i = 0; i < commands.length; i++)
				{
					commands[i] = commands[i].trim();
				}
				player.registerCommand(args[1], new Command(player)
				{
					public void execute(String[] args)
					{
						String[] curcmdstr;
						Command curcmd;
						for(int i = 0; i < commands.length; i++)
						{
							curcmdstr = commands[i].split(" ");
							curcmd = player.getCommand(curcmdstr[0]);
							if(curcmd != null)
							{
								try
								{
									curcmd.execute(curcmdstr);
								}
								catch(StackOverflowError soe)
								{
									player.getClientSocket().println("Nice try at crashing the server. (Un?)fortunately, we're immune to stack overflows over here. However, we're not immune to regular overflows. So if you're dead set on bringing down this server, just find it's physical location and pour water on it until it shuts down. Or just hit the power button. That might be easier.");
								}
							}
							else
							{
								player.getClientSocket().println("Command not found: " + curcmdstr[0]);
							}
						}
					}
					public String usage(String name)
					{
						return "Alias created by player";
					}
				});
			}
			public String usage(String commandname)
			{
				return "Usage:\n\t"+commandname+" ALIASNAME COMMAND1; COMMAND2...";
			}
		});
		player.registerCommand("spawn", new Command(player)
		{
			public void execute(String[] args)
			{
				new Unit("alien", player, 1, 1, 1, 1, 1, 1, 1, 1);
			}
			public String usage(String commandname)
			{
				return "";
			}
		});
	}
}
