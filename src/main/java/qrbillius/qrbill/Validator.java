package qrbillius.qrbill;

import net.codecrete.qrbill.generator.Language;

public class Validator {

    private Validator() {}



    public static boolean isValidLanguage(String language) {
        try {
            Language.valueOf(language);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }

    }

}
