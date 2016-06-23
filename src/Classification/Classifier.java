package Classification;

import Primitives.ClassInfo;
import Primitives.DocInfo;
import Primitives.OccurenceHolder;
import Primitives.Term;
import com.sun.org.apache.xml.internal.utils.Trie;

import java.util.Vector;

public class Classifier {
    private Vector<ClassInfo> classes;
    private Trie wordClassDictionary; // words and their class vector
    private Vector<String> words;

    public Classifier()
    {
        classes = new Vector<ClassInfo>();
        wordClassDictionary = new Trie();
    }

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
            WordOccurenceInAllClasses classVector = (WordOccurenceInAllClasses) wordClassDictionary.get(term.content);
            if(classVector == null)
            {
                classVector = new WordOccurenceInAllClasses(new Vector<WordOccurenceInClass>(), 0);
                wordClassDictionary.put(term.content, classVector);
                words.add(term.content);
            }
            WordOccurenceInClass classData = getWordOccurenceInClassFromVector(classVector, classInfo);
            if(classData == null)
            {
                classData = new WordOccurenceInClass(classInfo, 0);
                classVector.area.add(classData);
            }
            classVector.occurences += term.occurences;
            classData.occurences += term.occurences;

        }
    }

    private WordOccurenceInClass getWordOccurenceInClassFromVector(WordOccurenceInAllClasses classVector, ClassInfo classInfo)
    {
        for(WordOccurenceInClass temp: classVector.area)
            if(temp.area == classInfo)
                return temp;
        return null;
    }

    private boolean classVectorContainsClassInfo(WordOccurenceInAllClasses classVector, ClassInfo classInfo)
    {
        for(WordOccurenceInClass temp: classVector.area)
            if(temp.area == classInfo)
                return true;
        return false;
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

    public double getWordOccurenceProbabilityInAllClasses(String word)
    {
        return ((WordOccurenceInAllClasses)wordClassDictionary.get(word)).area.size()/(double)(classes.size());
    }

    public double getWordOccurenceProbabilityInClass(String word, int classInfoIndex) // P(wi, ck)
    {
        // TODO
        return -1;
    }

    public double getAverageWordOccurenceInClass(String word, int classInfoIndex)
    {
        return classes.elementAt(classInfoIndex).getAverageWordOccurence(word);
    }

    public double getClassUseProbability(int classInfoIndex) // P(ck)
    {
        long totalDocs = 0;
        for(ClassInfo classInfo : classes)
            totalDocs += classInfo.getDocs().size();
        return classes.elementAt(classInfoIndex).getDocs().size()/((double) totalDocs);
    }

    public double getDocClassMatchProbability(int classInfoIndex, DocInfo doc)
    {
        // TODO
        return -1;
    }

    public String[] getVector(DocInfo doc)
    {
        String[] result = new String[words.size()];
        for(int i = 0; i < words.size(); i++)
        {
            if(doc.getWordsInTrie().get(words.elementAt(i)) == null)
                result[i] = null;
            else
                result[i] = words.elementAt(i);
        }
        return result;
    }

    class WordOccurenceInClass extends OccurenceHolder<ClassInfo>
    {

        public WordOccurenceInClass(ClassInfo classInfo, long occurences)
        {
            super(classInfo, occurences);
        }
    }

    class WordOccurenceInAllClasses extends OccurenceHolder<Vector<WordOccurenceInClass>>
    {

        public WordOccurenceInAllClasses(Vector<WordOccurenceInClass> area, long occurences)
        {
            super(area, occurences);
        }
    }
}

