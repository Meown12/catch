package network;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.*;

import core.Player;
import core.Screen;
import core.Game;
import static misc.Serializer.*;

public class Server
{
	public static final int FRAME_INTERVAL = 4;
	public static short PORT;
	DatagramPacket packet;
	DatagramSocket socket;
	Game game;
	private byte[] data;
	
	public Server()
	{
		System.out.println("Server> started");
		Screen.init();
		Screen.get().addKeyListener(new ServerKeyManager(this)); // create ServerKeyManager
		game = new Game();
		ServerSender sender = new ServerSender(this); // create ServerSender
		new Thread(sender).start(); // start ServerSender

		try
		{
			socket = new DatagramSocket(PORT); // setup socket
			System.out.println("Server> socket init");	
		} catch (Exception e) { System.err.println("Server> socket init: " + e); System.exit(1); }

		new Timer().scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				data = new byte[2000];
				packet = new DatagramPacket(data, data.length);
				try
				{
					socket.receive(packet); // receive KeyEvent as byte[]
					System.out.println("Server> receive data");
				} catch (Exception e) { System.err.println("Server> receive data: " + e); System.exit(1); }

				KeyEvent keyEvent = null;
				try
				{
					keyEvent = (KeyEvent) byteArrayToObject(data); // convert to KeyEvent
				} catch (Exception e) { System.err.println("Server> data to ki: " + e); System.exit(1); }


				Player localPlayer = null;

				for (Player player : getPlayers()) // for all players
				{
					if (player.getAddress().getHostAddress().equals(packet.getAddress().getHostAddress())) // if the KeyEvent came from any of you
					{
						localPlayer = player; // be the localPlayer!
						break;
					}
				}

				if (localPlayer == null) // if not
				{
					getPlayers().add(localPlayer = new Player(10, 10, false, packet.getAddress().getHostName())); // create localPlayer
				}

				localPlayer.applyKeyEvent(keyEvent); // apply the KeyEvent to localPlayer
			}

		}, FRAME_INTERVAL, FRAME_INTERVAL);

		while (true)
		{
			game.tick();
		}
	}
	
	public static void main(String args[])
	{
		PORT = (short) Integer.parseInt(args[0]);
		new Server();
	}

	LinkedList<Player> getPlayers()
	{
		return game.getPlayers();
	}

	Player getServerPlayer()
	{
		return getPlayers().get(0);
	}

	Game getGame() { return game; }
}
