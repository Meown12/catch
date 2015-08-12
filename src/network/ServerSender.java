package network;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.*;
import java.awt.Color;
import java.awt.Dimension;
import java.util.*;

import static misc.Serializer.*;
import network.Server;
import core.Player;

public class ServerSender implements Runnable
{
	
	Server server;
	DatagramPacket packet;		
	byte[] data;

	public ServerSender(Server server) 
	{
		this.server = server;
		System.out.println("ServerSender> started");
	}
		
	public void run()
	{
		while (true)
		{
			data = objectToByteArray(new LinkedList<Player>(server.clients.values()));
			
			for (InetAddress addr : server.clients.keySet())
			{
				packet = new DatagramPacket(data, data.length, addr, server.PORT);
			
				try
				{
					server.socket.send(packet);
					System.out.println("ServerSender> send data");
				} catch (Exception e) {System.err.println("ServerSender> send data: " + e); System.exit(1);}
			}
			
			try
			{
				Thread.sleep(20);
			} catch (Exception e) {}
			tick();
		}
	}

	public void tick()
	{
		for (Player player : server.clients.values())
			player.tick();
	}
}
