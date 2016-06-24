import Classification.Classifier;
import FileManagement.FileParser;
import Primitives.ClassInfo;
import Primitives.DocInfo;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File folder = new File("Samples/20_newsgroups/");
        Classifier classifier = null;
        classifier = new Classifier();
        File[] groups = folder.listFiles();
        for (int j = 0 ; j < 5; j++ ) {
            File groupFolder = groups[j];
            System.out.println("group: " + groupFolder.getName());
            File[] docs = groupFolder.listFiles();
            int range = (int) (docs.length * 0.7);
            for (int i = 0; i < range; i++) {
                DocInfo result = FileParser.parse(docs[i]);
                if (result == null)
                    continue;
                classifier.addNewDoc(result);
            }
        }
        classifier.findClassSpecifiers();
        for (ClassInfo classInfo : classifier.getClasses()) {
            System.out.println("\t\t*****category\t:\t" + classInfo.getNewsGroup() + "*****");
            for (String word : classInfo.getSpecifiers())
                System.out.println(word);
        }

//        for (File groupFolder : folder.listFiles()) {
//            System.out.println("group: " + groupFolder.getName());
//            File[] docs = groupFolder.listFiles();
//            int range = (int) (docs.length * 0.7);
//            for (int i = range; i < docs.length; i++) {
//                DocInfo result = FileParser.parse(docs[i]);
//                if (result == null)
//                    continue;
//
//                classifier.getdocClassMatch(result);
//            }
//        }
    }
}
