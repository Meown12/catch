package core;

import java.util.LinkedList;

import core.Player;

public class Game
{
	LinkedList<Player> players;

	public Game()
	{
		players = new LinkedList<Player>();
		players.add(new Player(10, 10, true, "localhost"));
	}

	public void tick()
	{
		// TODO
	}

	public LinkedList<Player> getPlayers() { return players; }
}
