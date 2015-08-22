package network;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.*;

import core.Player;
import core.Screen;
import core.Game;
import misc.KeyManager;
import misc.Debug;
import static misc.Serializer.*;

public class Server
{
	public static final short PORT = 4200, FRAME_RATE = 60; // maybe should be higher? TODO
	private DatagramSocket socket;
	private Game game;
	private KeyManager keyManager;
	
	public Server()
	{
		System.out.println("Server> started");
		game = new Game(); // create Game (internally adds server-player)

		try
		{
			socket = new DatagramSocket(PORT); // setup socket
			System.out.println("Server> socket init");	
		} catch (Exception e) { System.err.println("Server> socket init: " + e); System.exit(1); }

		Screen.init(); // init screen
		Screen.get().addKeyListener(keyManager = new KeyManager()); // add keyManager to the screen
		run(); // run()
	}
	
	public static void main(String args[])
	{
		new Server(); // goto <Server>()
	}

	private void run()
	{
		new Timer().scheduleAtFixedRate(new TimerTask() // repeat all <FRAME_RATE> milliseconds?
		{
			@Override public void run()
			{
				Debug.timeLog("Server.run()");
				applyKeyEvents(); // apply key events for server-player
				tick(); // tick all players and game
				send(); // send getPlayers() to all clients
				render(); // render all players
			}
		}, FRAME_RATE, FRAME_RATE);

		while (true) // repeat forever
		{
			receive(); // receive
		}

	}

	private void send() // send getPlayers() to all clients
	{
		DatagramPacket sendPacket = null;
		byte[] data = objectToByteArray(new LinkedList<Player>(getPlayers())); // convert players to data
		
		for (int i = 1 /* !!! */; i < getPlayers().size(); i++) // for every player EXCEPT of the serverPlayer
		{
			sendPacket = new DatagramPacket(data, data.length, getPlayers().get(i).getAddress(), PORT);
		
			try
			{
				socket.send(sendPacket); // send him all players to render
			} catch (Exception e) {System.err.println("Server> send data: " + e); System.exit(1);}
		}
	}

	private void receive() // receive byte[] keys from the clients
	{
		byte[] keys = new byte[KeyManager.KEYS_LENGTH];
		DatagramPacket packet = new DatagramPacket(keys, keys.length);
		try
		{
			socket.receive(packet); // receive byte[] keys
		} catch (Exception e) { System.err.println("Server> receive data: " + e); System.exit(1); }

		Player localPlayer = null;

		for (int i = 1; i < getPlayers().size(); i++) // for all players except the server--player
		{
			if (getPlayers().get(i).getAddress().getHostAddress().equals(packet.getAddress().getHostAddress())) // if the KeyEvent came from any of you
			{
				localPlayer = getPlayers().get(i); // be the localPlayer!
				break;
			}
		}

		if (localPlayer == null) // if not
		{
			getPlayers().add(localPlayer = new Player(10, 10, false, packet.getAddress().getHostName())); // create localPlayer
		}

		localPlayer.applyKeys(keys); // apply the keys to localPlayer
	}

	private void tick() // tick players and game
	{
		for (Player player : getPlayers()) // for all players
			player.tick(); // tick
		game.tick(); // game.tick()
	}

	private void render() // renders all players
	{
		for (Player player : getPlayers()) // for all players
			player.render(); // render
		Screen.update(); // update screen
	}

	private void applyKeyEvents()
	{
		if (keyManager.keysChanged()) // if keys have been pressed or released
		{
			getServerPlayer().applyKeys(keyManager.keys); // give them to the server-player
		}
		keyManager.updateKeys(); // reset key states
	}

	private Player getServerPlayer() { return getPlayers().get(0); }
	private LinkedList<Player> getPlayers() { return game.getPlayers(); }
}
