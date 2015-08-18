package network;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

import network.Client;
import static misc.Serializer.*;

public class ClientSender implements KeyListener
{
	Client client;

	ClientSender(Client client)
	{
		this.client = client;
	}

	public void keyPressed(KeyEvent keyEvent)
	{
		byte[] data = objectToByteArray(keyEvent); // convert keyEvent to byte[]
		DatagramPacket packet = new DatagramPacket(data, data.length, client.ADDR, client.PORT);
		try
		{
			client.socket.send(packet); // send it to Server
			System.out.println("ClientSender> sending KeyEvent");
		} catch (Exception e) { System.out.println("ClientSender ERROR> sending KeyEvent "); System.exit(1); }
	}

	public void keyReleased(KeyEvent keyEvent)
	{
		byte[] data = objectToByteArray(keyEvent.getKeyCode()); // as above
		DatagramPacket packet = new DatagramPacket(data, data.length, client.ADDR, client.PORT);
		try
		{
			client.socket.send(packet);
			System.out.println("ClientSender> sending KeyEvent");
		} catch (Exception e) { System.out.println("ClientSender ERROR> sending KeyEvent "); System.exit(1); }
	}

	public void keyTyped(KeyEvent keyEvent) {}
}
