import Classification.Classifier;
import FileManagement.FileParser;
import Primitives.DocInfo;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File folder = new File("Samples/20_newsgroups/");
        Classifier classifier = null;
        for (File groupFolder : folder.listFiles()) {
            File[] docs = groupFolder.listFiles();
            int range = (int) (docs.length * 0.7);
            for (int i = 0; i < range; i++) {
                DocInfo result = FileParser.parse(docs[i]);
                if (result == null)
                    continue;
                classifier = new Classifier();
                classifier.addNewDoc(result);
            }
        }
        classifier.findClassSpecifiers();
    }
}
