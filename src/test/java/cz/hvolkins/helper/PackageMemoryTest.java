package cz.hvolkins.helper;

import cz.hvolkins.model.Fee;
import cz.hvolkins.model.PostalPackage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class demonstrates how to test some methods from PackageMemory class
 */
class PackageMemoryTest {

    private static PackageMemory pcgMemory;
    private static Map<Long, PostalPackage> map = new HashMap<>();

    @BeforeAll
    static void setUp() {
        pcgMemory = PackageMemory.getInstance();

        map.put(1L, new PostalPackage(1L, 13.4, null, "08801"));
        map.put(2L, new PostalPackage(2L, 2.0, null, "90005"));
        map.put(3L, new PostalPackage(3L, 12.56, null, "32479"));
    }

    @Test
    @DisplayName("Saved packages from file")
    void saveFromFile() {
        pcgMemory.getAllPackages().clear();
        pcgMemory.saveFromFile(new ArrayList<>(map.values()));
        assertEquals(3, pcgMemory.getAllPackages().size());
    }

    @Test
    @DisplayName("Adding postal package")
    void save() {
        Long index = Long.valueOf(pcgMemory.getAllPackages().size()) + 1;
        String postalCode = "41501";
        PostalPackage pp = new PostalPackage(index, 12.56, null, postalCode);
        pcgMemory.save(pp);

        assertEquals(1, pcgMemory.getAllPackages().stream().filter(v -> v.getPostalCode().equals(postalCode)).count());
    }

    @Test
    @DisplayName("Recounted fees for packages")
    void recountFeeForPackages() {
        List<Fee> fees = new ArrayList<>();
        fees.add(new Fee(10.0, 5.00));
        fees.add(new Fee(2.0, 2.50));

        pcgMemory.getAllPackages().clear();
        pcgMemory.saveFromFile(new ArrayList<>(map.values()));

        pcgMemory.recountFeeForPackages(fees);

        double fee1 = pcgMemory.getAllPackages().get(0).getFee();
        double fee2 = pcgMemory.getAllPackages().get(1).getFee();
        double fee3 = pcgMemory.getAllPackages().get(2).getFee();

        assertEquals(5.00, fee1);
        assertEquals(2.50, fee2);
        assertEquals(5.00, fee3);
    }
}