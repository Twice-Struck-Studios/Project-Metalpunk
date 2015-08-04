package com.twicestruck.metalpunk.server;

import com.twicestruck.metalpunk.common.*;

import java.util.*;

abstract class Entity
{
	private double nextThink;
	private static double timeScale = 1.0;
	
	public Entity(ArrayList<Entity> chain)
	{
		nextThink = getTime();
		if(chain != null)
		{
			chain.add(this);
		}
	}
	
	public static double getTime()
	{
		return System.nanoTime()*timeScale/1000000000.0;
	}
	
	public static double getTimeScale()
	{
		return timeScale;
	}
	
	public static void setTimeScale(double timeScale)
	{
		Entity.timeScale = timeScale;
	}
	
	public void setNextThink(double time)
	{
		nextThink = getTime() + time;
	}
	
	public double getNextThink()
	{
		return nextThink;
	}
	
	public abstract void think();
}
