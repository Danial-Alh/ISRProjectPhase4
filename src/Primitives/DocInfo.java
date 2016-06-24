package Primitives;

import FileManagement.FileReader;
import com.sun.org.apache.xml.internal.utils.Trie;

import java.util.HashMap;
import java.util.Vector;

public class DocInfo {
    private String name;
    private String newsGroup;
    private Trie wordsInTrie; // it has words and their location in vector
    private Vector<Term> words;

    public static HashMap<String,Character> hashmap;
    public static FileReader stopWordsReader;
    public static Stemmer stemmer;

    public DocInfo(String docName, String newsGroup, String[] tokens)
    {
        this.name = docName;
        this.newsGroup = newsGroup;
        this.wordsInTrie = new Trie();
        this.words = new Vector<Term>();

//        Vector<String> strings = new Vector<String>();
//        for(int i = 0 ; i < tokens.length ; i++){
//            strings.add(tokens[i]);
//        }

//        createStopWordsSet();
//        delete_stop_words(strings);
//        strings = completeStem(strings);

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


    public static Vector<String> completeStem(Vector<String> tokens1){
        Stemmer pa = new Stemmer();
        Vector<String> arrstr = new Vector<String>();
        for (String i : tokens1){
            String s1 = pa.step1(i);
            String s2 = pa.step2(s1);
            String s3= pa.step3(s2);
            String s4= pa.step4(s3);
            String s5= pa.step5(s4);
            arrstr.add(s5);
        }
        return arrstr;
    }

    public static void delete_stop_words(Vector<String> words){
        for(int i  = words.size()-1 ; i >=0 ; i--){
            String str = words.elementAt(i);
            if(str.endsWith("،")){
//                System.out.println(words.elementAt(i));
                str = str.substring(0,str.length()-1);
//                System.out.println(str);
            }
            if(hashmap.containsKey(str) == true){
//                System.out.println("##  "  + str);
                words.remove(i);
            }
        }
    }

    public static void createStopWordsSet(){
        stopWordsReader = new FileReader("Stopwords_en.txt");
        hashmap = new HashMap<String, Character>();
        Vector<String> stopWords = new Vector<String>();
        while ((stopWords =stopWordsReader.readWithBufferSize(1000))!= null){
            for(String s : stopWords){
                hashmap.put(s,'@');
            }
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
