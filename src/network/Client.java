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
		if (args.length != 3)
		{
			System.out.println("Usage: " + args[0] + " <address> <port>");
			System.exit(1);
		}
		PORT = (short) Integer.parseInt(args[1]);
		try
		{
			ADDR = InetAddress.getByName(args[2]);
		} catch (Exception e) { ADDR = null; System.out.println("Client ERROR> wrong address"); System.exit(1); }
		try
		{
			socket = new DatagramSocket(PORT);
		} catch (Exception e) { System.out.println("Client ERROR> create socket"); System.exit(1); }
		new ClientSender(this);
		run();
	}

	private void run()
	{
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

	static class ClientSender implements KeyListener
	{
		Client client;

		ClientSender(Client client)
		{
			this.client = client;
		}

		public void keyPressed(KeyEvent keyEvent)
		{
			byte[] data = objectToByteArray(keyEvent);
			DatagramPacket packet = new DatagramPacket(data, data.length, client.ADDR, client.PORT);
			try
			{
				client.socket.send(packet);
				System.out.println("ClientSender> sending KeyEvent");
			} catch (Exception e) { System.out.println("ClientSender ERROR> sending KeyEvent "); System.exit(1); }
		}

		public void keyReleased(KeyEvent keyEvent)
		{
			byte[] data = objectToByteArray(keyEvent);
			DatagramPacket packet = new DatagramPacket(data, data.length, client.ADDR, client.PORT);
			try
			{
				client.socket.send(packet);
				System.out.println("ClientSender> sending KeyEvent");
			} catch (Exception e) { System.out.println("ClientSender ERROR> sending KeyEvent "); System.exit(1); }
		}

		public void keyTyped(KeyEvent keyEvent) {}
	}
}
