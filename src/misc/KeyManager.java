package misc;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import misc.Debug;

public class KeyManager implements KeyListener // handler of key-events, client and server have one
{
	public static final int KEYS_LENGTH = 200; // amount of keys in the keys array
	public byte[] keys = new byte[KEYS_LENGTH]; // keys of this frame
	public byte[] oldKeys = new byte[KEYS_LENGTH]; // keys of last frame (needed to check differences in keysChanged())

	@Override public void keyPressed(KeyEvent keyEvent) // if key pressed
	{
		Debug.timeLog("keyPressed");
		keys[keyEvent.getKeyCode()] = 1; // set the key to 1
	}

	@Override public void keyReleased(KeyEvent keyEvent) // if key released
	{
		Debug.timeLog("keyReleased");
		keys[keyEvent.getKeyCode()] = 0; // set the key to 0
	}

	@Override public void keyTyped(KeyEvent keyEvent) {} // keyTyped is just ignored.

	public boolean keysChanged() // check if a keyEvent happened
	{
		for (int i = 0; i < KEYS_LENGTH; i++) // for all keys
			if (oldKeys[i] != keys[i]) // if there is a difference
				return true; // return true
		return false; // else .. nope.
	}

	public void updateKeys() // resets key states
	{
		for (int i = 0; i < KEYS_LENGTH; i++)
			oldKeys[i] = keys[i];
	}
}
