package Primitives;

import FileManagement.RandomAccessFileManager;
import Primitives.Tree.RamFileBtree;
import Primitives.Vector.FileVector;
import javafx.util.Pair;

import java.util.Vector;

public class Dictionary
{
    private static Dictionary instance = null;
    private final int indexFileID, vectorFileID;
    public RamFileBtree<TermAbstractDetail> tree;
    private FileVector<TermDocDetail> fileVector;
    private int lastOffsetRead;
    private Vector<Pair<String, TermAbstractDetail>> lastNodeRead;

    private Dictionary(int id)
    {
        indexFileID = RandomAccessFileManager.createNewInstance("index"+id);
        vectorFileID = RandomAccessFileManager.createNewInstance("postingVector"+id);
        String temp = "ممممممممممممممممممممممممممممممممممممممممممممممممممممممممممممممممممممممم";
        TermAbstractDetail termAbstractDetail = new TermAbstractDetail(null, null);
        tree = new RamFileBtree<>(temp.length(), termAbstractDetail.sizeof(), 17, TermAbstractDetail.class, indexFileID);
        fileVector = new FileVector<>(TermDocDetail.class, vectorFileID);
        lastOffsetRead = Integer.MAX_VALUE;
        lastNodeRead = null;
    }

    public static Dictionary getIntance(int id)
    {
        if (instance == null)
            instance = new Dictionary(id);
        return instance;
    }

    public TermAbstractDetail search(String key){
        return tree.search(key);
    }


    public void insert(String term, int docID)
    {
        TermAbstractDetail searchRes = tree.search(term);
        if (searchRes == null)
        {
            Long indexPtr = fileVector.writeElementAt(null, docID, new TermDocDetail(1));
            try
            {
                tree.insert(term, new TermAbstractDetail(1, indexPtr));
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        } else
        {
            TermDocDetail termDocDetail = fileVector.elementAt(searchRes.getFilePtr(), docID);
            if (termDocDetail == null)
                termDocDetail = new TermDocDetail(0);
            searchRes.incrementOccurences();
            termDocDetail.incrementOccurences();
            fileVector.writeElementAt(searchRes.getFilePtr(), docID, termDocDetail);
            tree.update(term, searchRes);
        }
    }

    public void initializeForSequentialRead()
    {
        tree.initializeForSequentialRead();
        lastNodeRead = tree.getNextNode();
        lastOffsetRead = 0;
    }

//    public TermPosting getNextTermPosting()
//    {
//        if (lastOffsetRead >= lastNodeRead.size())
//        {
//            lastNodeRead = tree.getNextNode();
//            if (lastNodeRead == null)
//                return null;
//            lastOffsetRead = 0;
//        }
//
//        Pair<String, TermAbstractDetail> currentPair = lastNodeRead.elementAt(lastOffsetRead);
//        Vector<TermDocDetail> allElements = fileVector.getAllElements(currentPair.getValue().getFilePtr());
//        TermPosting termPosting = new TermPosting(currentPair.getKey(), allElements,
//                Math.log(tree.getNumberOfTermsAdded() / allElements.size()));
//
//        lastOffsetRead++;
//        return termPosting;
//    }
}
