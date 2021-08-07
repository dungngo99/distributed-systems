import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    private static final Scanner scanner = new Scanner(System.in);

    /*
        Stage 1: enter your name
    */
    public static String register(Election election) throws RemoteException {
        System.out.print("Please enter your name: ");
        String input = scanner.nextLine();
        if (input.equals("") || input.equals("0") || input.equals("Exit")) return "Exit";

        if (!election.didRegister(input)){
            System.out.printf("Hi %s, do you want to register; 2=Yes, 1=No, 0=Exit? ", input);
            while (true){
                try{
                    int option = Integer.parseInt(scanner.nextLine());
                    if (option == 1) return "";
                    if (option == 0) return "Exit";
                    if (option == 2){
                        election.register(input);
                        System.out.println("Registered successfully");
                        break;
                    }
                    else throw new Exception();
                } catch (Exception e){
                    System.out.println("Invalid input");
                }
            }
        }
        else System.out.printf("Welcome back %s! %n", input);
        return input;
    }

    /*
        Stage 2: return an option to vote
     */
    public static int chooseOption() {
        while(true) {
            System.out.print("Choose an option (1; 2; 3; 4). 1=Vote, 2=Query, 3=Profile, 4=Exit: ");
            try {
                int option = Integer.parseInt(scanner.nextLine());
                if (option == 4) return -1;
                if (option == 1 || option == 2 || option == 3) return option;
                else throw new Exception();
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        }
    }

    /*
        Vote for a specific candidate
     */
    public static void vote(Election election, int option, String name) throws RemoteException{
        if (option == 3) {
            System.out.printf("Your id is %d%n", election.getVoterID(name));
            return;
        }

        System.out.print("Enter a candidate's name: ");
        String candidate = scanner.nextLine();
        switch (option){
            case 1:
                boolean res = election.vote(candidate, name);
                if (res){
                    System.out.println("You has successfully voted for " + candidate + "!");
                }else{
                    System.out.println("Failed to vote");
                }
                break;
            case 2:
                String response = election.getResult(candidate);
                if (response.equals("")) {
                    System.out.println("Candidate not found!");
                } else {
                    System.out.println(response);
                }
        }
    }

    public static void main(String[] args) {
        String id = args.length < 1 ? null : args[0];

        try {
            Registry registry = LocateRegistry.getRegistry(id);
            Election election = (Election) registry.lookup("Election");

            while(true) {
                String name = register(election);
                if (!name.equals("") && ! name.equals("Exit")){
                    while (true){
                        int option = chooseOption();
                        if (option != -1){
                            vote(election, option, name);
                        }else{
                            break;
                        }
                    }
                }
                else if (name.equals("Exit")){
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
