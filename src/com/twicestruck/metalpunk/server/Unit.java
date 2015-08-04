package com.twicestruck.metalpunk.server;

import java.io.*;

class Unit extends Entity
{
	String name;
	Player owner;
	double hp;
	double speed;
	double strength;
	double perception;
	double defense;
	double will;
	double aim;
	double leadership;
	public Unit(String name, Player owner, double hp, double speed, double strength, double perception, double defense, double will, double aim, double leadership)
	{
		super(Server.chain);
		this.owner = owner;
		this.name = name;
		this.hp = hp;
		this.speed = speed;
		this.strength = strength;
		this.perception = perception;
		this.defense = defense;
		this.will = will;
		this.aim = aim;
		this.leadership = leadership;
	}
	
	public void think()
	{
		System.out.println("ayy lmao");
		setNextThink(0.2);
	}
}
