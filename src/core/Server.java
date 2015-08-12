package network;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.*;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.util.*;

import static  misc.Serializer.*;
import core.Player;

public class Server
{
	DatagramPacket packet;
	DatagramSocket socket;
	Map<InetAddres, Player> clients;
	KeyEvent ke;
	byte[] data;
	short PORT;
	
	public Server()
	{
		System.out.println("Server> started");
		
		clients = new HashMap<InetAddress, Player>();
		
		ServerSender sender = ServerSender(this);
		new Thread(sender).start(); //starts Sender;

		try
		{
			socket = new DatagramSocket(PORT);
			System.out.println("Server> socket init");	
		}	catch(Exeption e) {System.err.println("Server> socket init: " + e);System.exit(1);}

		while (true)
		{
			data = new byte[2000];
			packet = new DatagramPacket(data, data.length);
			try	//receive keyEvent
			{
				socket.receive(packet);
				System.out.println("Server> receive data");
			} catch(Exeption e) {System.err.println("Server> receive data: " + e); System.exit(1);}

			try //cast data to ke
			{
				ke = byteArrayToObject(data);
			} catch(Exeption e) {System.err.println("Server> data to ke: " + e); System.exit(1);}
		}
	}
	
	public static void main(String args[])
	{
		PORT = args[1];
	
	}
}
