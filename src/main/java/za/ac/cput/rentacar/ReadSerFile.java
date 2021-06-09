/**
 *
 * @author Abel Kabeya 217174183
 */
package za.ac.cput.rentacar;

import java.io.*;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ReadSerFile {

    ObjectInputStream input;
    ArrayList<Customer> customerList = new ArrayList<>();
    ArrayList<Supplier> supplierList = new ArrayList<>();

    public void openSerfile() {// openning the ser file
        try {
            input = new ObjectInputStream(new FileInputStream("stakeholder.ser"));
            System.out.println("stakeholder file is open for reading");
        } catch (IOException ioe) {
            System.out.println("Error while attempting to open ser file " + ioe.getMessage());
        }
    }

    public void closeSerFile() { // closing the ser file
        try {
            input.close();
        } catch (IOException ioe) {
            System.out.println("Error while attempting to close ser file " + ioe.getMessage());
        }
    }

    public void readSerFile() {

        try {
            while (true) {
                Object cusSup = input.readObject();
                if (cusSup instanceof Customer) { // checking if cusSup is a Customer Object
                    customerList.add((Customer) cusSup); // poplulating Customer ArrayList
                    System.out.println(cusSup);// printing read Objects to screen
                }
                if (cusSup instanceof Supplier) { // checking if cusSup is a Supplier Obejct
                    supplierList.add((Supplier) cusSup); // poplulating Supplier ArrayList
                    System.out.println(cusSup);// printing read Objects to screem
                }
            }

        } catch (EOFException eofe) {
            System.out.println("EOF reached");
        } catch (ClassNotFoundException ioe) {
            System.out.println("Class not found: " + ioe);
        } catch (IOException ioe) {
            System.out.println("Error attempting to read from ser file: " + ioe);
        } finally {
            closeSerFile();
            System.out.println("File has been closed ");
        }

    }

    public int formatAge(String date) {

        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        return Period.between(birthDate, today).getYears();

    }

    public String dobFormat(Customer customer) {

        Date date = null;
        DateFormat dF = new SimpleDateFormat("dd MMM yyy");
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(customer.getDateOfBirth());
        } catch (ParseException e) {
            System.out.println("Parse error" + e.getMessage());
        }
        return dF.format(date);

    }

    public void writeToCustomerFile() {

        Collections.sort(customerList, (Customer cus1, Customer cus2)
                -> cus1.getStHolderId().compareTo(cus2.getStHolderId()));

        try {
            FileWriter fw = new FileWriter("CustomerOutput.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            fw.write("==============================CUSTOMERS ===================================\n");
            fw.write(String.format("%-10s\t%-10s\t%-10s\t%-10s\t%-10s\n", "ID", "Name", "Surname", "Date of birth", "Age"));
            fw.write("===========================================================================\n");

            for (Iterator<Customer> it = customerList.iterator(); it.hasNext();) {
                Customer cus = it.next();
                String output = String.format("%-10s\t%-10s\t%-10s\t%-10s\t%-10s",
                        cus.getStHolderId(),
                        cus.getFirstName(),
                        cus.getSurName(),
                        dobFormat(cus),
                        formatAge(cus.getDateOfBirth()));
                fw.write(output + "\n");
            }
            fw.write("\nNumber of customers who can rent: "
                    + customerList.stream().filter(Customer::getCanRent).collect(Collectors.toList()).size() + "\n");
            fw.write("\nNumber of customers who cannot rent: "
                    + customerList.stream().filter(c -> !c.getCanRent()).collect(Collectors.toList()).size());
            fw.close();
        } catch (Exception e) {
            System.out.println(" Exception Error: " + e.getMessage());
            System.out.println(" Error occured when writing to file ");
        }
    }

    public void writeSupplierFile() {

        Collections.sort(supplierList, (Supplier sup1, Supplier sup2)
                -> sup1.getName().compareTo(sup2.getName()));

        try {
            FileWriter fw = new FileWriter("SupplierOutput.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            fw.write("========================== SUPPLIERS  ============================\n");
            fw.write(String.format("%-5s\t%-20s\t%-10s\t%-10s\n", "ID", "Name", "Prod Type", "Description"));
            fw.write("==================================================================\n");

            for (Iterator<Supplier> it = supplierList.iterator(); it.hasNext();) {
                Supplier supp = it.next();
                String output = String.format("%-5s\t%-20s\t%-10s\t%-10s",
                        supp.getStHolderId(),
                        supp.getName(),
                        supp.getProductType(),
                        supp.getProductDescription());
                fw.write(output + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println(" Exception Error: " + e.getMessage());
            System.out.println(" Error occured when writing to file ");
        }
    }

}
