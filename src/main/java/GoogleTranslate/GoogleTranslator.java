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



    public static String translateText(String text){
        Translation translation = translate.translate(text, Translate.TranslateOption.sourceLanguage("es"),
                Translate.TranslateOption.targetLanguage("en"));
        return translation.getTranslatedText();
    }



}
