package org.jabref.logic.identifier;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ISBN {

    private static final Pattern ISBN_PATTERN = Pattern.compile("^(\\d{9}[\\dxX]|\\d{13})$");

    private final String isbnString;


    public ISBN(String isbnString) {
        this.isbnString = Objects.requireNonNull(isbnString).trim().replace("-", "");
    }

    public boolean isValidFormat() {
        Matcher isbnMatcher = ISBN_PATTERN.matcher(isbnString);
        if (isbnMatcher.matches()) {
            return true;
        }
        return false;
    }

    public boolean isValidChecksum() {
        boolean valid;
        if (isbnString.length() == 10) {
            valid = isbn10check();
        } else {
            // length is either 10 or 13 based on regexp so will be 13 here
            valid = isbn13check();
        }
        return valid;
    }

    public boolean isIsbn10() {
        return isbn10check();
    }

    public boolean isIsbn13() {
        return isbn13check();
    }

    // Check that the control digit is correct, see e.g. https://en.wikipedia.org/wiki/International_Standard_Book_Number#Check_digits
    private boolean isbn10check() {
        if (isbnString.length() != 10) {
            return false;
        }

        int sum = 0;
        for (int pos = 0; pos <= 8; pos++) {
            sum += (isbnString.charAt(pos) - '0') * ((10 - pos));
        }
        char control = isbnString.charAt(9);
        if ((control == 'x') || (control == 'X')) {
            control = '9' + 1;
        }
        sum += (control - '0');
        return (sum % 11) == 0;
    }

    // Check that the control digit is correct, see e.g. https://en.wikipedia.org/wiki/International_Standard_Book_Number#Check_digits
    private boolean isbn13check() {
        if (isbnString.length() != 13) {
            return false;
        }

        int sum = 0;
        for (int pos = 0; pos <= 12; pos++) {
            sum += (isbnString.charAt(pos) - '0') * ((pos % 2) == 0 ? 1 : 3);
        }
        return (sum % 10) == 0;
    }

    public boolean isValid() {
        return isValidFormat() && isValidChecksum();
    }
}
