package Stemmer;
/**
 * Created by 9231006 on 4/24/2016.
 */
public class VerbStemmer {

    public VerbStemmer(){

    }

    public enum State{

        PASTCONTINUOUS ,
        PRESENTPERFECTTENSE ,
        PASTPERFECT ,
        PASTSUBjJUNCTIVE ,
        NEWSPVR ,
        SIMPLEPVR ,
        IMPRETIVE ,
        NEGATIVE ,

    };

    protected String execute(String word , boolean SpecialState){
        if (SpecialState){
            if (word.endsWith("ه")) {
                return word.substring(0, word.length() - 1);
            }
            if(word.startsWith("می")){
                if(word.endsWith("یم") || word.endsWith("ید") || word.endsWith("ند")){
                    if(word.length() >= 6){
                        return  word.substring(2,word.length()-2);
                    }
                return "";
                }
                if(word.endsWith("م") || word.endsWith("ی")){
                    if(word.length() >=5){
                        return word.substring(2,word.length()-1);
                        }
                    return "";
                }
                return word.substring(2,word.length()-1);
            }
            return word;
        }

        if(word.startsWith("می")){
            if(word.endsWith("یم") || word.endsWith("ید") || word.endsWith("ند")){
                return word.substring(2,word.length()-2);
            }
            if(word.endsWith("م") || word.endsWith("ی") || word.endsWith("د") ){
                return  word.substring(2,word.length()-1);
            }
            if(word.length() >=5){
                return word.substring(2,word.length());
            }
        return word;
        }
        if(word.startsWith("نمی")){
            if(word.endsWith("یم") || word.endsWith("ید") || word.endsWith("ند")){
                return word.substring(2,word.length()-2);
            }
            if(word.endsWith("م") || word.endsWith("ی") || word.endsWith("د") ){
                return  word.substring(2,word.length()-1);
            }
            if(word.length() >=5){
                return word.substring(3,word.length());
            }
            return word;
        }

        if(word.startsWith("ب")){
            if(word.endsWith("یم") || word.endsWith("ید") || word.endsWith("ند")){
                return word.substring(1,word.length()-2);
            }
            if(word.endsWith("م") || word.endsWith("ی") || word.endsWith("د")){
                return word.substring(1,word.length()-1);
            }
        return word;
        }

        if(word.endsWith("یم") || word.endsWith("ید") || word.endsWith("ند")){
            if(word.length() >=5){
                return word.substring(0,word.length()-2);
            }
            return word;
        }
        if(word.endsWith("م") || word.endsWith("ی")){
            if(word.length() >=4){
                return word.substring(0,word.length()-1);
            }
            return word;
        }
        if(word.startsWith("خواه")){
            return"خواه" ;
        }
    return word;
    }
}