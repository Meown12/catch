package core;

public class Player
{
	private int x, y;
	private byte direction; // 1 = left;  2 = up; 3 = right; 4= down;
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
	
	public void setDirection(int dir)
	{
		direction =(byte) dir;
	}
}
