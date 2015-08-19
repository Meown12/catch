package misc;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyManager implements KeyListener
{
	public byte[] keys = new byte[200];
	public byte[] oldKeys = new byte[200];

	@Override public void keyPressed(KeyEvent keyEvent)
	{
		int code = keyEvent.getKeyCode();
		keys[code] = 1;
	}

	@Override public void keyReleased(KeyEvent keyEvent)
	{
		int code = keyEvent.getKeyCode();
		keys[code] = 0;
	}

	@Override public void keyTyped(KeyEvent keyEvent) {}
}
