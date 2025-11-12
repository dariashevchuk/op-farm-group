import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Thread> threads = new ArrayList<>();

        try {
            // 1) Read user-defined parameters
            System.out.print("Enter the size of the field (e.g., 10): ");
            int fieldSize = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter the number of farmers (e.g., 2): ");
            int numberOfFarmers = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter the number of rabbits (e.g., 3): ");
            int numberOfRabbits = Integer.parseInt(scanner.nextLine().trim());

            // Validate inputs
            if (fieldSize <= 0 || numberOfFarmers < 0 || numberOfRabbits < 0) {
                throw new IllegalArgumentException(
                        "Field size, farmers, and rabbits must be positive integers (zero allowed for farmers/rabbits)."
                );
            }

            // 2) Create the Field
            Field field = new Field(fieldSize);

            // 3) Create the FarmGroup
            FarmGroup farmGroup = new FarmGroup("Anti-rabbit Corporation");

            // 4) Create and start Farmer (and Dog) threads
            for (int i = 0; i < numberOfFarmers; i++) {
                int startX = (int) (Math.random() * fieldSize);
                int startY = (int) (Math.random() * fieldSize);

                Farmer farmer = new Farmer("Farmer" + i, startX, startY, field);
                farmGroup.addMember(farmer);

                Thread farmerThread = new Thread(farmer);
                threads.add(farmerThread);
                farmerThread.start();
            }

            // 5) Create and start Rabbit threads
            for (int i = 0; i < numberOfRabbits; i++) {
                int startX = (int) (Math.random() * fieldSize);
                int startY = (int) (Math.random() * fieldSize);

                // Random turns for eating: 5-10
                int turns = 5 + (int) (Math.random() * 6);
                Rabbit rabbit = new Rabbit(startX, startY, field, turns);

                field.addRabbit(rabbit);
                Thread rabbitThread = new Thread(rabbit);
                threads.add(rabbitThread);
                rabbitThread.start();
            }

            // 6) Main simulation loop
            System.out.println("\nType 'exit' to stop the simulation or 'save' to save the field to file.\n");

            boolean isRunning = true;
            while (isRunning) {
                // Display the field
                field.displayField(farmGroup);

                System.out.println("Enter 'exit' to stop, or 'save' to save the field to file.");

                // Wait for a few seconds for user input
                long waitUntil = System.currentTimeMillis() + 5000; // 5 seconds
                while (System.currentTimeMillis() < waitUntil) {
                    if (System.in.available() > 0) {
                        String command = scanner.nextLine().trim().toLowerCase();
                        if (command.equals("exit")) {
                            System.out.println("Stopping all threads...");
                            isRunning = false;
                            break;
                        } else if (command.equals("save")) {
                            field.saveField("field_save.txt", farmGroup);
                        } else {
                            System.out.println("Unknown command. Try again.");
                        }
                    }
                }
                System.out.println("-----");
            }

            // 7) Stop all Farmer and Dog threads
            for (Farmer farmer : farmGroup.getMembers()) {
                farmer.stopEntity();
                farmer.getDog().stopEntity();
            }

            // (Optional) Wait for all threads to finish
            for (Thread t : threads) {
                t.join(1000);
            }

        } catch (Exception e) {
            System.err.println("Error in main: " + e.getMessage());
        } finally {
            scanner.close();
        }

        System.out.println("Program terminated.");
    }
}
