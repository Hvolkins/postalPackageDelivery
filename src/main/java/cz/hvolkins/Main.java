package cz.hvolkins;

import cz.hvolkins.helper.*;
import cz.hvolkins.model.Fee;
import cz.hvolkins.model.PostalPackage;

import java.util.*;

/**
 * The PackageDelivery program implements work with package
 * @see PostalPackage from console (input/output data).
 *
 * Is possible:
 * 1) added data manualy,
 * 	  format (<weight: positive number, >0, maximal 3 decimal places, . (dot) as decimal separator><space><postal code: fixed 5 digits> )
 * 2) import packages from text file
 * 3) import fees from text file, recount output
 * 4) quit from program
 * Use menu after PackageDelivery tool starting for help information.
 *
 * @author Elena Hvolkova
 * @version 1.0
 * @since 2021-11-08
 */
public class Main {

    private static PackageMemory memory = PackageMemory.getInstance();

    public static void main(String[] args) {
        setHelpMenu();

        FileUtil fileUtil = new FileUtil();
        Scanner scanner = new Scanner(System.in).useLocale(Locale.ENGLISH);

        printDataEveryMinute();

        while (true) {
            String inputArgument = scanner.nextLine().trim();

            // Control if entered parameter in console is empty or blank
            if (inputArgument.isEmpty() || inputArgument.isBlank())
                System.out.println(MessageConstants.ENTER_MENU_PARAMETER);
            else {
                // Find parameter in enumeration. If exists, use for working with console menu
                ConsoleParameterEnum argument = findArgumentByValue(inputArgument);
                if (null != argument) {
                    switch (argument) {
                        case NEW_PCG, NEW_PCG_SHORT: {
                            // Saving manually added package
                            addNewPackage(scanner);
                            break;
                        }
                        case FILE_INITIAL, FILE_INITIAL_SHORT: {
                            // Read information about packages
                            List<PostalPackage> data = fileUtil.readInitialFile(scanner);
                            // Saving data in memory
                            memory.saveFromFile(data);
                            break;
                        }
                        case FILE_FEES, FILE_FEES_SHORT: {
                            // Read information about fees
                            List<Fee> data = fileUtil.readFeesFromFile(scanner);
                            // Added fee to existed in memory packages
                            memory.recountFeeForPackages(data);
                            break;
                        }
                        case QUIT, QUIT_SHORT: {
                            System.out.println("Exiting from Package delivery tool..");
                            System.exit(0);
                            break;
                        }
                    }
                } else
                    System.out.println("Entered parameter is bad, see Package delivery tool!");
            }
        }
    }

    /**
     * Finds enumeration
     * @see ConsoleParameterEnum by entered attribute value
     *
     * @param argument input in console
     * @return enumeration
     */
    private static ConsoleParameterEnum findArgumentByValue(String argument) {
        List<ConsoleParameterEnum> enums = Arrays.asList(ConsoleParameterEnum.values());
        if (!enums.isEmpty()) {
            for (ConsoleParameterEnum param: enums) {
                if (param.getArgument().equals(argument))
                    return param;
            }
        }
        return null;
    }

    /**
     * Writes output to console one per minute
     */
    private static void printDataEveryMinute() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Printing data from memory..");
                memory.printAllData();
                System.out.println(MessageConstants.ENTER_MENU_PARAMETER);
            }
        }, 0, 60*1000);//wait 0 ms before doing the action and do it every 60 000ms (1 minute)

        //timer.cancel();//Not necessary because calls System.exit
    }

    /**
     * Saves manually added package
     * @param scanner scanner
     */
    private static void addNewPackage(Scanner scanner) {
        System.out.println("Enter package (<weight> <postal code>, for example 56.123 41501): ");
        // Package validation
        PostalPackage pcg = new DataValidator().valid(scanner);
        if (null != pcg) {
            // Package saving in memory
            PostalPackage savedPcg = memory.save(pcg);
            // Print to console info about added package
            memory.printPackageInfo(savedPcg, false);
        }
    }

    /**
     * Prints help menu in console
     */
    private static void setHelpMenu() {
        System.out.println("Package delivery tool:");
        System.out.println("-n, -new	Enter the new package ");
        System.out.println("-i, -init	Read packages from file");
        System.out.println("-f, -file	Read fees from file");
        System.out.println("-q, -quit	Exit from tool");
    }
}