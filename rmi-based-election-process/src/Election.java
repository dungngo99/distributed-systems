import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Election extends Remote{
    boolean vote(String candidate, String voter) throws RemoteException;
    String getResult(String candidate) throws RemoteException;
    void register(String voterName) throws RemoteException;
    boolean didRegister(String voterName) throws RemoteException;
    int getVoterID(String voterName) throws RemoteException;
}