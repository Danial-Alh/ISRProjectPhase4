package FileManagement; /**
 * Created by 9231020 on 5/1/2016.
 */

import sun.nio.cs.StandardCharsets;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;


public class FileReader {
    String address;
    /*DataInputStream dis = null;
    InputStream fis = null;*/
    File file;
    Scanner scanner;
//    InputStream is = null;
//    InputStreamReader isr = null;
//    BufferedReader br = null;


    public FileReader(String address) {
        this.address = address;
        file = new File(this.address);
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        try {
//            // open input stream test.txt for reading purpose.
//            is = new FileInputStream(this.address);
//
//            // create new input stream reader
//            isr = new InputStreamReader(is);
//
//            // create new buffered reader
//            br = new BufferedReader(isr);
//
////        try {
//////            fis = new FileInputStream(this.address);
//////            dis = new DataInputStream(fis);
////
////        } catch (FileNotFoundException e) {
////            System.out.println("khar!!!!");
////            e.printStackTrace();
////        }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    public Vector<String> readWithBufferSize(int bufferSize) {

        Vector<String> strings = new Vector<String>();
        String token = "";
        int size = 0;
        while (scanner.hasNext()){
            token = scanner.next();
            size += token.length();
            strings.add(token);
            if (token.equals("<مقاله>")){
                return strings;
            }
        }
        if (strings.size() == 0) {
            return null;
        }
        return strings;
    }


    // creates buffer
            /*char[] cbuf = new char[bufferSize/2];
            int max_size= 0;
            try {
                max_size = is.available();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            for (int i=0; i<(max_size/bufferSize); i++)
            {
                try {
                    br.read(cbuf, i*bufferSize, bufferSize);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.out.print(cbuf);
            }
            try {
                br.read(cbuf, max_size/bufferSize, max_size%bufferSize);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            for (int i=0; i<=max_size%bufferSize; i++)
            {
                System.out.print(cbuf[i]);
            }
        // releases resources associated with the streams
        return new String(cbuf);*/
//    }


//        try {
//            int max_val = fis.available(),
//                    count = fis.available();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////         create buffer
//        byte[] bs = new byte[size];
//        // read data into buffer
//        String result = "";
//        int size_of_readed_bytes = -1;
//        try {
//            size_of_readed_bytes = dis.read(bs);
//            if (size_of_readed_bytes == -1) {
//                return "";
//            }
//            result = new String(bs);
//            if (size_of_readed_bytes != size) {
////                result = result.trim();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // for each byte in the buffer
//        return result;
//    }
}
