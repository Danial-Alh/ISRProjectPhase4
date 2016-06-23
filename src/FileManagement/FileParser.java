package FileManagement;

import javafx.util.Pair;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Vector;

public class FileParser {

    public static Vector<Pair<String, String>> parse(String filePath)
    {
        File file = new File(filePath);
        return parse(file);
    }
    public static Vector<Pair<String, String>> parse(File f)
    {
        Vector<Pair<String, String>> result = new Vector<Pair<String, String>>();

        InputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);


        String line = "";
        try
        {
            while ((line = br.readLine()) != null)
            {
                String[] tokens = line.split(" ");
                if(tokens.length == 0)
                    continue;
                String lineTitle = tokens[0];
                String key = null, value = null;

                for(int i = 0; i < DocFieldKeys.KEY.length; i++ )
                    if( lineTitle.toLowerCase().startsWith(DocFieldKeys.KEY[i] + ":") )
                    {
                        key = DocFieldKeys.KEY[i];
                        break;
                    }
                if( key == null )
                    continue;

                try {
                    value = line.substring(key.length() + 2);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                if( key.equalsIgnoreCase(DocFieldKeys.KEY[0]) )
                    value = value.split(",")[0];
                else if( key.equalsIgnoreCase(DocFieldKeys.KEY[2]) )
                {
                    int numberOfLines = Integer.parseInt(tokens[1]);
                    value = "";
                    for(int i = 0; i < numberOfLines; i++)
                    {
                        line = br.readLine();
                        if(!line.trim().equalsIgnoreCase(""))
                            value += line + "\n";
                    }
                }

                result.add(new Pair<String, String>(key, value));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
