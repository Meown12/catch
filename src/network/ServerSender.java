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
		DatagramPacket sendPacket = null;
		while (true) // repeat forever
		{
			data = objectToByteArray(new LinkedList<Player>(server.getPlayers())); // convert players to data
			
			for (int i = 1 /* !!! */; i < server.getPlayers().size(); i++) // for every player EXCEPT of the serverPlayer
			{
				sendPacket = new DatagramPacket(data, data.length, server.getPlayers().get(i).getAddress(), server.PORT);
			
				try
				{
					server.socket.send(sendPacket); // send him all players to render
					System.out.println("ServerSender> send data");
				} catch (Exception e) {System.err.println("ServerSender> send data: " + e); System.exit(1);}
			}

			tick(); // tick all players
			render(); // render all players
		}
	}

	private void render()
	{
		for (Player player : server.getPlayers()) // render all players
			player.render();
		Screen.update(); // update Screen
		
	}

	private void tick()
	{
		for (Player player : server.getPlayers()) // tick all players
			player.tick();
	}
}
