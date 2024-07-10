package bg.softuni.damapp.util;


public enum Constants {
    ;
    //Validation messages:
    public static final String NOT_EMPTY = "Това поле е задължително.";
    public static final String INVALID_EMAIL = "Въведи валиден имейл адрес (напр. name@domain.com).";
    public static final String PASSWORD_MISMATCH = "Паролите не съвпадат.";
    public static final String PASSWORD_STRENGTH = "Паролата трябва да съдържа главна и малка буква от латинската азбука, цифра и специален символ(@$!%*#?&).";
    public static final String PASSWORD_SIZE = "Паролата трябва да е минимум 8 символа.";

    // Regex patterns
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
}

