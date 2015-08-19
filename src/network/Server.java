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
	
	public Server()
	{
		System.out.println("Server> started");
		game = new Game();

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
				applyKeyEvents();
				tick();
				send();
				render();
			}
		}, 20, 20);

		while (true)	
		{
			receive();
		}

	}

	private void send()
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

	private void receive()
	{
		byte[] keys = new byte[200];
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

	private void tick()
	{
		for (Player player : getPlayers())
			player.tick();
		game.tick();
	}

	private void render()
	{
		for (Player player : getPlayers())
			player.render();
		Screen.update();
	}

	private void applyKeyEvents()
	{
		if (keyManager.keysChanged())
		{
			getServerPlayer().applyKeys(keyManager.keys);
		}
		keyManager.updateKeys();
	}

	private Player getServerPlayer() { return getPlayers().get(0); }
	private LinkedList<Player> getPlayers() { return game.getPlayers(); }
}
