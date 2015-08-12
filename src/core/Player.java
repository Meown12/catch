package core;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.awt.Color;

import misc.KeyInfo;

public class Player implements Serializable
{
	public static final int SPEED = 5, SIZE = 200;
	private byte w = 0, a = 0, s = 0, d = 0;
	private boolean runner = false;
	private int x, y;

	public Player(int x, int y, boolean runner)
	{
		this.x = x;
		this.y = y;
		this.runner = runner;
	}

	public void tick()
	{
		x += (d-a)*SPEED;
		y += (s-w)*SPEED;
	}

	public void applyKeyInfo(KeyInfo keyInfo)
	{
		if (keyInfo.isPressed())
		{
			if (keyInfo.getKey() == KeyEvent.VK_UP)
				w = 1;
			else if (keyInfo.getKey() == KeyEvent.VK_LEFT)
				a = 1;
			else if (keyInfo.getKey() == KeyEvent.VK_DOWN)
				s = 1;
			else if (keyInfo.getKey() == KeyEvent.VK_RIGHT)
				d = 1;
			else
				System.out.println("bad key ID: " + keyInfo.getKey());
		}
		else
		{
			if (keyInfo.getKey() == KeyEvent.VK_UP)
				w = 0;
			else if (keyInfo.getKey() == KeyEvent.VK_LEFT)
				a = 0;
			else if (keyInfo.getKey() == KeyEvent.VK_DOWN)
				s = 0;
			else if (keyInfo.getKey() == KeyEvent.VK_RIGHT)
				d = 0;
			else
				System.out.println("bad key ID: " + keyInfo.getKey());
		}
	}

	public void move(int xOffset, int yOffset)
	{
		x += xOffset;
		y += yOffset;
	}

	public void render()
	{
		Screen.g().setColor(Color.RED);
		Screen.g().fillRect(x, y, SIZE, SIZE);
		System.out.println("x={" + x + "} y={" + y + "}");
	}
}
