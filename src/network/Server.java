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
	Map<InetAddress, Player> clients;
	KeyEvent ke;
	private byte[] data;
	private static short PORT;
	
	public Server()
	{
		System.out.println("Server> started");
		
		clients = new HashMap<InetAddress, Player>();
		
		ServerSender sender = new ServerSender(this);
		new Thread(sender).start(); //starts Sender;

		try
		{
			socket = new DatagramSocket(PORT);
			System.out.println("Server> socket init");	
		}	catch(Exception e) {System.err.println("Server> socket init: " + e);System.exit(1);}

		while (true)
		{
			data = new byte[2000];
			packet = new DatagramPacket(data, data.length);
			try	//receive KeyEvent
			{
				socket.receive(packet);
				System.out.println("Server> receive data");
			} catch(Exception e) {System.err.println("Server> receive data: " + e); System.exit(1);}

			try //cast data to ke
			{
				ke =(KeyEvent) byteArrayToObject(data);
			} catch(Exception e) {System.err.println("Server> data to ke: " + e); System.exit(1);}
		

                        Player localPlayer;
                        if (clients.containsKey(packet.getAddress()))
                                localPlayer = clients.get(packet.getAddress());
                        else
                                clients.put(packet.getAddress(), localPlayer = new Player(10,10));

                        if (ke.getKeyCode() == KeyEvent.VK_UP) //move
                        {
                                localPlayer.setDirection(2);
                                System.out.println("Server> moved Up");
                        } else if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
                        {
                                localPlayer.setDirection(3);
                                System.out.println("Server> moved Right");
                        } else if (ke.getKeyCode() == KeyEvent.VK_DOWN)
                        {
                                localPlayer.setDirection(4);
                                System.out.println("Server> moved Down");
                        } else if (ke.getKeyCode() == KeyEvent.VK_LEFT)
                        {
                                localPlayer.setDirection(1);
                                System.out.println("Server> moved Left");
                        } else
                        {
                                System.err.println("Server> ke contained no move operation");
                        }
		
		}
	}
	
	public static void main(String args[])
	{
		PORT = (short) Integer.parseInt(args[1]);
		new Server();
	}
}
