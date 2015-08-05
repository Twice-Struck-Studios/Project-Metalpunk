/**
 * Copyright (c) 2015 Benjamin Pylko
 * Distributed under the GNU GPL v3
 * For full terms see GPL in this project's root directory
 */

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
