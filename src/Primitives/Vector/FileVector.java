package Primitives.Vector;

import FileManagement.RandomAccessFileManager;
import Primitives.Interfaces.Parsable;
import Primitives.Interfaces.Sizeofable;
import jdk.internal.util.xml.impl.Pair;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Vector;

public class FileVector<Value extends Sizeofable & Parsable>
{
    private static final int MEMORY_LIMIT = 30;
    private static final long MB = 1024 * 1024;
    protected final int fileID;
    private final int INDEX_DATA_SIZE = 50;
    private final Long NEXT_INDEX_Ptr_LOC_REL_TO_CURR_INDEX = (long) (Long.BYTES * INDEX_DATA_SIZE);
    private final Class valueClassType;
    private int numberOfItemsAdded;
    private HashMap<Integer, Value> cache;

    public FileVector(Class valueClassType, int fileID)
    {
        this.valueClassType = valueClassType;
        this.fileID = fileID;
        numberOfItemsAdded = 0;
        cache = new HashMap<>();
    }

    public Value elementAt(Long indexPtr, int offset)
    {
        if (indexPtr == -1 || indexPtr == null)
            return null;
        Value cacheResult = cache.get(offset);
        if(cacheResult != null)
            return cacheResult;
        Long valuePtrOnFile = findValuePtrOnFile(indexPtr, offset);
        if (valuePtrOnFile == null)
            return null;
        Value result = readValueAt(valuePtrOnFile);
        cache.put(offset, result);
        checkMemoryAndFree();
        return result;
    }

    private void checkMemoryAndFree() {
        if (memoryLimitExceeded())
        {
            cache.clear();
            Runtime.getRuntime().gc();
        }
    }

    private boolean memoryLimitExceeded() {
        double freeMomory = Runtime.getRuntime().freeMemory() / MB;
        if (freeMomory < MEMORY_LIMIT)
        {
//            System.out.println("Memory freeing -- >> freeMemory: " + freeMomory);
            return true;
        }
        return false;
    }

    public Long writeElementAt(Long indexPtr, int offset, Value value)
    {
        if (indexPtr == null || indexPtr == -1)
            indexPtr = createNewIndex();
        if(cache.get(offset) != null)
        {
            cache.remove(offset);
            cache.put(offset, value);
        }
        Long valuePtrOnFile = createIndexAndFindValuePtrOnFile(indexPtr, offset);
        writeValueAt(valuePtrOnFile, value);
        return indexPtr;
    }

    private Long createIndexAndFindValuePtrOnFile(Long indexPtr, int offset)
    {
        RandomAccessFile instance = RandomAccessFileManager.getInstance(fileID);
        Long tempIndexPtr = indexPtr;
        while (true)
        {
            try
            {
                if (offset < INDEX_DATA_SIZE)
                {
                    instance.seek(tempIndexPtr + offset * Long.BYTES);
                    Long resPtr = instance.readLong();
                    if (resPtr == -1)
                        resPtr = writeNewValueOnFile(tempIndexPtr + offset * Long.BYTES);
                    return resPtr;
                }

                instance.seek(tempIndexPtr + NEXT_INDEX_Ptr_LOC_REL_TO_CURR_INDEX);
                Long tempNextIndexPtr = instance.readLong();
                if (tempNextIndexPtr == -1)
                {
                    tempNextIndexPtr = createNewIndex();
                    instance.seek(tempIndexPtr + NEXT_INDEX_Ptr_LOC_REL_TO_CURR_INDEX);
                    instance.writeLong(tempNextIndexPtr);
                }
                tempIndexPtr = tempNextIndexPtr;
                offset -= INDEX_DATA_SIZE;

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private Value readValueAt(Long valuePtrOnFile)
    {
        RandomAccessFile instance = RandomAccessFileManager.getInstance(fileID);
        Value value = null;
        try
        {
            value = (Value) valueClassType.newInstance();
            if (valuePtrOnFile < 0)
                System.out.println("ljlskdjfl");
            instance.seek(valuePtrOnFile);
            int size = instance.readInt();
            if (size == -1)
                return null;
            byte tempByteArray[] = new byte[size];
            instance.read(tempByteArray);
            value.parseFromByteArray(tempByteArray);
        } catch (InstantiationException | IllegalAccessException | IOException e)
        {
            e.printStackTrace();
        }
        return value;
    }

    private void writeValueAt(Long valuePtrOnFile, Value value)
    {
        RandomAccessFile instance = RandomAccessFileManager.getInstance(fileID);
        try
        {
            instance.seek(valuePtrOnFile);
            byte[] bytes = value.toByteArray();
            instance.writeInt(bytes.length);
            instance.write(bytes);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private Long writeNewValueOnFile(Long ptrInIndex)
    {
        RandomAccessFile instance = RandomAccessFileManager.getInstance(fileID);
        Long resPtr = null;
        try
        {
            resPtr = instance.getChannel().size();
            instance.seek(ptrInIndex);
            instance.writeLong(resPtr);
            instance.seek(resPtr);
            instance.writeInt(-1);
            instance.write(new byte[((Value) valueClassType.newInstance()).sizeof()]);
            numberOfItemsAdded++;
        } catch (IOException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return resPtr;
    }

    private Long findValuePtrOnFile(Long indexPtr, int offset)
    {
        RandomAccessFile instance = RandomAccessFileManager.getInstance(fileID);
        Long tempIndexPtr = indexPtr;
        while (true)
        {
            try
            {
                if (offset < INDEX_DATA_SIZE)
                {
                    instance.seek(tempIndexPtr + offset * Long.BYTES);
                    Long resPtr = instance.readLong();
                    return resPtr == -1 ? null : resPtr;
                }

                instance.seek(tempIndexPtr + NEXT_INDEX_Ptr_LOC_REL_TO_CURR_INDEX);
                Long tempNextIndexPtr = instance.readLong();
                if (tempNextIndexPtr == -1)
                    return null;
                tempIndexPtr = tempNextIndexPtr;
                offset -= INDEX_DATA_SIZE;
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Long createNewIndex()
    {
        Long returnAdd = null;
        RandomAccessFile instance = RandomAccessFileManager.getInstance(fileID);
        try
        {
            instance.seek(instance.getChannel().size());
            returnAdd = instance.getFilePointer();
            for (int i = 0; i <= INDEX_DATA_SIZE; i++) // one more write for ptr at end to next index
                instance.writeLong(-1);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return returnAdd;
    }


    public Vector<Value> getAllElements(Long indexPtr)
    {
        RandomAccessFile instance = RandomAccessFileManager.getInstance(fileID);
        Vector<Value> resultVector = new Vector<>();
        long tempIndexPtr = indexPtr;
        try
        {
            while (true)
            {
                instance.seek(tempIndexPtr);
                for (int i = 0; i < INDEX_DATA_SIZE; i++)
                {
                    long valuePtr = instance.readLong();
                    if (valuePtr == -1)
                        return resultVector;
                    Value value = readValueAt(valuePtr);
                    if (value != null)
                        resultVector.add(value);
                }
                long nextIndexPtr = instance.readLong();
                if (nextIndexPtr == -1)
                    return resultVector;
                tempIndexPtr = nextIndexPtr;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return resultVector;
    }

    public int size()
    {
        return numberOfItemsAdded;
    }

}
