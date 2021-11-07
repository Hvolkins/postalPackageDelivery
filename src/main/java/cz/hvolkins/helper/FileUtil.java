package cz.hvolkins.helper;

import cz.hvolkins.model.Fee;
import cz.hvolkins.model.PostalPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Util for importing data from text file over console
 *
 * @author Elena Hvolková
 */
public class FileUtil {
    /**
     * Import initial data about packages.
     *
     * @param scanner scanner
     * @return list of
     * @see PostalPackage
     */
    public List<PostalPackage> readInitialFile(Scanner scanner) {
        System.out.println(MessageConstants.ENTER_FILE_NAME);
        String name = scanner.nextLine();

        File file = new File(name);
        System.out.println("File exist - " + file.exists());

        List<PostalPackage> list = new ArrayList<>();

        // check that the file exists
        if (file.exists()) {
            // Create a Scanner from the file.
            Scanner inFile;
            try {
                inFile = new Scanner(file);
                while (inFile.hasNext()) {
                    String line = inFile.nextLine();   // read the next line
                    // Writing elements from line into list
                    List<String> fields = Stream.of(line.split(" ", -1)).collect(Collectors.toList());
                    if (!fields.isEmpty()) {
                        // Creating new package with data read from file
                        PostalPackage pcg = new PostalPackage(Double.valueOf(fields.get(0).trim()), Integer.valueOf(fields.get(1).trim()));
                        list.add(pcg);
                    }
                }
                System.out.println(MessageConstants.DATA_FROM_FILE);
                System.out.println(MessageConstants.ENTER_MENU_PARAMETER);
                // Close the Scanner object attached to the file
                inFile.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * Import data about fees.
     *
     * @param scanner scanner
     * @return list of
     * @see Fee
     */
    public List<Fee> readFeesFromFile(Scanner scanner) {
        System.out.println(MessageConstants.ENTER_FILE_NAME);
        String name = scanner.nextLine();

        File file = new File(name);
        System.out.println("File exist - " + file.exists());

        List<Fee> list = new ArrayList<>();

        if (file.exists()) {
            Scanner inFile;
            try {
                inFile = new Scanner(file);
                while (inFile.hasNext()) {
                    String line = inFile.nextLine();   // read the next line
                    List<String> fields = Stream.of(line.split(" ", -1)).collect(Collectors.toList());
                    if (!fields.isEmpty()) {
                        Fee fee = new Fee(Double.valueOf(fields.get(0).trim()), Double.valueOf(fields.get(1).trim()));
                        list.add(fee);
                    }
                }
                System.out.println(MessageConstants.DATA_FROM_FILE);
                System.out.println(MessageConstants.ENTER_MENU_PARAMETER);
                inFile.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

}
