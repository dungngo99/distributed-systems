import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server implements Election {
    private static final int capacity = 10000;
    private static final Hashtable<String, Integer> ballot = new Hashtable<>();
    private static final Hashtable<String, Integer> voters = new Hashtable<>();
    private static final boolean[] voterID = new boolean[capacity];
    private static final boolean[] hasVoted = new boolean[capacity];

    private static final File fileBallot = new File("ballot.txt");
    private static final File fileVoter = new File("voters.txt");
    private static final File fileVoterID = new File("voterIDs.txt");
    private static final File fileHasVoted = new File("hasVoted.txt");

    public Server() {
        getStorage(fileBallot, "ballot");
        getStorage(fileVoter, "voter");
        getStorage(fileVoterID, "voterID");
        getStorage(fileHasVoted, "hasVoted");
    }

    public void writeToStorage(String result, File file) {
        FileWriter fr = null;
        BufferedWriter br = null;
        try {
            fr = new FileWriter(file, true);
            br = new BufferedWriter(fr);
            br.write(result);
            br.newLine();
            br.close();
        } catch (IOException e) {
            System.out.println("Unable to write to file.");
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) fr.close();
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getStorage(File file, String fileName) {
        Scanner scanner = null;
        try {
            if (file.createNewFile()) {
                System.out.println("File is created");
            } else {
                System.out.println("File already created");
            }

            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] strings = line.split(",");

                switch (fileName){
                    case "ballot":
                        ballot.put(strings[0], Integer.parseInt(strings[1]));
                        break;
                    case "voter":
                        voters.put(strings[0], Integer.parseInt(strings[1]));
                        break;
                    case "voterID":
                        voterID[Integer.parseInt(strings[0])] = Boolean.parseBoolean(strings[1]);
                        break;
                    case "hasVoted":
                        hasVoted[Integer.parseInt(strings[0])] = Boolean.parseBoolean(strings[1]);
                        break;
                 }
            }
        } catch (IOException e) {
            System.out.println("Unable to read file");
        } finally {
            System.out.println(Arrays.toString(ballot.keySet().toArray()));
            if (scanner != null) scanner.close();
        }
    }

    @Override
    public void register(String voterName) {
        if (voters.containsKey(voterName)){
            System.out.println("You has already registered");
        }

        int voteID = (int) (Math.random() * capacity);
        while (voterID[voteID]){
            voteID = (int) (Math.random() * capacity);
        }

        voters.put(voterName, voteID);
        writeToStorage(voterName + "," + voteID, fileVoter);
        voterID[voteID] = true;
        writeToStorage(voteID + "," + true, fileVoterID);
    }

    @Override
    public boolean didRegister(String voterName){
        return voters.containsKey(voterName);
    }

    @Override
    public int getVoterID(String voterName){
        return voters.get(voterName);
    }

    @Override
    public boolean vote(String candidate, String voterName) {
        if (candidate.equals(voterName)){
            System.out.println("You can't vote for yourself!");
            return false;
        }

        int voterID = voters.get(voterName);
        if (Server.hasVoted[voterID]) {
            System.out.println("This voter has already voted!");
            return false;
        }

        Server.hasVoted[voterID] = true;
        writeToStorage(voterID + "," + true, fileHasVoted);

        if (!ballot.containsKey(candidate)) {
            ballot.put(candidate, 1);
        } else {
            int currentVote = ballot.get(candidate);
            ballot.put(candidate, currentVote + 1);
        }
        writeToStorage(candidate + "," + ballot.get(candidate), fileBallot);
        System.out.println("Updated votes for " + candidate);
        return true;
    }

    @Override
    public String getResult(String candidate) {
        if (!ballot.containsKey(candidate)) return "";
        return candidate + " has " + ballot.get(candidate) + " vote(s).";
    }

    public static void main(String[] args) {
        Server obj = new Server();
        try {
            Election stub = (Election) UnicastRemoteObject.exportObject(obj, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Election", stub);

            System.err.println("Server ready");
        } catch (AlreadyBoundException | RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
