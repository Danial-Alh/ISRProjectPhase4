package Stemmer;

public class StateHandler {
    VerbStemmer verbHandler;
    NouneStemmer nouneHandler;

    public StateHandler(){
        verbHandler = new VerbStemmer();
        nouneHandler = new NouneStemmer();
    }

    public String detectType(String last_word, String word, String next_word, boolean hasChange){

        String result  = "";

        // pre process for special words
        if(word.length() <= 3 && !(word.startsWith("ب") || word.startsWith("ن"))){
            return word;
        }
        if(isArabic(word)){
            return word;
        }
        if(!next_word.equals("") && next_word.startsWith("بود") || next_word.startsWith("باش")){
            if (next_word.length() == 4 && (next_word.endsWith("م") || next_word.endsWith("ی") || next_word.endsWith("د"))){
                result  = verbHandler.execute(word,true);
            }
            if(next_word.length() == 5 && (next_word.endsWith("یم") || next_word.endsWith("ید") || next_word.endsWith("ند"))) {
                result  = verbHandler.execute(word,true);
            }
        }
        else
        if(hasChange && !last_word.equals("") && last_word.startsWith("خواه")){
//            if (last_word.length() == 5 && (last_word.endsWith("م") || last_word.endsWith("ی") || last_word.endsWith("د"))){
                result = verbHandler.execute(word,true);
//            }
//            if(last_word.length() == 6 && (last_word.endsWith("یم") || last_word.endsWith("ید") || last_word.endsWith("ند"))){
//                result = verbHandler.execute(word,true);
//            }
        }
        else
        if(!last_word.equals("") && last_word.startsWith("داشت")){
            result = verbHandler.execute(word,true);
        }
        else
        if(!next_word.equals("") && next_word.endsWith("ام") || next_word.endsWith("ای") || next_word.endsWith("است") || next_word.endsWith("ایم") || next_word.endsWith("اید") || next_word.endsWith("اند")){
            result  = verbHandler.execute(word,true);
        }
        else
            result = nouneHandler.execute(word,false);
            if(result.equals("")){
               result = verbHandler.execute(word,false);
            }
        return result;
    }

    protected boolean isArabic(String word){
        if(word.length() == 4 ){
            if(word.startsWith("ا")){
                return true;
            }
            if(word.startsWith("ت")){
                return true;
            }
        return false;
        }
        if(word.length() == 5){
            if(word.charAt(0) == 'ا' && word.charAt(3) == 'ا'){
                return true;
            }
            if(word.charAt(0)=='ت' && word.charAt(2) =='ا' ){
                return true;
            }
            if(word.charAt(0)=='ت' && word.charAt(3)=='ي'){
                return true;
            }
            if(word.charAt(0)=='م' && word.charAt(3)=='و'){
                return true;
            }
            if(word.charAt(0)=='م' && word.charAt(2)=='ت'){
                return true;
            }
        return false;
        }
        if(word.length() == 6){
            if(word.charAt(0) =='م'  && word.charAt(2) == 'ا'  && word.charAt(5) =='ه'){
                return true;
            }
            if(word.charAt(0) == 'ا'&&  word.charAt(2) == 'ت' && word.charAt(5) =='ا'){
                return true;
            }
            if(word.charAt(0) == 'ا'&&  word.charAt(1) == 'ن' && word.charAt(5) =='ا'){
                return true;
            }
            return false;
        }
        if(word.length() == 7){
            if(word.charAt(0) == 'ا'&&  word.charAt(1) == 'س' && word.charAt(2) =='ت' && word.charAt(5) =='ا'){
                return true;
            }
            return false;
        }
    return false;
    }
}