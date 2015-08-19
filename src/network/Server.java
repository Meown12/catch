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
import static misc.Serializer.*;

public class Server
{
	public static final short PORT = 4200;
	DatagramSocket socket;
	Game game;
	KeyManager keyManager;
	LinkedList<Player> players = new LinkedList<Player>();
	
	public Server()
	{
		System.out.println("Server> started");
		game = new Game();
		players.add(new Player(10, 10, true, "localhost"));

		try
		{
			socket = new DatagramSocket(PORT); // setup socket
			System.out.println("Server> socket init");	
		} catch (Exception e) { System.err.println("Server> socket init: " + e); System.exit(1); }
		Screen.init();
		Screen.get().addKeyListener(keyManager = new KeyManager());
		run();
	}
	
	public static void main(String args[])
	{
		new Server();
	}

	private void run()
	{
		new Timer().scheduleAtFixedRate(new TimerTask()
		{
			@Override public void run()
			{
				receive();
			}
		}, 40, 40);

		new Timer().scheduleAtFixedRate(new TimerTask()
		{
			@Override public void run()
			{
				applyKeyEvents();
				tick();
				send();
				render();
			}
		}, 60, 60);
	}

	private void send()
	{
		DatagramPacket sendPacket = null;
		byte[] data = objectToByteArray(new LinkedList<Player>(players)); // convert players to data
		
		for (int i = 1 /* !!! */; i < players.size(); i++) // for every player EXCEPT of the serverPlayer
		{
			sendPacket = new DatagramPacket(data, data.length, players.get(i).getAddress(), PORT);
		
			try
			{
				socket.send(sendPacket); // send him all players to render
				System.out.println("ServerSender> send data");
			} catch (Exception e) {System.err.println("ServerSender> send data: " + e); System.exit(1);}
		}
	}

	private void receive()
	{
		byte[] keys = new byte[200];
		DatagramPacket packet = new DatagramPacket(keys, keys.length);
		try
		{
			socket.receive(packet); // receive KeyEvent as byte[]
			System.out.println("Server> receive data");
		} catch (Exception e) { System.err.println("Server> receive data: " + e); System.exit(1); }

		Player localPlayer = null;

		for (Player player : players) // for all players
		{
			if (player.getAddress().getHostAddress().equals(packet.getAddress().getHostAddress())) // if the KeyEvent came from any of you
			{
				localPlayer = player; // be the localPlayer!
				break;
			}
		}

		if (localPlayer == null) // if not
		{
			players.add(localPlayer = new Player(10, 10, false, packet.getAddress().getHostName())); // create localPlayer
		}

		localPlayer.applyKeys(keys); // apply the keys to localPlayer
	}

	private void tick()
	{
		for (Player player : players)
			player.tick();
		game.tick();
	}

	private void render()
	{
		for (Player player : players)
			player.render();
		Screen.update();
	}

	private void applyKeyEvents()
	{
		if (!keyManager.keys.equals(keyManager.oldKeys))
		{
			getServerPlayer().applyKeys(keyManager.keys);
		}
		for (int i = 0; i < keyManager.keys.length; i++)
			keyManager.oldKeys[i] = keyManager.keys[i];
	}

	private Player getServerPlayer() { return players.get(0); }
}
