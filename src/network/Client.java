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
			ADDR = InetAddress.getByName(args[0]);
		} catch (Exception e) { ADDR = null; System.out.println("Client ERROR> wrong address"); System.exit(1); }
		PORT = (short) Integer.parseInt(args[1]);
		try
		{
			socket = new DatagramSocket(PORT);
		} catch (Exception e) { System.out.println("Client ERROR> create socket"); System.exit(1); }
		run();
	}

	private void run()
	{
		Screen.init();
		Screen.get().addKeyListener(new ClientSender(this));
		while (true)
		{
			byte[] data = new byte[2000];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try
			{
				socket.receive(packet);
			} catch (Exception e) { System.out.println("Client> receive"); System.exit(1); }

			for (Player player : (LinkedList<Player>) byteArrayToObject(packet.getData()))
			{
				player.render();
			}
		}
	}
}
