import java.io.*;
import java.util.*;

class UserInputThread extends Thread {
    private final Scanner sc = new Scanner(System.in);
    private final int userCount;

    UserInputThread(int userCount) {
        this.userCount = userCount;
    }

    public static void writeToFile(String name, String id, int year, String quarter, double amount, String comments) {
        try {
            String filePath = "UserDetails.txt";
            FileWriter fileWriter = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(name + "," + id + "," + year + "," + quarter + "," + amount + "," + comments);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void clearFileContent() {
        try {
            new FileWriter("UserDetails.txt", false).close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void run() {
        double totalAmount = 0;

        for (int i = 0; i < userCount; i++) {
            System.out.println("Enter details for user #" + (i + 1));
            System.out.print("Enter name: ");
            String name = sc.nextLine();

            System.out.print("Enter ID: ");
            String id = sc.nextLine();

            System.out.print("Enter year: ");
            int year = sc.nextInt();

            System.out.print("Enter quarter: ");
            String quarter = sc.next();

            System.out.print("Enter amount: ");
            double amount = sc.nextDouble();
            totalAmount += amount;

            sc.nextLine();

            System.out.print("Enter comments: ");
            String comments = sc.nextLine();
            writeToFile(name, id, year, quarter, amount, comments);
        }

        System.out.println("Total amount entered by all users: " + totalAmount);
    }
}

class FileReadThread extends Thread {
    private final int repeatCount;

    FileReadThread(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public void run() {
        for (int i = 0; i < repeatCount; i++) {
            try {
                List<String> data = new ArrayList<>();
                BufferedReader bufferedReader = new BufferedReader(new FileReader("UserDetails.txt"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    data.add(line);
                }
                bufferedReader.close();
                System.out.println("Data read from file:");
                for (String item : data) {
                    System.out.println(item);
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}

public class UserManagement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of users: ");
        int userCount = sc.nextInt();

        System.out.print("Do you want to clear the existing file content? (yes/no): ");
        sc.nextLine(); // Consume newline
        String clearFile = sc.nextLine();
        if (clearFile.equalsIgnoreCase("yes")) {
            UserInputThread.clearFileContent();
        }

        UserInputThread userInputThread = new UserInputThread(userCount);
        userInputThread.start();

        try {
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }

        int readRepeatCount = 1;
        FileReadThread fileReadThread = new FileReadThread(readRepeatCount);
        fileReadThread.start();
    }
}
