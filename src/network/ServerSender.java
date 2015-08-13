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
import core.Screen;

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
			data = objectToByteArray(new LinkedList<Player>(server.getPlayers()));
			
			for (int i = 1; i < server.getPlayers().size(); i++)
			{
				packet = new DatagramPacket(data, data.length, server.getPlayers().get(i).getAddress(), server.PORT);
			
				try
				{
					server.socket.send(packet);
					System.out.println("ServerSender> send data");
				} catch (Exception e) {System.err.println("ServerSender> send data: " + e); System.exit(1);}
			}

			tick();
			render();	

			try
			{
				Thread.sleep(20);
			} catch (Exception e) {}

		}
	}

	private void render()
	{
		for (Player player : server.getPlayers())
			player.render();
		Screen.update();
		
	}

	private void tick()
	{
		for (Player player : server.getPlayers())
			player.tick();
	}
}
