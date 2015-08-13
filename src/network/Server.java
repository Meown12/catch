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
		Screen.get().addKeyListener(new ServerKeyManager(this));
		players = new LinkedList<Player>();
		players.add(new Player(10, 10, true, "localhost"));
		ServerSender sender = new ServerSender(this);
		new Thread(sender).start(); // starts Sender;

		try
		{
			socket = new DatagramSocket(PORT);
			System.out.println("Server> socket init");	
		} catch (Exception e) { System.err.println("Server> socket init: " + e); System.exit(1); }

		while (true)
		{
			data = new byte[2000];
			packet = new DatagramPacket(data, data.length);
			try	// receive KeyInfo
			{
				socket.receive(packet);
				System.out.println("Server> receive data");
			} catch (Exception e) { System.err.println("Server> receive data: " + e); System.exit(1); }

			KeyInfo ki = null;
			try // cast data to ki
			{
				ki = (KeyInfo) byteArrayToObject(data);
			} catch (Exception e) { System.err.println("Server> data to ki: " + e); System.exit(1); }


                        Player localPlayer = null;

			for (Player player : getPlayers())
			{
				if (player.getAddress().getHostAddress().equals(packet.getAddress().getHostAddress()))
				{
					localPlayer = player;
					break;
				}
			}

			if (localPlayer == null)
			{
				getPlayers().add(localPlayer = new Player(10, 10, false, packet.getAddress().getHostName()));
			}
			localPlayer.applyKeyInfo(ki);
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
