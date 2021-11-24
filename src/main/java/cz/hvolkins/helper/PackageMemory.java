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
            Optional<PostalPackage> optional = getAllPackages()
                    .stream().filter(v -> newPackage.getPostalCode().equals(v.getPostalCode())).findFirst();

            if (optional.isEmpty()) { // add new postal code
                newPackage.setId(pcgKeySequence.getAndIncrement());
                // Control if fees are imported and recount fees od packages, saved in HashMap
                if (!feesInMemory.isEmpty())
                    newPackage.setFee(count(newPackage.getWeight()));

                packagesInMemory.put(newPackage.getId(), newPackage);
            } else { // add data for exists postal code
                PostalPackage pp = optional.get();
                Double weight = pp.getWeight() + pp.getWeight();
                pp.setWeight(weight);
            }
        }
        return newPackage;
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

    public Map<Long, PostalPackage> getPackagesInMemory() {
        return packagesInMemory;
    }

    public List<Fee> getFeesInMemory() {
        return feesInMemory;
    }
}
