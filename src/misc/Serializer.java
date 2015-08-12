package misc;

import java.io.*;
import java.util.*;

public final class Serializer
{
	private Serializer() {}

	public static byte[] objectToByteArray(Object object) // serialize
	{
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.close();
			return bos.toByteArray();
		} catch (Exception e) {System.out.println(e.getMessage());}
		return null;
	}

	public static Object byteArrayToObject(byte[] bytes) // deserialize
	{
		try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes)))
		{
			return in.readObject();
		} catch (Exception e){System.out.println(e.getMessage());}
		return null;
	}
}
