package GoogleTranslate;


import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static ConfigReader.ConfigReader.getKeyFromConfig;

public class GoogleTranslator {

    private static String API_KEY = getKeyFromConfig("GOOGLE_API_KEY");
    private static final Translate translate = TranslateOptions.newBuilder()
            .setApiKey(API_KEY)
            .build()
            .getService();

//    public static String translatedText() {
//
//    }

//    public static void main(String[] args) {
//        List<String> titleInEnglish = new ArrayList<String>();
//        titleInEnglish.add("I Love English");
//        titleInEnglish.add("English is a good language");
//
//
//        String[] words = titleInEnglish.toLowerCase().replaceAll("[^a-z ]", "").split("\\s+");
//        for(String word : words){
//            countMap.put(word, countMap.getOrDefault(word,0)+1);
//        }
//    }

    public static String translateText(String text){
        Translation translation = translate.translate(text, Translate.TranslateOption.sourceLanguage("es"),
                Translate.TranslateOption.targetLanguage("en"));
        return translation.getTranslatedText();
    }



}
