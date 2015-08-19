package network;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.*;

import core.Screen;
import core.Player;
import misc.KeyManager;
import static misc.Serializer.*;

public class Client
{
	public final short PORT = 4200;
	public InetAddress ADDR;
	private DatagramSocket socket;
	private KeyManager keyManager;

	public static void main(String args[]) // <address>
	{
		new Client(args);
	}

	private Client(String args[])
	{
		if (args.length != 1) // if there not one argument
		{
			System.out.println("Usage: ... <address>");
			System.exit(1); // quit!
		}
		try
		{
			ADDR = InetAddress.getByName(args[0]); // setup ADDR
		} catch (Exception e) { ADDR = null; System.out.println("Client ERROR> wrong address"); System.exit(1); }
		try
		{
			socket = new DatagramSocket(PORT); // create socket
			System.out.println("Client> create socket");
		} catch (Exception e) { System.out.println("Client ERROR> create socket"); System.exit(1); }
		Screen.init(); // init Screen
		Screen.get().addKeyListener(keyManager = new KeyManager()); // add keyManager onto Screen
		run(); // goto run()
	}

	private void run()
	{
		new Timer().scheduleAtFixedRate(new TimerTask()
		{
			@Override public void run()
			{
				send(); // send keys to Server if they changed
			}
		}, 10, 10); // TODO change?

		while (true) // repeat forever
		{
			render(receive());
		}
	}

	private void send() // if keys changed: send them to server
	{
		if (keyManager.keysChanged()) // are the keys changed?
		{
			DatagramPacket packet = new DatagramPacket(keyManager.keys, keyManager.keys.length, ADDR, PORT);
			try
			{
				socket.send(packet); // send it to Server
			} catch (Exception e) { System.out.println("Client ERROR> sending KeyEvent "); System.exit(1); }
		}
		keyManager.updateKeys(); // reset key states
	}

	private LinkedList<Player> receive() // receive players from Server and returns
	{
		byte[] data = new byte[2000];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try
		{
			socket.receive(packet); // receive LinkedList<Player>
		} catch (Exception e) { System.out.println("Client ERROR> receive"); System.exit(1); }

		LinkedList<Player> players = (LinkedList<Player>) byteArrayToObject(packet.getData()); // convert it to LinkedList<Player>
		if (players == null) // if it failed
		{
			System.out.println("Client> Cannot convert the data to a LinkedList<Player>"); // dump me an information
			System.exit(1); // and quit
		}
		return players;
	}

	private void render(LinkedList<Player> players) // renders all players
	{
		for (Player player : players) // for all players
		{
			player.render(); // render
		}
		Screen.update(); // update screen
	}
}
