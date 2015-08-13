package network;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.LinkedList;

import core.Screen;
import core.Player;
import static misc.Serializer.*;

public class Client
{
	public final short PORT;
	public InetAddress ADDR;

	DatagramSocket socket;

	public static void main(String args[]) // <address> <port>
	{
		new Client(args);
	}

	private Client(String args[])
	{
		if (args.length != 2)
		{
			System.out.println("Usage: ... <address> <port>");
			System.exit(1);
		}
		try
		{
			ADDR = InetAddress.getByName(args[0]); // setup ADDR
		} catch (Exception e) { ADDR = null; System.out.println("Client ERROR> wrong address"); System.exit(1); }
		PORT = (short) Integer.parseInt(args[1]); // setup PORT
		try
		{
			socket = new DatagramSocket(PORT); // create socket
			System.out.println("Client> create socket");
		} catch (Exception e) { System.out.println("Client ERROR> create socket"); System.exit(1); }
		run(); // start run()
	}

	private void run()
	{
		Screen.init();
		Screen.get().addKeyListener(new ClientSender(this)); // add ClientSender to Screen
		while (true) // repeat forever
		{
			byte[] data = new byte[2000];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try
			{
				socket.receive(packet); // receive List<Player>
				System.out.println("Client> receive");
			} catch (Exception e) { System.out.println("Client ERROR> receive"); System.exit(1); }

			LinkedList<Player> players = (LinkedList<Player>) byteArrayToObject(packet.getData()); // convert it to LinkedList<Player>
			if (players == null) // if it failed
			{
				System.out.println("Client> Cannot convert the data to a LinkedList<Player>"); // dump me an information
				System.exit(1); // and quit
			}

			for (Player player : players) // render all players
			{
				player.render();
			}
			Screen.update();
		}
	}
}
