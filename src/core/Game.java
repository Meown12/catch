package core;

import java.util.LinkedList;

import core.Player;

public class Game
{
	LinkedList<Player> players;

	public Game()
	{
		players = new LinkedList<Player>();
		players.add(new Player(50, 10, true, "localhost"));
	}

	public void tick()
	{
		Player runner = getRunner();
		for (Player player : getPlayers())
		{
			if (runner.collide(player))
			{
				runner.setRunning(false);
				runner.resetPosition();
				player.setRunning(true);
				break;
			}
		}
	}

	private Player getRunner()
	{
		for (Player player : getPlayers())
		{
			if (player.isRunning())
			{
				return player;
			}
		}

		System.out.println("Game.getRunner()> no runner found"); // DEBUG
		return null;
	}
	public LinkedList<Player> getPlayers() { return players; }
}
