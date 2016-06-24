package Primitives;

import Primitives.Interfaces.Parsable;
import Primitives.Interfaces.Sizeofable;
import Utilities.ByteUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by Danial on 6/24/2016.
 */
public class MyString implements Sizeofable, Parsable {
    public String string;
    public static final int MAX_SIZE = 128;
    public MyString(String string) {
        this.string = string;
    }

    public MyString() {
        string = "";
    }

    @Override
    public byte[] toByteArray() {
        byte[] bytes = new byte[sizeof()];
        byte[] stringByte = string.getBytes();
        if(stringByte.length > MAX_SIZE)
            System.out.println("ohhhhhhhhhnOOOOOO");
        System.arraycopy(ByteUtils.intToBytes(string.length()), 0, bytes, 0, 1);
        System.arraycopy(stringByte, 0, bytes, 1, stringByte.length);
        return bytes;
    }

    @Override
    public void parseFromByteArray(byte[] input) {
        int size = ByteUtils.bytesToInt(new byte[]{input[0]});
        byte[] stringByte = new byte[size];
        System.arraycopy(input, 1, stringByte, 0, stringByte.length);
        try {
            string = new String(stringByte, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int sizeof() {
        return MAX_SIZE+1;
    }
}
