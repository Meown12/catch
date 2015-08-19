package core;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.awt.Color;
import java.net.InetAddress;

import misc.KeyManager;

public class Player implements Serializable
{
	public static final int SPEED = 5, SIZE = 20;
	private byte[] keys = new byte[KeyManager.KEYS_LENGTH]; // 
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

	public void tick() // moves player according to <keys>
	{
		// move
		x += (keys[KeyEvent.VK_RIGHT]-keys[KeyEvent.VK_LEFT])*SPEED;
		y += (keys[KeyEvent.VK_DOWN]-keys[KeyEvent.VK_UP])*SPEED;
	}

	public void applyKeys(byte[] keys) // assign this.keys to keys
	{
		for (int i = 0; i < KeyManager.KEYS_LENGTH; i++)
			this.keys[i] = keys[i];
	}

	public void render() // draws rect on screen
	{
		if (running) // if running
		{
			// draw a green rect
			Screen.g().setColor(Color.GREEN);
			Screen.g().fillRect(x, y, SIZE, SIZE);
		}
		else // else
		{
			// draw a red rect
			Screen.g().setColor(Color.RED);
			Screen.g().fillRect(x, y, SIZE, SIZE);
		}
	}

	public boolean collide(Player player)
	{
		if (player == this) // if <player> is yourself
			return false; // you can't really collide ...

		if (Math.abs(x-player.x) < SIZE && Math.abs(y-player.y) < SIZE) // if your x-distance and y-distance are both tinier than your SIZE
			return true; // COLLISION!

		return false; // else nope.
	}

	public void setRunning(boolean running) { this.running = running; } // guess ...
	public void resetPosition() { x = 10; y = 10; } // resets your position back to spawn

	public InetAddress getAddress() { return address; }
	public boolean isRunning() { return running; }
}
