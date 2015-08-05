/**
 * Copyright (c) 2015 Benjamin Pylko
 * Distributed under the GNU GPL v3
 * For full terms see GPL in this project's root directory
 */

package com.twicestruck.metalpunk.common;

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPSocket
{
	private Scanner reader;
	private DataOutputStream writer;
	private Socket sock;
	String hostname;
	int port;
	
	public TCPSocket(String hostname, int port)
	{
		this.hostname = hostname;
		this.port = port;
		try
		{
			sock = new Socket(hostname, port);
			reader = new Scanner(sock.getInputStream());
			writer = new DataOutputStream(sock.getOutputStream());
		}
		catch(UnknownHostException uhe)
		{
			System.err.println("Error: could not find host " + hostname + " on port " + port);
			uhe.printStackTrace();
		}
		catch(IOException ioe)
		{
			System.err.println("Error: could not communicate with host " + hostname + " on port " + port);
			ioe.printStackTrace();
		}
	}
	
	public TCPSocket(Socket sock)
	{
		try
		{
			this.sock = sock;
			reader = new Scanner(sock.getInputStream());
			writer = new DataOutputStream(sock.getOutputStream());
		}
		catch(IOException ioe)
		{
			System.err.println("Error: could not communicate over Socket");
			ioe.printStackTrace();
		}
	
	}
	
	public void close() throws IOException
	{
		try
		{
			sock.close();
		}
		catch(IOException ioe)
		{
			System.out.println("Error communicating");
		}
	}
	
	public void println(String print)
	{
		try
		{
			writer.writeBytes(print+"\n");
		}
		catch(IOException ioe)
		{
			System.out.println("Error communicating");
		}
	}
	
	public String readln()
	{
		return reader.nextLine();
	}
	
	public boolean hasNext()
	{
		return reader.hasNextLine();
	}
	
	public boolean isConnected()
	{
		return sock.isConnected();
	}
}
