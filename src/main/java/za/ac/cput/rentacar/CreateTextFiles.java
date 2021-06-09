/**
 *
 * @author Abel Kabeya 217174183
 */
package za.ac.cput.rentacar;

public class CreateTextFiles {

    public static void main(String[] args) {

        ReadSerFile ss = new ReadSerFile();
        ss.openSerfile();
        ss.readSerFile();
        ss.writeToCustomerFile();
        ss.writeSupplierFile();

    }

}
