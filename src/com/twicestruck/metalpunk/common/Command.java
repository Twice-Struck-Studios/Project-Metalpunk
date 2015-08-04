package com.twicestruck.metalpunk.common;

public abstract class Command
{
	protected Object owner;

	public Command(Object owner)
	{
		this.owner = owner;
	}
	
	public Command()
	{
		this.owner = null;
	}
	
	public abstract void execute(String[] args);
	public abstract String usage(String commandname);
}
