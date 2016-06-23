package Primitives;

import com.sun.org.apache.xml.internal.utils.Trie;

import java.util.Vector;

public class ClassInfo {
    private String newsGroup;
    private Trie wordsDocDictionary; // it has words and their termDoc vector
    private Vector<DocInfo> docs;

    public ClassInfo(String newsGroup)
    {
        this.newsGroup = newsGroup;
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
            }
            if(!docVector.contains(newDoc))
            {
                docVector.add(newDoc);
            }
        }
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
}
