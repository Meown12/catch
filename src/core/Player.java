package core;

public class Player
{
	int x, y;

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
}
