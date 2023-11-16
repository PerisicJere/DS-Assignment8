import java.io.*;
import java.util.*;

public class program8 {
    public static void main(String[] args) {
        Map<String, myAVL> carMakeTrees = new TreeMap<>();
        String file = args[0];
        String testFile = args[1];
        String resultFile = "result.csv";
        long startTime = System.currentTimeMillis();
        insertFromCSV(file, carMakeTrees);
        processTestFile(testFile, carMakeTrees, resultFile);
        long endTime = System.currentTimeMillis();
        System.out.println((double) (endTime-startTime)/1000);


    }
    private static SaleRecord parsingCSV(String line) {
        String[] parts = line.split(",");
        if (parts.length != 9) {
            return null;
        }
        String dateStr = parts[0];
        String salesperson = parts[1];
        String customerName = parts[2];
        String carMake = parts[3];
        String carModel = parts[4];
        int carYear = Integer.parseInt(parts[5]);
        double salePrice = Double.parseDouble(parts[6]);
        double commissionRate = Double.parseDouble(parts[7]);
        double commissionEarned = Double.parseDouble(parts[8]);

        return new SaleRecord(dateStr, salesperson, customerName, carMake, carModel, carYear, salePrice, commissionRate, commissionEarned);
    }

    private static void insertFromCSV(String csvFilePath, Map<String, myAVL> carMakeTrees) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean skipHeader = true;
            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                SaleRecord saleRecord = parsingCSV(line);
                if (saleRecord != null) {
                    String carMake = saleRecord.getCarMake();
                    if (!carMakeTrees.containsKey(carMake)) {
                        carMakeTrees.put(carMake, new myAVL());
                    }
                    myAVL saleRecordTree = carMakeTrees.get(carMake);
                    saleRecordTree.add(saleRecord);}
            }
        } catch (IOException e) {
            e.printStackTrace();}
    }
    private static void processTestFile(String testFile, Map<String, myAVL> carMakeTrees, String resultFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(testFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) {
                    continue;}
                String carMake = parts[0].trim();
                String date = parts[1].trim();
                myAVL saleRecordTree = carMakeTrees.get(carMake);
                if (saleRecordTree != null) {
                    List<SaleRecord> saleRecords = saleRecordTree.search(date);
                    writer.write(saleRecords.size() + "\n");
                }else {
                    writer.write(0 + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();}
    }
}