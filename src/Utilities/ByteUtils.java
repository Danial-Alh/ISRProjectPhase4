package Utilities;

import java.nio.ByteBuffer;

public class ByteUtils
{
    private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    public static byte[] longToBytes(long x)
    {
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes)
    {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static byte[] intToBytes(int x)
    {
        buffer.putInt(0, x);
        return buffer.array();
    }

    public static int bytesToInt(byte[] bytes)
    {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getInt();
    }

    public static byte[] doubleToBytes(double x)
    {
        buffer.putDouble(0, x);
        return buffer.array();
    }

    public static double bytesToDouble(byte[] bytes)
    {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getDouble();
    }
}
