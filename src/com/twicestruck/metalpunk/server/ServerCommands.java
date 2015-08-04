package com.twicestruck.metalpunk.server;

import com.twicestruck.metalpunk.common.*;

import java.util.*;
import java.io.*;
import java.nio.file.*;

final class ServerCommands
{
	private ServerCommands(){}
	public static void setup(HashMap<String, Command> commands)
	{
		Set<Map.Entry<String, Command>> commandset = commands.entrySet();
		commands.put("help", new Command()
		{
			public void execute(String[] args)
			{
				if(args.length == 1)
				{
					System.out.println("Server commands:");
					for(Map.Entry<String, Command> entry : commandset)
					{
						System.out.println("\t"+entry.getKey());
					}
					System.out.println("Type 'help COMMAND' for help on a specific command");
				}
				else if(args.length == 2)
				{
					System.out.println(commands.get(args[1]).usage(args[1]));
				}
			}
			public String usage(String name)
			{
				return "Lists commands and provides help on individual commands";
			}
		});
		commands.put("exit", new Command()
		{
			public void execute(String[] args)
			{
				System.out.println("Server shutting down");
				System.exit(0);
			}
			public String usage(String name)
			{
				return "Shuts down the server";
			}
		});
		commands.put("timescale", new Command()
		{
			public void execute(String[] args)
			{
				if(args.length > 2)
				{
					System.out.println("Too many arguments:");
					System.out.println(usage(args[0]));
					return;
				}
				else if(args.length == 1)
				{
					System.out.println("Game Time Scale is " + Entity.getTimeScale());
					return;
				}
				try
				{
					Entity.setTimeScale(Double.parseDouble(args[1]));
					System.out.println("Game Time Scale set to " + args[1]);
				}
				catch(NumberFormatException nfe)
				{
					System.out.println("Error: TIMESCALE must be an integer");
				}
			}
			public String usage(String name)
			{
				return "Usage:\n\t" + name + " TIMESCALE";
			}
		});
		commands.put("exec", new Command()
		{
			public void execute(String[] args)
			{
				if(args.length > 2)
				{
					System.out.println("Too many arguments:");
					System.out.println(usage(args[0]));
					return;
				}
				if(args.length == 1)
				{
					System.out.println("Missing FILE argument:");
					System.out.println(usage(args[0]));
					return;
				}
				try
				{
					Path p = FileSystems.getDefault().getPath(args[1]);
					List<String> lines = Files.readAllLines(p);
					String[] curline;
					Command curcmd;
					for(String line : lines)
					{
						curline = line.split(" ");
						curcmd = commands.get(curline[0]);
						if(curcmd != null)
						{
							curcmd.execute(curline);
						}
						else
						{
							System.out.println("Command not found: " + curline[0]);
						}
					}
				}
				catch(IOException ioe)
				{
					System.out.println("Error reading file: " + args[1]);
				}
			}
			public String usage(String name)
			{
				return "Usage: \n\t" + name + " FILE";
			}
		});
		commands.put("alias", new Command()
		{
			public void execute(String[] args)
			{
				if(args.length < 3)
				{
					System.out.println(usage(args[0]));
					return;
				}
				String commandstring = "";
				String[] listofcommands;
				for(int i = 2; i < args.length; i++)
				{
					commandstring += args[i] + " ";
				}
				listofcommands = commandstring.trim().split(";");
				for(int i = 0; i < listofcommands.length; i++)
				{
					listofcommands[i] = listofcommands[i].trim();
				}
				commands.put(args[1], new Command()
				{
					public void execute(String[] args)
					{
						String[] curcmdstr;
						Command curcmd;
						for(int i = 0; i < listofcommands.length; i++)
						{
							curcmdstr = listofcommands[i].split(" ");
							curcmd = commands.get(curcmdstr[0]);
							if(curcmd != null)
							{
								try
								{
									curcmd.execute(curcmdstr);
								}
								catch(StackOverflowError soe)
								{
									System.out.println("Nice try at crashing the server. (Un?)fortunately, we're immune to stack overflows over here. However, we're not immune to regular overflows. So if you're dead set on bringing down this server, pour water on it until it shuts down. Or just hit the power button. That might be easier.");
									
								}
							}
							else
							{
								System.out.println("Command not found: " + curcmdstr[0]);
							}
						}
					}
					public String usage(String name)
					{
						return "Alias created by server admin";
					}
				});
			}
			public String usage(String commandname)
			{
				return "Usage:\n\t"+commandname+" ALIASNAME COMMAND1; COMMAND2...";
			}
		});
	}
}
