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
	boolean[] keys = new boolean[500];

	ClientSender(Client client)
	{
		this.client = client;
	}


	private void send(KeyEvent keyEvent)
	{
		byte[] data = objectToByteArray(keyEvent); // convert keyEvent to byte[]
		DatagramPacket packet = new DatagramPacket(data, data.length, client.ADDR, client.PORT);
		try
		{
			client.socket.send(packet); // send it to Server
			System.out.println("ClientSender> sending KeyEvent");
		} catch (Exception e) { System.out.println("ClientSender ERROR> sending KeyEvent "); System.exit(1); }
	}

	public void keyPressed(KeyEvent keyEvent)
	{
		int code = keyEvent.getKeyCode();
		if (keys[code] == false)
		{
			keys[code] = true;
			send(keyEvent);
		}
	}

	public void keyReleased(KeyEvent keyEvent)
	{
		int code = keyEvent.getKeyCode();
		if (keys[code] == true)
		{
			keys[code] = false;
			send(keyEvent);
		}
	}

	public void keyTyped(KeyEvent keyEvent) {}
}
