package Stemmer;
/**
 * Created by 9231006 on 4/24/2016.
 */
public class NouneStemmer {
    public NouneStemmer(){

    }
    protected  String execute(String word, boolean specialState){
        if(word.endsWith("ات")){
            return word.substring(0,word.length()-2)+"ه";
        }
        if(word.endsWith("ها") /*|| word.endsWith("ان") || word.endsWith("ون") || word.endsWith("ین")*/){
        return word.substring(0,word.length()-2);
        }
        else
        if(word.endsWith("کده") || word.endsWith("سرا") || word.endsWith("بار") || word.endsWith("بان") || word.endsWith("سان")||
                word.endsWith("دیس") || word.endsWith("زار") || word.endsWith("گار") || word.endsWith("مان") ||
                word.endsWith("نده") || word.endsWith("گون") || word.endsWith("اسا") || word.endsWith("آسا") || word.endsWith("انه")
                ){
            if(word.length() >=5){
                 return word.substring(0,word.length()-3);
            }
            return "";
        }
        else
        if(word.endsWith("گاه")){
            if(word.endsWith("یشگاه")){
                return word.substring(0,word.length()-5);
            }
            else{
                return word.substring(0,word.length()-3);
            }
        }
        else
        if(word.endsWith("ترین") || word.endsWith("ستان")){
            return word.substring(0,word.length()-4);
        }
        else
        if(word.endsWith("ترینه")){
            return word.substring(0,word.length()-5);
        }
    return "";
    }
}