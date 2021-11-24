package cz.hvolkins.helper;

/**
 * Validation of input data for package (weight and postal code)
 *
 * @author Elena Hvolkova
 */
public class DataValidator {
    /**
     * Returns absolute value (create positive number from negative)
     * @param weight of postal package
     * @return absolute value of package weight
     */
    public Double validWeight(Double weight) {
        return Math.abs(weight);
    }

    /**
     * Returns absolute value with control value on length (must be 5 digits)
     * @param postalCode value from input or file
     * @return valid postal code
     */
    public String validPostalCode(String postalCode) {
        postalCode = postalCode.replace("-", "");
        int iLength = postalCode.length();

        if (iLength < 5) {
            new DataPrinter().printValidError();
        } else if (iLength > 5) {
            postalCode = postalCode.substring(0,5);
        }
        return postalCode;
    }
}
