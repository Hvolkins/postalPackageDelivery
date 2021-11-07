package cz.hvolkins.helper;

import cz.hvolkins.model.Fee;
import cz.hvolkins.model.PostalPackage;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class used for add/read information about package into memory.
 *
 * @author Elena Hvolkova
 */
public class PackageMemory {

    private Map<Long, PostalPackage> packagesInMemory = new HashMap<>();
    private AtomicLong pcgKeySequence = new AtomicLong(1L);
    private List<Fee> feesInMemory  = new ArrayList<>();

    private static PackageMemory instance;

    private PackageMemory() {}

    // Initialization of the singleton instance
    public static PackageMemory getInstance() {
        if (instance == null)
            instance = new PackageMemory();
        return instance;
    }

     /**
     * Return all packages from memory
     * @return packagesInMemory
     */
    public List<PostalPackage> getAllPackages() {
        return new ArrayList<>(packagesInMemory.values());
    }

    /**
     * Saving the package
     * @param newPackage new postal package
     */
    public PostalPackage save(final PostalPackage newPackage) {
        if (null != newPackage) {
            if (isExistPostalCode(newPackage.getPostalCode()))
                updateWeight(newPackage);
            else {
                newPackage.setId(pcgKeySequence.getAndIncrement());
                // Control if fees are imported and recount fees od packages, saved in HashMap
                if (!feesInMemory.isEmpty())
                    newPackage.setFee(count(newPackage.getWeight()));

                packagesInMemory.put(newPackage.getId(), newPackage);
            }
        }
        return newPackage;
    }

    /**
     * Control if in HashMap exists current package
     * @param postalCode postal code
     * @return true if postal code exists in memory
     */
    private boolean isExistPostalCode(Integer postalCode) {
        long pcgCount = getAllPackages().stream().filter(v -> postalCode.equals(v.getPostalCode())).count();
        return pcgCount > 0;
    }

    /**
     * Weight updating for same postal code
     * @param postalPackage postal package
     */
    private void updateWeight(PostalPackage postalPackage) {
        List<PostalPackage> all = getAllPackages();
        if (null != postalPackage && !all.isEmpty()) {
            for (PostalPackage p: all) {
                if (p.getPostalCode().equals(postalPackage.getPostalCode())) {
                    Double weight = p.getWeight() + postalPackage.getWeight();
                    p.setWeight(weight);
                }
            }
        }
    }

    /**
     * Saving list of packages imported from file into HashMap
     * @param packages list of imported packages from file
     */
    public void saveFromFile(List<PostalPackage> packages) {
        if (null != packages && !packages.isEmpty()) {
            for (PostalPackage pcg: packages)
                save(pcg);
        }
    }

    /**
     * Recounting fees in packages after import fees from file.
     * @param fees list of fees
     */
    public void recountFeeForPackages(List<Fee> fees) {
        List<PostalPackage> list = getAllPackages();
        if (null != fees) {
            feesInMemory = fees;
            if (!list.isEmpty()) {
                for (PostalPackage pcg: list) {
                    Double fee = count(pcg.getWeight());
                    pcg.setFee(fee);
                }
            }
        }
    }

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
        if (!feesInMemory.isEmpty()) {
            String fee = String.format(Locale.US, "%.2f", postPackage.getFee());
            // uses full message (with feels) for printing all data, for one package uses message without fee
            message = allPackages ? message + " "+fee : message;
        }
        System.out.println(message);
    }

    /**
     * Print all data from memory
     */
    public void printAllData() {
        if (!packagesInMemory.isEmpty()) {
            packagesInMemory.forEach((k, v) -> printPackageInfo(v, true));
        }
    }

    /**
     * Count fee over weight of package
     * @param weight weight of package
     * @return counted fee
     */
    private Double count(Double weight) {
        if (null != weight && !feesInMemory.isEmpty()) {
            for (int i=0; i<feesInMemory.size(); i++) {
                if (weight >= feesInMemory.get(i).getWeight())
                    return feesInMemory.get(i).getFee();
            }
        }
        return null;
    }
}
