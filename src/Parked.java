import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Parked {
    // ALL PARKED VEHICLES
    private int numOfVehicle;
    private int numOfRepVehicle;
    private String plateNumber;
    private String carType;
    private String parkingSlot;
    private String timeIn;

    public Parked(){
        numOfVehicle = 0;
        numOfRepVehicle = 0;
        this.plateNumber = "";
        this.carType = "";
        this.parkingSlot = "";
        this.timeIn = "";
    }

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int choice;
    
        System.out.println("\t\t----------------------------");
        System.out.println("\t\tPARKING LOT MANAGEMENT SYSTEM");
        System.out.println("\t\t----------------------------");
    
        System.out.println("\n\t\t1. View All Parked Vehicles");
        System.out.println("\n\t\t2. Park a Vehicle");
        System.out.println("\n\t\t3. Remove a Vehicle");
        System.out.println("\n\t\t4. Generate a Parking Report");
        System.out.println("\n\t\t5. Exit Application");
    
        while(true){
            System.out.print("\n\t\tEnter Number of Choice: ");
            try{
                choice = input.nextInt();
                input.nextLine();
                break;
            }catch(InputMismatchException err){
                System.out.println("ERROR: Invalid input. Please try again.");
                input.nextLine();
            }
        }

        // OBJECT DECLARATION
        Parked parked = new Parked();
            
        switch(choice){
            case 1:
                clearScreen();

                System.out.println("\t\t----------------------------");
                System.out.println("\t\t VIEW ALL PARKED VEHICLES");
                System.out.println("\t\t----------------------------");

                System.out.println("\n#" + "\t  Date" + "\t\tTime-in" + "\t        Plate Number" + "    Vehicle Type" + "\tParking Slot");
                parked.readFile("parked.txt");
                break;
            case 2:
                clearScreen();

                System.out.println("\t\t----------------------------");
                System.out.println("\t\t      PARK A VEHICLE");
                System.out.println("\t\t----------------------------");

                parked.parkVehicle(input);
                break;
            case 3:
                clearScreen();

                System.out.println("\t\t----------------------------");
                System.out.println("\t\t    REMOVE A VEHICLE");
                System.out.println("\t\t----------------------------");

                parked.removeVehicle(input);
                break;
            case 4:
                clearScreen();
                float calculateCollectedFees = parked.totalCollectedFees("report.txt");

                System.out.println("\t\t----------------------------");
                System.out.println("\t\t  GENERATE PARKING REPORT");
                System.out.println("\t\t----------------------------");

                System.out.println("\n#" + "\t  Date" + "\t\tTime-in" + "\t        Plate Number" + "    Vehicle Type" + "\tHours Parked" + "\tFee(PHP)");
                parked.readFile("report.txt");

                System.out.println("\n\tTotal Number of Vehicles: " + parked.numOfRepVehicle);
                System.out.println("\tTotal Fees Collected: PHP " + calculateCollectedFees);

                break;
            case 5:
                System.out.println("\n\t\t\tThank you!");
                return;
            default:
                System.out.println("\n\tThat doesn't look like it's part of the options. Please try again.");
        }
    }

    public static void clearScreen(){
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    // TOTAL FEES COLLECTED
    public float totalCollectedFees(String reportFile){
        float totalFees = 0.0f;

        try(BufferedReader br = new BufferedReader(new FileReader(reportFile))){
            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split("\\s++");
                if(parts.length >= 7){
                    try{
                        float fee = Float.parseFloat(parts[7].trim());
                        totalFees += fee;
                        numOfRepVehicle++;
                    } catch(NumberFormatException err){
                        System.out.println("ERROR: Invalid fee format.");
                        System.exit(0);
                    }
                }
            }
        } catch(IOException err){
            System.out.println("ERROR: Report list is corrupted or does not exist. Please try again.");
            System.exit(0);
        }
        
        return totalFees;
    }

    // GET REAL-TIME DATE
    public String getCurrentDate(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return today.format(formatter);
    }

    // CREATE FILE
    public void createFile(String fileName){
        try{
            File allRemovedVehicles = new File(fileName);
            if(allRemovedVehicles.createNewFile()){
                // means FILE doesn't yet exist
            } else{
                // means FILE already exists
            }
        } catch(IOException err){
            System.out.println("ERROR: File creation is corrupted. Please try again.");
            return;
        }
    }

    // WRITE TO FILE
    public void writeFile() {
        String date = getCurrentDate();
        try{
            FileWriter dataWriter = new FileWriter("parked.txt", true);
            dataWriter.write(numOfVehicle);
            dataWriter.write("\t" + date);
            dataWriter.write("\t" + timeIn);
            dataWriter.write("\t\t" + plateNumber);
            dataWriter.write("\t\t" + carType);
            dataWriter.write("\t\t" + parkingSlot);
            dataWriter.write("\n");
            dataWriter.close();
            System.out.println("\n\t\tParked Vehicles Updated!");
        } catch(IOException err){
            System.out.println("ERROR: Cannot update list. Please try again.");
            return;
        }
    }

    // READ FILE
    public void readFile(String fileName) {
        try{
            File file = new File(fileName);
            Scanner dataScanner = new Scanner(file);
            while (dataScanner.hasNextLine()) {
                String data = dataScanner.nextLine();
                System.out.println(data);
            }
            dataScanner.close();
        } catch(FileNotFoundException err){
            System.out.println("\t\tERROR: " + fileName + " could not be found.");
            err.printStackTrace();
        }
    }

    // PARK A VEHICLE
    public void parkVehicle(Scanner input){
        System.out.print("\t\tPlate Number: ");
        plateNumber = input.nextLine().trim();
        System.out.print("\t\tVehicle Type (Car, Van, Motorcycle): ");
        carType = input.nextLine().trim();
        System.out.print("\t\tParking Slot (P1, P2, ...): ");
        parkingSlot = input.nextLine().trim();
        System.out.print("\t\tTime-in: ");
        timeIn = input.nextLine();

        numOfVehicle++;

        createFile("parked.txt");
        writeFile();
    }

    // REMOVE A VEHICLE
    public void removeVehicle(Scanner input) {
        System.out.print("\tPlate Number: ");
        String findPlateNo = input.nextLine().trim();
        
        boolean found = false;
        List<String> updatedLines = new ArrayList<>(); 
    
        try (BufferedReader br = new BufferedReader(new FileReader("parked.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
    
                int plateIndex = -1;
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equalsIgnoreCase(findPlateNo)) {
                        plateIndex = i;
                        break;
                    }
                }
    
                if (plateIndex != -1 && parts.length >= 5) {
                    String timeIn = parts[1] + " " + parts[2];
                    String carType = parts[4];
    
                    System.out.println("\tType: " + carType);
                    System.out.println("\tTime-in: " + timeIn);
                    
                    System.out.print("\tTime-out: ");
                    String timeOut = input.nextLine().trim();
    
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
                        LocalTime timeInParsed = LocalTime.parse(timeIn, formatter);
                        LocalTime timeOutParsed = LocalTime.parse(timeOut, formatter);
    
                        Duration duration = Duration.between(timeInParsed, timeOutParsed);
                        int hoursParked = (int) duration.toHours();
    
                        float totalFee = 0;
                        if(carType == "Car" || carType == "Van"){
                            totalFee = 20.0f * hoursParked;
                        } else if(carType == "Motorcycle"){
                            totalFee = 10.0f * hoursParked;
                        } else{
                            System.out.println("\tCar Type does not match any vehicle in the list.");
                        }

                        System.out.println("\tTotal Hours Parked: " + hoursParked);
                        System.out.println("\tFee: PHP " + totalFee);

                        numOfRepVehicle++;
                        try(BufferedWriter bw = new BufferedWriter(new FileWriter("report.txt", true))){
                            bw.write(numOfRepVehicle);
                            bw.write("\t" + getCurrentDate());
                            bw.write("\t" + timeIn); 
                            bw.write("\t\t" + findPlateNo);
                            bw.write("\t\t" + carType); 
                            bw.write("\t\t" + hoursParked); 
                            bw.write("\t\t" + totalFee);
                            bw.newLine();
                        }
    
                        found = true;  
                    } catch(Exception err){
                        System.out.println("\tERROR: Invalid time format. Please use h:mm AM/PM.");
                        return;
                    }
    
                } else{
                    updatedLines.add(line);
                }
            }
        } catch(IOException err){
            System.out.println("\tERROR: Parking list is corrupted or does not exist. Please try again.");
            return;
        }
    
        if(found){
            try(BufferedWriter bw = new BufferedWriter(new FileWriter("parked.txt"))){
                for(String updatedLine : updatedLines){
                    bw.write(updatedLine);
                    bw.newLine();
                }
                numOfVehicle--;
                System.out.println("\tVehicle with Plate Number " + findPlateNo + " has been removed.");
            } catch(IOException err){
                System.out.println("\tERROR: Cannot update list. Please try again.");
                return;
            }
        } else{
            System.out.println("\tVehicle with Plate Number " + findPlateNo + " not found.");
        }
    }    
}
