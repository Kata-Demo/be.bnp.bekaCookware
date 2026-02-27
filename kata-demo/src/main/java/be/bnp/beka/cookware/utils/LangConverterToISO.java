package utils;
import utils.typedEnum.Lang;

public class LangConverterToISO {

    public static String convertToIso639(Lang lang){
        switch (lang) {
            case Lang.FR:                
                return "fr-FR";
            case Lang.NL: 
                return "nl-NL";
            case Lang.DE:
                return "de-DE";
            case Lang.EN:
                return "en-US";
            default:
                return "en-US";
        }
        
    }
}
