package misc;

public abstract class Timer extends Thread
{
	private final int RATE;

	public Timer()
	{
		RATE = 60;
		this.start();
	}

	public Timer(int rate)
	{
		RATE = rate;
		this.start();
	}

	@Override public final void run()
	{
/*		new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask()
		{
			@Override public void run()
			{
				function();
			}
		}, RATE, RATE);
*/
		long lastTime = System.nanoTime();
		final double amountOfTicks = RATE;
		final double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;
		while (true)
		{
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1)
			{
				function();
				delta--;
			}
		}
	
	}

	public abstract void function();
}
