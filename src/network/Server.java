package network;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.*;
import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;

import static misc.Serializer.*;
import core.Player;
import core.Screen;
import misc.KeyInfo;

public class Server
{
	public static short PORT;
	DatagramPacket packet;
	DatagramSocket socket;
	LinkedList<Player> players;
	private byte[] data;
	
	public Server()
	{
		System.out.println("Server> started");
		Screen.init();
		Screen.get().addKeyListener(new ServerKeyManager(this)); // create ServerKeyManager
		players = new LinkedList<Player>();
		players.add(new Player(10, 10, true, "localhost")); // add first player to players
		ServerSender sender = new ServerSender(this); // create ServerSender
		new Thread(sender).start(); // start ServerSender

		try
		{
			socket = new DatagramSocket(PORT); // setup socket
			System.out.println("Server> socket init");	
		} catch (Exception e) { System.err.println("Server> socket init: " + e); System.exit(1); }

		while (true) // repeat forever
		{
			data = new byte[2000];
			packet = new DatagramPacket(data, data.length);
			try
			{
				socket.receive(packet); // receive KeyInfo as byte[]
				System.out.println("Server> receive data");
			} catch (Exception e) { System.err.println("Server> receive data: " + e); System.exit(1); }

			KeyInfo ki = null;
			try
			{
				ki = (KeyInfo) byteArrayToObject(data); // convert to KeyInfo
			} catch (Exception e) { System.err.println("Server> data to ki: " + e); System.exit(1); }


                        Player localPlayer = null;

			for (Player player : getPlayers()) // for all players
			{
				if (player.getAddress().getHostAddress().equals(packet.getAddress().getHostAddress())) // if the KeyInfo came from any of you
				{
					localPlayer = player; // be the localPlayer!
					break;
				}
			}

			if (localPlayer == null) // if not
			{
				getPlayers().add(localPlayer = new Player(10, 10, false, packet.getAddress().getHostName())); // create localPlayer
			}

			localPlayer.applyKeyInfo(ki); // apply the KeyInfo to localPlayer
		}
	}
	
	public static void main(String args[])
	{
		PORT = (short) Integer.parseInt(args[0]);
		new Server();
	}

	LinkedList<Player> getPlayers()
	{
		return players;
	}

	Player getServerPlayer()
	{
		return players.get(0);
	}
}
