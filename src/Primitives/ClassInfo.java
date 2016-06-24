package Primitives;

import com.sun.org.apache.xml.internal.utils.Trie;

import java.util.Vector;

public class ClassInfo {
    public static final int specifyingSize = 15;
    private String newsGroup;
    private Trie wordsDocDictionary; // it has words and their termDoc vector
    private Vector<DocInfo> docs;
    private Vector<String> words;
    private Vector<String> specifiers;

    public ClassInfo(String newsGroup)
    {
        this.newsGroup = newsGroup;
        this.wordsDocDictionary = new Trie();
        this.docs = new Vector<DocInfo>();
    }

    public static int getSpecifyingSize() {
        return specifyingSize;
    }

    public void addNewDoc(DocInfo newDoc)
    {
        docs.add(newDoc);
        addDocWordsToDictionary(newDoc);
    }

    private void addDocWordsToDictionary(DocInfo newDoc)
    {
        for(Term term : newDoc.getWords())
        {
            Vector<DocInfo> docVector = (Vector<DocInfo>) wordsDocDictionary.get(term.content);
            if(docVector == null)
            {
                docVector = new Vector<DocInfo>();
                wordsDocDictionary.put(term.content, docVector);
                words.add(term.content);
            }
            if(!docVector.contains(newDoc))
            {
                docVector.add(newDoc);
            }
        }
    }

    public double getAverageWordOccurence(String word)
    {
        Vector<DocInfo> docVector = (Vector<DocInfo>) wordsDocDictionary.get(word);
        long totalOccurence = 0;
        for (DocInfo doc : docVector)
            totalOccurence += doc.getWordOccurence(word);
        return totalOccurence / ((double) docVector.size());
    }

    public String getNewsGroup()
    {
        return newsGroup;
    }

    public void setNewsGroup(String newsGroup)
    {
        this.newsGroup = newsGroup;
    }

    public Trie getWordsDocDictionary()
    {
        return wordsDocDictionary;
    }

    public void setWordsDocDictionary(Trie wordsDocDictionary)
    {
        this.wordsDocDictionary = wordsDocDictionary;
    }

    public Vector<DocInfo> getDocs()
    {
        return docs;
    }

    public void setDocs(Vector<DocInfo> docs)
    {
        this.docs = docs;
    }

    public Vector<String> getSpecifiers() {
        return specifiers;
    }

    public void setSpecifiers(Vector<String> specifiers) {
        this.specifiers = specifiers;
    }

    public Vector<String> getWords() {
        return words;
    }

    public void setWords(Vector<String> words) {
        this.words = words;
    }
}
