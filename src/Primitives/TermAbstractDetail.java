package Primitives;

import Primitives.Interfaces.Parsable;
import Primitives.Interfaces.Sizeofable;
import Utilities.ByteUtils;

public class TermAbstractDetail implements Parsable, Sizeofable
{
    private Integer occurences;
    private Long filePtr; // for details stored on file

    public TermAbstractDetail(Integer occurences, Long filePtr)
    {
        this.occurences = occurences;
        this.filePtr = filePtr;
    }

    @Override
    public byte[] toByteArray()
    {
        byte[] bytes = new byte[sizeof()];
        byte[] occrByte = ByteUtils.intToBytes(occurences);
        byte[] ptrByte = ByteUtils.longToBytes(filePtr);
        System.arraycopy(ByteUtils.intToBytes(occrByte.length), 0, bytes, 0, 1);
        System.arraycopy(occrByte, 0, bytes, 1, occrByte.length);
        System.arraycopy(ptrByte, 0, bytes, occrByte.length+1, ptrByte.length);
        return bytes;
    }

    @Override
    public void parseFromByteArray(byte[] input)
    {
        int size = ByteUtils.bytesToInt(new byte[]{input[0]});
        byte[] occrByte = new byte[size];
        byte[] ptrByte = new byte[input.length-size-1];
        System.arraycopy(input, 1, occrByte, 0, occrByte.length);
        System.arraycopy(input, occrByte.length+1, ptrByte, 0, ptrByte.length);
        occurences = ByteUtils.bytesToInt(occrByte);
        filePtr = ByteUtils.bytesToLong(ptrByte);
    }

    @Override
    public int sizeof()
    {
        return Integer.BYTES + Long.BYTES + 1; // for store size
    }

    public Integer getOccurences()
    {
        return occurences;
    }

    public void setOccurences(Integer occurences)
    {
        this.occurences = occurences;
    }

    public Long getFilePtr()
    {
        return filePtr;
    }

    public void setFilePtr(Long filePtr)
    {
        this.filePtr = filePtr;
    }

    public void incrementOccurences()
    {
        occurences++;
    }
}
