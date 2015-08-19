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

	public static void main(String args[]) // <address> <port>
	{
		new Client(args);
	}

	private Client(String args[])
	{
		if (args.length != 2)
		{
			System.out.println("Usage: ... <address>");
			System.exit(1);
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
		Screen.init();
		Screen.get().addKeyListener(keyManager = new KeyManager());
		run(); // start run()
	}

	private void run()
	{
		new Timer().scheduleAtFixedRate(new TimerTask()
		{
			@Override public void run()
			{
				send();
			}
		}, 10, 10); // TODO change?

		while (true) // repeat forever
		{
			render(receive());
		}
	}

	private void send()
	{
		if (!keyManager.keys.equals(keyManager.oldKeys))
		{
			DatagramPacket packet = new DatagramPacket(keyManager.keys, keyManager.keys.length, ADDR, PORT);
			try
			{
				socket.send(packet); // send it to Server
				System.out.println("Client> sending KeyEvent");
			} catch (Exception e) { System.out.println("Client ERROR> sending KeyEvent "); System.exit(1); }
		}
		for (int i = 0; i < keyManager.keys.length; i++)
			keyManager.oldKeys[i] = keyManager.keys[i];
	}

	private LinkedList<Player> receive()
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
		return players;
	}

	private void render(LinkedList<Player> players)
	{
		for (Player player : players) // render all players
		{
			player.render();
		}
		Screen.update();
	}
}
