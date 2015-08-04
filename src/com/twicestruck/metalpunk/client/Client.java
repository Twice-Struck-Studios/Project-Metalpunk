package com.twicestruck.metalpunk.client;

import com.twicestruck.metalpunk.common.*;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.nio.file.*;

class Client
{
	static TCPSocket client;
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			System.out.println("Usage:");
			System.out.println("\tjava -jar MetalPunkClient.jar HOSTNAME[:PORT] [SIGNATURE]");
			System.exit(420);
		}
		Scanner input = new Scanner(System.in);
		String[] host = args[0].split(":");
		try
		{
			client = new TCPSocket(host[0], Integer.parseInt(host[1]));
		}
		catch(ArrayIndexOutOfBoundsException aiooe)
		{
			client = new TCPSocket(host[0], 27420);
		}
		final String sig = args.length >= 2 ? args[1] : Integer.toHexString((int)Double.doubleToLongBits(Math.random()) & 0x0000ffff);
		client.println(sig);
		System.out.println("Your signature is " + sig);
		LinkedBlockingQueue<String> cmdlines = new LinkedBlockingQueue<String>();
		HashMap<String, Command> commands = new HashMap<String, Command>();
		commands.put("$exec", new Command()
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
					for(String line : lines)
					{
						try
						{
							cmdlines.put(line);
						}
						catch(InterruptedException ie)
						{
							System.out.println("Interruption I don't care about, but probably should");
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
				return "\tUsage: " + name + " FILE";
			}
		});
		commands.put("$exit", new Command()
		{
			public void execute(String[] args)
			{
				System.exit(0);
			}
			
			public String usage(String name)
			{
				return "Exits the game and quits the client";
			}
		});
		commands.put("$signature", new Command()
		{
			public void execute(String[] args)
			{
				System.out.println("You are connected with signature " + sig);
			}
			
			public String usage(String name)
			{
				return "Prints the client's current signature";
			}
		});
		String msg = "";
		while(!("EOM".equals(msg = client.readln())))
		{
			System.out.println(msg);
		}
		String[] commandsplit;
		while(true)
		{
			System.out.print(">");
			try
			{
				if(cmdlines.peek() == null)
				{
					cmdlines.put(input.nextLine());
				}
				while(cmdlines.peek() != null)
				{
					client.println(cmdlines.poll());
					while(!("EOM".equals(msg = client.readln())))
					{
						if(msg.charAt(0) == '$')
						{
							commandsplit = msg.split(" ");
							commands.get(commandsplit[0]).execute(commandsplit);
						}
						else if(msg.charAt(0) == '#')
						{
							client.println(commands.get(msg.substring(1)).usage(msg.substring(2)));
						}
						else
						{
							System.out.println(msg);
						}
					}
				}
			}
			catch(InterruptedException ie)
			{
				System.out.println("Interruption that I don't care about, but probably should");
			}
		}
	}
}
