package Classification;

import Primitives.ClassInfo;
import Primitives.DocInfo;
import Primitives.Term;
import com.sun.org.apache.xml.internal.utils.Trie;

import java.util.Vector;

public class Classifier {
    private Vector<ClassInfo> classes;
    private Trie wordClassDictionary;

    public void addNewDoc(DocInfo newDoc)
    {
        ClassInfo classInfo = getCorrespondingClassInfo(newDoc.getNewsGroup());
        if(classInfo == null)
        {
            classInfo = new ClassInfo(newDoc.getNewsGroup());
            classes.add(classInfo);
        }
        classInfo.addNewDoc(newDoc);
        addDocWordsToDictionary(newDoc, classInfo);

    }

    private void addDocWordsToDictionary(DocInfo newDoc, ClassInfo classInfo)
    {
        for(Term term : newDoc.getWords())
        {
            Vector<ClassInfo> classVector = (Vector<ClassInfo>) wordClassDictionary.get(term.content);
            if(classVector == null)
            {
                classVector = new Vector<ClassInfo>();
                wordClassDictionary.put(term.content, classVector);
            }
            if(!classVector.contains(classInfo))
            {
                classVector.add(classInfo);
            }
        }
    }

    private ClassInfo getCorrespondingClassInfo(String newsGroup)
    {
        for(int i = 0; i < classes.size(); i++)
            if(classes.elementAt(i).getNewsGroup().equalsIgnoreCase(newsGroup))
                return classes.elementAt(i);
        return null;
    }

    public void findClassSpecifiers()
    {

    }

    public double getWordOccurenceProbabilityInAllNewsGroup(String word)
    {
        return -1;
    }

    public double getWordOccurenceProbabilityInNewsGroup(String word, int classInfoIndex) // P(wi, ck)
    {
        return -1;
    }

    public double getAverageWordOccurenceInNewsGroup(String word, int classInfoIndex)
    {
        return -1;
    }

    public double getNewsGroupUseProbability(int classInfoIndex) // P(ck)
    {
        return -1;
    }

    public double getDocClassMatchProbability(int classInfoIndex, DocInfo doc)
    {
        return -1;
    }

    public String[] getVector(DocInfo doc)
    {
        return null;
    }
}

