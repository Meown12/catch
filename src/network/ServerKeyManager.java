package network;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import network.Server;

public class ServerKeyManager implements KeyListener
{
	Server server;

	ServerKeyManager(Server server)
	{
		this.server = server;
	}

	public void keyPressed(KeyEvent keyEvent)
	{
		server.getServerPlayer().applyKeyEvent(keyEvent); // give keyEvent to the serverPlayer
	}

	public void keyReleased(KeyEvent keyEvent)
	{
		server.getServerPlayer().applyKeyEvent(keyEvent);
	}

	public void keyTyped(KeyEvent keyEvent) {}
}
