import java.io.Serializable;
import java.net.DatagramPacket;
import java.util.Queue;

/**
 * An object to store different types of data
 */
public class MyPacket implements Serializable {;
    public Queue<DatagramPacket> holdBackQueue;
    public int S;
    public int[] sequenceNo;
    public String message;
    public int id;

    public MyPacket(Queue<DatagramPacket> queue, int S, int[] sequenceNo, String message, int id){
        this.holdBackQueue = queue;
        this.S = S;
        this.sequenceNo = sequenceNo;
        this.message = message;
        this.id = id;
    }
}
