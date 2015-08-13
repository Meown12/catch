package network;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.*;
import java.awt.Color;
import java.awt.Dimension;
import java.util.*;

import static misc.Serializer.*;
import core.Player;
import core.Screen;
import misc.KeyInfo;

public class Server
{
	public static short PORT;
	DatagramPacket packet;
	DatagramSocket socket;
	Map<InetAddress, Player> clients;
	private Player serverPlayer;
	private byte[] data;
	
	public Server()
	{
		System.out.println("Server> started");
		Screen.init();
		Screen.get().addKeyListener(new ServerKeyManager(this));
		clients = new HashMap<InetAddress, Player>();
		serverPlayer = new Player(10, 10, true);
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


                        Player localPlayer;
                        if (clients.containsKey(packet.getAddress()))
                                localPlayer = clients.get(packet.getAddress());
                        else
                                clients.put(packet.getAddress(), localPlayer = new Player(10,10, false));
			
			localPlayer.applyKeyInfo(ki);
		}
	}
	
	public static void main(String args[])
	{
		PORT = (short) Integer.parseInt(args[0]);
		new Server();
	}

	Player getPlayer()
	{
		return serverPlayer;
	}
}
