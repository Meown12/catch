package core;

public class Player
{
	public static final int LEFT = 1, UP = 2, RIGHT = 3, DOWN = 4;

	private int x, y;
	private byte direction;

	public Player(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void move(int xOffset, int yOffset)
	{
		x += xOffset;
		y += yOffset;
	}

	public void render()
	{
		Screen.g().fillRect(x, y, 20, 20);
	}
	
	public byte getDirection()
	{
		return direction;
	}
	
	public void setDirection(int direction)
	{
		this.direction = (byte) direction;
	}
}
