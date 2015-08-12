package core;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;

import core.Main;

public class Screen extends JPanel
{
	public static final int SCREEN_WIDTH = 1920, SCREEN_HEIGHT = 1080;
	private static JFrame frame;
	private static Screen instance = new Screen();
	private static BufferedImage buffer;
	private static Graphics g; // graphics for the buffer

	private Screen()
	{
		// init stuff
		frame = new JFrame("CatchGame");
		frame.add(this);
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		buffer = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g = buffer.getGraphics();
		frame.setFocusable(true);
		setFocusable(true);
		requestFocusInWindow();
	}

	static void update()
	{
		instance.getGraphics().drawImage(buffer, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null); // renders buffer on screen
		g.dispose(); // disposes g
		g = buffer.getGraphics(); // setups g (used in Screen.draw(...))
		g.setColor(Color.BLACK); // clears ...
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT); // ... screen
	}

	public static Graphics g() { return g; }
	public static Screen get() { return instance; }
