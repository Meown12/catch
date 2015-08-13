package network;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

import network.Client;
import static misc.Serializer.*;
import misc.KeyInfo;

public class ClientSender implements KeyListener
{
	Client client;

	ClientSender(Client client)
	{
		this.client = client;
	}

	public void keyPressed(KeyEvent keyEvent)
	{
		KeyInfo ki = new KeyInfo(keyEvent.getKeyCode(), true); // convert keyEvent to KeyInfo
		byte[] data = objectToByteArray(ki); // convert it to byte[]
		DatagramPacket packet = new DatagramPacket(data, data.length, client.ADDR, client.PORT);
		try
		{
			client.socket.send(packet); // send it to Server
			System.out.println("ClientSender> sending KeyInfo");
		} catch (Exception e) { System.out.println("ClientSender ERROR> sending KeyEvent "); System.exit(1); }
	}

	public void keyReleased(KeyEvent keyEvent)
	{
		byte[] data = objectToByteArray(new KeyInfo(keyEvent.getKeyCode(), false)); // as above
		DatagramPacket packet = new DatagramPacket(data, data.length, client.ADDR, client.PORT);
		try
		{
			client.socket.send(packet);
			System.out.println("ClientSender> sending KeyInfo");
		} catch (Exception e) { System.out.println("ClientSender ERROR> sending KeyEvent "); System.exit(1); }
	}

	public void keyTyped(KeyEvent keyEvent) {}
}
