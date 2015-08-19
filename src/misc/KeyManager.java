package misc;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyManager implements KeyListener
{
	public static final int KEYS_LENGTH = 200;
	public byte[] keys = new byte[KEYS_LENGTH];
	public byte[] oldKeys = new byte[KEYS_LENGTH];

	@Override public void keyPressed(KeyEvent keyEvent)
	{
		keys[keyEvent.getKeyCode()] = 1;
	}

	@Override public void keyReleased(KeyEvent keyEvent)
	{
		keys[keyEvent.getKeyCode()] = 0;
	}

	@Override public void keyTyped(KeyEvent keyEvent) {}

	public boolean keysChanged()
	{
		for (int i = 0; i < KEYS_LENGTH; i++)
			if (oldKeys[i] != keys[i])
				return true;
		return false;
	}

	public void updateKeys()
	{
		for (int i = 0; i < KEYS_LENGTH; i++)
			oldKeys[i] = keys[i];
	}
}
