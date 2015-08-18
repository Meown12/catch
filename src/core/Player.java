package core;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.awt.Color;
import java.net.InetAddress;

public class Player implements Serializable
{
	public static final int SPEED = 5, SIZE = 20;
	private byte w = 0, a = 0, s = 0, d = 0;
	private boolean running = false;
	private int x, y;
	private InetAddress address;

	public Player(int x, int y, boolean running, String addressString)
	{
		this.x = x;
		this.y = y;
		this.running = running;
		try
		{
			this.address = InetAddress.getByName(addressString);
		} catch (Exception e) { System.out.println("Player> set address: " + e); System.exit(1); }
	}

	public void tick()
	{
		// move
		x += (d-a)*SPEED;
		y += (s-w)*SPEED;
	}

	public void applyKeyEvent(KeyEvent keyEvent)
	{
		if (keyEvent.getID() == KeyEvent.KEY_PRESSED) // is the keyEvent represents a key Press
		{
			if (keyEvent.getKeyCode() == KeyEvent.VK_UP) // if it's the up-arrow
				w = 1;
			else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) // .. left-arrow
				a = 1;
			else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) // .. down-arrw
				s = 1;
			else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) // .. right-arrow
				d = 1;
			else
				System.out.println("bad key ID: " + keyEvent.getKeyCode());
		}
		else // if the keyEvent represents a key Release
		{
			if (keyEvent.getKeyCode() == KeyEvent.VK_UP) // as above
				w = 0;
			else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT)
				a = 0;
			else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN)
				s = 0;
			else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT)
				d = 0;
			else
				System.out.println("bad key ID: " + keyEvent.getKeyCode());
		}
	}

	public void move(int xOffset, int yOffset)
	{
		x += xOffset;
		y += yOffset;
	}

	public void render()
	{
		if (running)
		{
			Screen.g().setColor(Color.GREEN);
			Screen.g().fillRect(x, y, SIZE, SIZE);
		}
		else
		{
			Screen.g().setColor(Color.RED);
			Screen.g().fillRect(x, y, SIZE, SIZE);
		}
	}

	public boolean collide(Player player)
	{
		if (player == this)
			return false;

		if (Math.abs(x-player.x) < SIZE && Math.abs(y-player.y) < SIZE)
			return true;

		return false;
	}

	public void setRunning(boolean running) { this.running = running; }
	public void setPosition(int x, int y) { this.x = x; this.y = y; }

	public InetAddress getAddress() { return address; }
	public boolean isRunning() { return running; }
}
