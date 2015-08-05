/**
 * Copyright (c) 2015 Benjamin Pylko
 * Distributed under the GNU GPL v3
 * For full terms see GPL in this project's root directory
 */

package com.twicestruck.metalpunk.server;

import com.twicestruck.metalpunk.common.*;

import java.util.*;
import java.io.*;

class Player extends Thread
{
	private TCPSocket client;
	private HashMap<String, Command> commands;
	private HashMap<String, String> strings;
	private String[] clientmsg;
	private Command curcmd;

	public Player(TCPSocket client)
	{
		super();
		this.client = client;
		commands = new HashMap<String, Command>();
		strings = new HashMap<String, String>();
	}

	public void run()
	{
		while(true)
		{
			clientmsg = client.readln().split(" ");
			curcmd = commands.get(clientmsg[0]);
			if(curcmd != null)
			{
				curcmd.execute(clientmsg);
			}
			else
			{
				client.println("Command not found: " + clientmsg[0]);
			}
			client.println("EOM");
		}
	}
	
	public TCPSocket getClientSocket()
	{
		return client;
	}
	
	public void registerCommand(String name, Command command)
	{
		commands.put(name, command);
	}
	
	public Command getCommand(String name)
	{
		return commands.get(name);
	}
	
	public Set<Map.Entry<String, Command>> getCommandEntries()
	{
		return commands.entrySet();
	}
	
	public void registerString(String name, String value)
	{
		strings.put(name, value);
	}
	
	public String getString(String name)
	{
		if(strings.get(name) != null)
		{
			return strings.get(name);
		}
		else
		{
			return "[Missing String " + name + "]";
		}
	}
}
