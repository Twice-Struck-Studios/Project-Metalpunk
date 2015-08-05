/**
 * Copyright (c) 2015 Benjamin Pylko
 * Distributed under the GNU GPL v3
 * For full terms see GPL in this project's root directory
 */

package com.twicestruck.metalpunk.server;

import com.twicestruck.metalpunk.common.*;

import java.util.*;
import java.io.*;

class Server
{
	public static ArrayList<Entity> chain = new ArrayList<Entity>();
	public static void main(String[] args)
	{
		TCPServer server;
		int port = 27420;
		TCPSocket p1;
		TCPSocket p2;
		String p1sig = "";
		String p2sig = "";
		Player up = null;
		Player dp = null;
		String choice = "";
		boolean choosing = true;
		Entity link;
		new Thread("Server Shell")
		{
			Scanner sc = new Scanner(System.in);
			String[] input;
			HashMap<String, Command> commands;
			Command curCmd;
			public void run()
			{
				commands = new HashMap<String, Command>();
				ServerCommands.setup(commands);
				System.out.println("Type 'help' for a list of commands");
				while(true)
				{
					System.out.print(">");
					input = sc.nextLine().split(" ");
					curCmd = commands.get(input[0]);
					if(curCmd != null)
					{
						curCmd.execute(input);
					}
					else
					{
						System.out.println("Command not found: " + input[0]);
					}
				}
			}
		}.start();
		try
		{
			port = Integer.parseInt(args[0]);
		}
		catch(ArrayIndexOutOfBoundsException aiooe)
		{
			System.out.println("Using default port 27420, because I guess you're not creative enough to think of any other number");
			port = 27420;
		}
		server = new TCPServer(port);
		p1 = server.accept();
		try
		{
			System.out.println("Player 1 connected with signature " + (p1sig = p1.readln()));
		}
		catch(NoSuchElementException nsee)
		{
			System.out.println("Error reading signature from Player 1. That takes skill to disconnect mere microseconds after the server accepts the connection. However, it's also super annoying. Player 1 is the worst.");
		}
		while(choosing)
		{
			System.out.print("[P1]");
			p1.println("Enter your faction:");
			System.out.print("[P1]");
			p1.println("(U)nderworld or (D)irective");
			p1.println("EOM");
			System.out.print("P1: ");
			System.out.println(choice = p1.readln());
			if(choice.charAt(0) == 'U' || choice.charAt(0) == 'u')
			{
				PlayerCommands.setup(up = new Player(p1));
				UnderworldCommands.setup(up);
				p1.println("Waiting for Player 2... (If you're in the same room, tell him/her to hurry up");
				choosing = false;
			}
			else if(choice.charAt(0) == 'D' || choice.charAt(0) == 'd')
			{
				PlayerCommands.setup(dp = new Player(p1));
				DirectiveCommands.setup(dp);
				p1.println("Waiting for Player 2... (If you're in the same room, tell him/her to hurry up)");
				choosing = false;
			}
		}
		
		p2 = server.accept();
		try
		{
			System.out.println("Player 2 connected with signature " + (p2sig = p2.readln()));
		}
		catch(NoSuchElementException nsee)
		{
			System.out.println("Error reading signature from Player 2. That takes skill to disconnect mere microseconds after the server accepts the connection. However, it's also super annoying. Player 2 is the worst.");
		}
		if(choice.charAt(0) == 'D' || choice.charAt(0) == 'd')
		{
			PlayerCommands.setup(up = new Player(p2));
			UnderworldCommands.setup(up);
			p2.println("You have been assigned the faction Underworld");
		}
		else if(choice.charAt(0) == 'U' || choice.charAt(0) == 'u')
		{
			PlayerCommands.setup(dp = new Player(p2));
			DirectiveCommands.setup(dp);
			p2.println("You have been assigned the faction Directive");
		}
		p1.println("EOM");
		p2.println("EOM");
		dp.start();
		up.start();
		Entity world = new Entity(chain)
		{
			public void think()
			{
				try
				{
					Thread.sleep(1);
				}
				catch(InterruptedException ie)
				{
					System.out.println("The think thread's nap was interrupted! But who cares? Not me.");
				}
				setNextThink(0.0);
			}
		};
		int i = 0;
		while(true)
		{
			link = chain.get(i);
			synchronized(link)
			{
				if(link.getNextThink() < Entity.getTime())
				{
					link.think();
				}
			}
			i = (i + 1)%chain.size();
		}

	}
}
