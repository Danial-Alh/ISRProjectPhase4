package FileManagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Vector;

public class RandomAccessFileManager
{
    private static String path = "";
    private static HashMap<String, Integer> fileNames = new HashMap<>();
    private static Vector<RandomAccessFile> instances = new Vector<>();

    protected RandomAccessFileManager()
    {

    }

    public static int createNewInstance(String fileName)
    {
        Integer id = fileNames.get(fileName);
        if (id != null)
            return id;

        System.out.println("instance creating file name" + fileName);
        try
        {
            File file = new File(path + fileName);
            if (file.exists())
            {
                file.delete();
                System.out.println("deleting last index file name" + fileName);
            }
            instances.add(new RandomAccessFile(path + fileName + ".isr", "rw"));
            fileNames.put(fileName, instances.size() - 1);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return instances.size() - 1;
    }

    public static RandomAccessFile getInstance(int id)
    {
        return instances.elementAt(id);
    }

    @Override
    protected void finalize() throws Throwable
    {
        for (RandomAccessFile instance : instances)
            instance.close();
        super.finalize();
    }
}
