package misc;

import java.text.DecimalFormat;

public class TimeChecker
{
	private static long lastTime = 0;

	private TimeChecker() {}

	public static void checkPoint(String string)
	{
		DecimalFormat df = new DecimalFormat("#.###");
		System.out.println(string + ": " + df.format((System.nanoTime() - lastTime)/1000000000D) + " seconds");
		lastTime = System.nanoTime();
	}
}
