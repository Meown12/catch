package core;

import java.util.LinkedList;

import core.Player;

public class Game
{
	private LinkedList<Player> players;

	public Game()
	{
		players = new LinkedList<Player>();
		players.add(new Player(50, 10, true, "localhost")); // server-player
	}

	public void tick()
	{
		Player runner = getRunner();
		for (Player player : getPlayers()) // for all player
		{
			if (runner.collide(player)) // if you collide with the <runner>
			{
				runner.setRunning(false); // the runner is no runner anymore
				runner.resetPosition(); // and gets ported back to 10, 10
				player.setRunning(true); // but the catcher is runner now
				break;
			}
		}
	}

	private Player getRunner() // returns running-player
	{
		for (Player player : getPlayers()) // for all players
		{
			if (player.isRunning()) // is you run
			{
				return player; // get returned
			}
		} // if none is returned

		System.out.println("Game.getRunner()> no runner found"); // DEBUG
		return null; // return null
	}

	public LinkedList<Player> getPlayers() { return players; }
}
