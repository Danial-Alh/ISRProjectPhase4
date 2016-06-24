package Classification;

import Primitives.ClassInfo;
import Primitives.DocInfo;
import Primitives.OccurenceHolder;
import Primitives.Term;
import com.sun.org.apache.xml.internal.utils.Trie;
import com.sun.swing.internal.plaf.synth.resources.synth_sv;

import java.util.Vector;

public class Classifier {
    private Vector<ClassInfo> classes;
    private Trie wordClassDictionary; // words and their class vector
    private Vector<String> words;
    private static TermPresence[][] termPresences = null;

    public Classifier()
    {
        classes = new Vector<ClassInfo>();
        wordClassDictionary = new Trie();
        words = new Vector<String>();
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
        for (int i = 0; i < classes.size(); i++) {
            ClassInfo classInfo = classes.elementAt(i);
            Vector<String> specifiers = new Vector<String>();
            Vector<Double> scores = new Vector<Double>();
            for (String word : classInfo.getWords()) {
                double score = calculateWordScoreForClass(word, i);
                int j;
                for (j = 0; j < scores.size() && j <= ClassInfo.specifyingSize; j++)
                    if (score > scores.elementAt(j))
                        break;
                if (j == scores.size()) {
                    specifiers.add(word);
                    scores.add(score);
                } else if (j < ClassInfo.specifyingSize) {
                    specifiers.insertElementAt(word, j);
                    scores.insertElementAt(score, j);
                }
                if (scores.size() > ClassInfo.specifyingSize) {
                    specifiers.removeElementAt(ClassInfo.specifyingSize);
                    scores.removeElementAt(ClassInfo.specifyingSize);
                }
            }
            if(specifiers.size() == 0)
                System.out.println("ohhhhhh");
            classInfo.setSpecifiers(specifiers);
        }

        initTermPresenceVector();
    }

    private double calculateWordScoreForClass(String word, int classInfoIndex) {
        //f1 percent of word occurence in all classes
        double f1 = getWordOccurenceProbabilityInAllClasses(word);
        //f2 percent of word occurence in this class docs
        double f2 = getWordOccurenceProbabilityInClass(word, classInfoIndex);
        //f3 average word occurence in this class docs
        double f3 = classes.elementAt(classInfoIndex).getAverageWordOccurence(word);
        return Math.log(1 + 1.0 / f1) * Math.log(1 + f2) * Math.log(1 + f3);
    }

    public double getWordOccurenceProbabilityInAllClasses(String word)
    {
        return ((WordOccurenceInAllClasses)wordClassDictionary.get(word)).area.size()/(double)(classes.size());
    }

    public double getWordOccurenceProbabilityInClass(String word, int classInfoIndex) // P(wi, ck)
    {
        double probability = 0;
        ClassInfo classInfo = classes.elementAt(classInfoIndex);
        Vector<DocInfo> docVector = (Vector<DocInfo>) classInfo.getWordsDocDictionary().get(word);
        probability = (docVector == null) ? 0 :
                (docVector.size() / ((double) classInfo.getDocs().size()));
        return probability;
    }

    public double getWordOccurNOccurProbabilityInClass(TermPresence termPresence, int classInfoIndex) // P(wi, ck)
    {
        try {

        double probability = getWordOccurenceProbabilityInClass(termPresence.word, classInfoIndex);
        return (termPresence.isPresent ? probability : 1.0-probability);
        }catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
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
        fillTermPresenceVector(doc);
        double score = 1;
        for(int i = 0; i < termPresences.length; i++) {
            for (int j = 0; j < termPresences[i].length; j++) {
                score *= getWordOccurNOccurProbabilityInClass(termPresences[i][j], classInfoIndex);
            }
        }
        score *= getClassUseProbability(classInfoIndex);
        return score;
    }//didam

    public String getdocClassMatch(DocInfo doc) {
        double maxScore = -1;
        int index = -1;
        for(int i = 0; i < classes.size(); i++)
        {
            double currentScore = getDocClassMatchProbability(i, doc);
            if(currentScore > maxScore)
            {
                maxScore = currentScore;
                index = i;
            }
        }//DIDAM
        return classes.elementAt(index).getNewsGroup();
    }
    public int getdocClassMatchIndex(DocInfo doc) {
        double maxScore = -1;
        int index = -1;

        for(int i = 0; i < classes.size(); i++)
        {
            double currentScore = getDocClassMatchProbability(i, doc);
            if(currentScore > maxScore)
            {
                maxScore = currentScore;
                index = i;
            }
        }
        return index;
    }

    public void fillTermPresenceVector(DocInfo doc)
    {
        for (int i = 0; i < classes.size(); i++) {
            ClassInfo classInfo = classes.elementAt(i);
            for (int j = 0; j < classInfo.getSpecifiers().size(); j++) {
                if (doc.getWordsInTrie().get(classInfo.getSpecifiers().elementAt(j)) == null)
                    termPresences[i][j].isPresent = false;
                else
                    termPresences[i][j].isPresent = true;
            }
        }
    }

    private void initTermPresenceVector() {
        termPresences = new TermPresence[classes.size()][ClassInfo.specifyingSize];
        for (int i = 0; i < classes.size(); i++) {
            ClassInfo classInfo = classes.elementAt(i);
            for (int j = 0; j < classInfo.getSpecifiers().size(); j++) {
                termPresences[i][j] = new TermPresence(classInfo.getSpecifiers().elementAt(j), false);
            }
        }
    }

    public Vector<ClassInfo> getClasses() {
        return classes;
    }

    public void setClasses(Vector<ClassInfo> classes) {
        this.classes = classes;
    }

    public Trie getWordClassDictionary() {
        return wordClassDictionary;
    }

    public void setWordClassDictionary(Trie wordClassDictionary) {
        this.wordClassDictionary = wordClassDictionary;
    }

    public Vector<String> getWords() {
        return words;
    }

    public void setWords(Vector<String> words) {
        this.words = words;
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

    public class TermPresence {
        public String word;
        public boolean isPresent;

        public TermPresence(String word, boolean isPresent) {
            this.word = word;
            this.isPresent = isPresent;
        }
    }
}

