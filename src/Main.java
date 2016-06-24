import Classification.Classifier;
import FileManagement.FileParser;
import Primitives.ClassInfo;
import Primitives.DocInfo;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        int numberOfGroups = 3;






        File folder = new File("Samples/20_newsgroups/");
        Classifier classifier = null;
        classifier = new Classifier();
        File[] groups = folder.listFiles();
        for (int j = 0 ; j < numberOfGroups; j++ ) {
            File groupFolder = groups[j];
            System.out.println("group: " + groupFolder.getName());
            File[] docs = groupFolder.listFiles();
            int range = (int) (docs.length * 0.7);
            for (int i = 0; i < range; i++) {
                DocInfo result = FileParser.parse(docs[i]);
                if (result == null || result.getWords().size() == 0)
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
        int n= classifier.getClasses().size();
        int confusionMatrix [][]= new int[n][n];

        //Confusion Matrix:
        for (int i=0; i<n; i++)
        {
            for (int j=0; j<n; j++)
            {
                confusionMatrix[i][j]=0;
            }
        }
        for (int j = 0 ; j < numberOfGroups; j++ ) {
            File groupFolder = groups[j];
            System.out.println("group: " + groupFolder.getName());
            File[] docs = groupFolder.listFiles();
            int range = (int) (docs.length * 0.7);
            for (int i = range; i < docs.length; i++) {
                DocInfo result = FileParser.parse(docs[i]);
                if (result == null)
                    continue;

//                String newsGroup = classifier.getdocClassMatch(result);
                int index = classifier.getdocClassMatchIndex(result);
//                System.out.println("wanted: " + result.getNewsGroup() + "seen: " + newsGroup);

                    String s;
                    s = result.getNewsGroup();
                    int realIndex=-1;
                    for (int k=0; k<classifier.getClasses().size(); k++)
                    {
                        if (classifier.getClasses().elementAt(k).getNewsGroup().equalsIgnoreCase(s))
                        {
                            realIndex = k;
                            break;
                        }

                    }
                if (realIndex==-1)
                {
                    continue;
                }
                try
                {
                    confusionMatrix[realIndex][index]++;
                }
                catch (Exception e)
                {
                    System.err.println(realIndex+", "+index);
                }


            }
        }
        System.out.println("confusion matrix");
        for (int i=0; i<n; i++)
        {
            for (int j=0; j<n; j++)
            {
                System.out.print(confusionMatrix[i][j]+",");
            }
            System.out.println();
        }

    }
}
