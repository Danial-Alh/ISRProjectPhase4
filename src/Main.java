import Classification.Classifier;
import FileManagement.FileParser;
import Primitives.DocInfo;

public class Main {

    public static void main(String[] args) {
        DocInfo result = FileParser.parse("Samples/20_newsgroups/alt.atheism/49960");
        Classifier classifier = new Classifier();
        classifier.addNewDoc(result);
    }
}
