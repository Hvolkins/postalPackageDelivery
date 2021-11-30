package cz.hvolkins.helper;

import cz.hvolkins.model.PostalPackage;

import java.util.Locale;

/**
 * This class used for printing info into console
 *
 * @author Elena Hvolkova
 */
public class DataPrinter {

    private static PackageMemory memory = PackageMemory.getInstance();

    /**
     * Print information about packages in console
     * @param postPackage postal package
     * @param allPackages true if need to print all data from memory
     */
    public void printPackageInfo(PostalPackage postPackage, boolean allPackages) {
        String info = allPackages ? "" : "Added package: ";

        String postalCode = String.format(Locale.US, "%s", postPackage.getPostalCode());
        String weight = String.format(Locale.US, "%.3f", postPackage.getWeight());

        // this message uses for printing information about added manually package
        String message = info + postalCode +" "+weight;

        // if was added information about fees into memory
        if (!memory.getFeesInMemory().isEmpty()) {
            String fee = String.format(Locale.US, "%.2f", postPackage.getFee());
            // uses full message (with feels) for printing all data, for one package uses message without fee
            message = allPackages ? message + " "+fee : message;
        }
        print(message);
    }

    /**
     * Print all data from memory
     */
    public void printAllData() {
        print("Printing data from memory..");
        if (!memory.getPackagesInMemory().isEmpty()) {
            memory.getPackagesInMemory().forEach((k, v) -> printPackageInfo(v, true));
        }
        print(MessageConstants.ENTER_MENU_PARAMETER);
    }

    /**
     * Print validation message for postal code
     */
    public void printValidError() {
        print("Postal code must has 5 digits! For example 41501");
    }

    /**
     * Prints help menu in console
     */
    public void printHelpMenu() {
        print("Package delivery tool:");
        print("-n, -new	Enter the new package ");
        print("-i, -init	Read packages from file");
        print("-f, -file	Read fees from file");
        print("-q, -quit	Exit from tool");
    }

    public void print(String message) {
        System.out.println(message);
    }
}
