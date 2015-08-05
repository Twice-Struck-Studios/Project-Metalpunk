/**
 * Copyright (c) 2015 Benjamin Pylko
 * Distributed under the GNU GPL v3
 * For full terms see GPL in this project's root directory
 */

package com.twicestruck.metalpunk.server;

import com.twicestruck.metalpunk.common.TCPSocket;

import java.io.*;
import java.net.*;

class TCPServer
{
	private ServerSocket serverSock;
	
	public TCPServer(int port)
	{
		try
		{
			serverSock = new ServerSocket(port);
		}
		catch(IOException ioe)
		{
			System.err.println("Could not create server");
			ioe.printStackTrace();
			System.exit(34);
		}
	}
	
	public TCPSocket accept()
	{
		try
		{
			return new TCPSocket(serverSock.accept());
		}
		catch(IOException ioe)
		{
			System.err.println("Could not accept connection");
			ioe.printStackTrace();
		}
		return null;
	}
}
