package misc;

import java.text.DecimalFormat;

public class Debug
{
	private static long lastTime = 0;

	private Debug() {}

	public static void log(String string)
	{
		System.out.println(string);
	}

	public static void timeLog(String string)
	{
		if (lastTime == 0)
		{
			System.out.println(string + ": ?");
		}
		else
		{
			DecimalFormat df = new DecimalFormat("#.###");
			System.out.println(string + ": " + df.format((System.nanoTime() - lastTime)/1000000000D) + " seconds");
		}
		lastTime = System.nanoTime();
	}

	public static void quit(String string)
	{
		System.out.println(string);
		System.exit(1);
	}

	public static void timeQuit(String string)
	{
		timeLog(string);
		System.out.println("Debug> quitting.");
		System.exit(1);
	}
}
