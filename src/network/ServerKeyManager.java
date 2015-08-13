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
		KeyInfo ki = new KeyInfo(keyEvent.getKeyCode(), true);
		server.getPlayer().applyKeyInfo(ki);
	}

	public void keyReleased(KeyEvent keyEvent)
	{
		KeyInfo ki = new KeyInfo(keyEvent.getKeyCode(), false);
		server.getPlayer().applyKeyInfo(ki);
	}

	public void keyTyped(KeyEvent keyEvent) {}
}
