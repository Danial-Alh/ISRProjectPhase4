package Primitives;

import Primitives.Interfaces.Parsable;
import Primitives.Interfaces.Sizeofable;
import Utilities.ByteUtils;

public class TermDocDetail implements Sizeofable, Parsable
{
    private Integer occurences;

    public TermDocDetail() {
        this.occurences = 0;
    }

    public TermDocDetail(Integer occurences)
    {
        this.occurences = occurences;
    }

    @Override
    public byte[] toByteArray()
    {
        byte[] bytes = new byte[sizeof()];
        byte[] occrByte = ByteUtils.intToBytes(occurences);
        System.arraycopy(ByteUtils.intToBytes(occrByte.length), 0, bytes, 0, 1);
        System.arraycopy(occrByte, 0, bytes, 1, occrByte.length);
        return bytes;
    }

    @Override
    public void parseFromByteArray(byte[] input)
    {
        int size = ByteUtils.bytesToInt(new byte[]{input[0]});
        byte[] occrByte = new byte[size];
        System.arraycopy(input, 1, occrByte, 0, occrByte.length);
        occurences = ByteUtils.bytesToInt(occrByte);
    }

    @Override
    public int sizeof()
    {
        return Integer.BYTES+1;
    }

    public Integer getOccurences()
    {
        return occurences;
    }

    public void setOccurences(Integer occurences)
    {
        this.occurences = occurences;
    }

    public void incrementOccurences()
    {
        occurences++;
    }
}
