package Primitives;

import com.sun.org.apache.xml.internal.utils.Trie;

import java.util.Vector;

public class DocInfo {
    private String name;
    private String newsGroup;
    private Trie wordsInTrie; // it has words and their location in vector
    private Vector<Term> words;

    public DocInfo(String docName, String newsGroup, String[] tokens)
    {
        this.name = docName;
        this.newsGroup = newsGroup;
        this.wordsInTrie = new Trie();
        this.words = new Vector<Term>();

        for(String token: tokens)
            if(!token.trim().equalsIgnoreCase(""))
            {
                Integer wordIndexInVector;
                if((wordIndexInVector = (Integer) wordsInTrie.get(token)) == null)
                {
                    try {
                        words.add(new Term(token, 1));
                        wordIndexInVector = words.size() - 1;
                        wordsInTrie.put(token, wordIndexInVector);
                    } catch (Exception e) {
                        words.removeElementAt(words.size() - 1);
                        continue;
                    }
                }
                else
                    words.elementAt(wordIndexInVector).occurences++;
            }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNewsGroup()
    {
        return newsGroup;
    }

    public void setNewsGroup(String newsGroup)
    {
        this.newsGroup = newsGroup;
    }

    public Trie getWordsInTrie()
    {
        return wordsInTrie;
    }

    public void setWordsInTrie(Trie wordsInTrie)
    {
        this.wordsInTrie = wordsInTrie;
    }

    public Vector<Term> getWords()
    {
        return words;
    }

    public void setWords(Vector<Term> words)
    {
        this.words = words;
    }

    public long getWordOccurence(String word)
    {
        Integer wordIndexInVector;
        wordIndexInVector = (Integer) wordsInTrie.get(word);
        return words.elementAt(wordIndexInVector).occurences;
    }
}
