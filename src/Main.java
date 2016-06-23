import FileManagement.FileParser;
import javafx.util.Pair;

import java.util.Vector;

public class Main {

    public static void main(String[] args) {
        Vector<Pair<String, String>> result = FileParser.parse("Samples/20_newsgroups/alt.atheism/49960");
        System.out.println(result);
    }
}
