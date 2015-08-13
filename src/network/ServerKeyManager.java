package network;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import network.Server;
import misc.KeyInfo;

public class ServerKeyManager implements KeyListener
{
	Server server;

	ServerKeyManager(Server server)
	{
		this.server = server;
	}

	public void keyPressed(KeyEvent keyEvent)
	{
		KeyInfo ki = new KeyInfo(keyEvent.getKeyCode(), true); // convert KeyEvent to KeyInfo
		server.getServerPlayer().applyKeyInfo(ki); // give it to the serverPlayer
	}

	public void keyReleased(KeyEvent keyEvent)
	{
		KeyInfo ki = new KeyInfo(keyEvent.getKeyCode(), false); // as above
		server.getServerPlayer().applyKeyInfo(ki);
	}

	public void keyTyped(KeyEvent keyEvent) {}
}
