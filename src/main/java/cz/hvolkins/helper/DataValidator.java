package cz.hvolkins.helper;

import cz.hvolkins.model.PostalPackage;

import java.util.Scanner;

/**
 * Validation of input data for package (weight and postal code)
 *
 * @author Elena Hvolkova
 */
public class DataValidator {

    public PostalPackage valid(Scanner scanner) {
        boolean isValid = true;
        // Returns absolute value (create positive number from negative)
        double weight = Math.abs(scanner.nextDouble());

        int postalCode = Math.abs(scanner.nextInt());
        int length = String.valueOf(postalCode).length();

        // Controls if postal code has 5 digits
        if (length != 5) {
            System.out.println("Postal code must has 5 digits! For example 41501");
            isValid = false;
        }
        return isValid ? new PostalPackage(weight, postalCode) : null;
    }
}
