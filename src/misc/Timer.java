package misc;

public abstract class Timer
{
	public Timer()
	{
/*		new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask()
		{
			@Override public void run()
			{
				run();
			}
		}, 600, 600);
*/
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60D;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while (true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				run();
				delta--;
			}
		}
	
	}

	public abstract void run();
}
