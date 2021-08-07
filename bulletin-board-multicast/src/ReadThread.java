import java.net.*;
import java.io.*;

public class ReadThread implements Runnable {
    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private static final int MAX_LEN = 1000;
    private MyPacket packet;

    public ReadThread(MulticastSocket socket, InetAddress group, int port, MyPacket packet){
        this.socket = socket;
        this.group = group;
        this.port = port;
        this.packet = packet;
    }

    /**
     * deserialize an array of bytes to a MyPacket object
     * @param data
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static MyPacket deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return (MyPacket) is.readObject();
    }

    @Override
    public void run(){
        while (!BotUDP.finished){
            byte[] buffer = new byte[MAX_LEN];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, port);

            try{
                // add a buffer to receive data from a socket
                socket.receive(packet);

                // delay message receive time
                Thread.sleep((long) (Math.random() * 1000));

                // deserialize an array of bytes to a MyPacket object
                MyPacket receivedPacket = deserialize(packet.getData());

                // Implement FIFO ordering
                int sender = receivedPacket.id;
                if (receivedPacket.S == this.packet.sequenceNo[sender] + 1){
                    System.out.println(receivedPacket.message);
                    this.packet.sequenceNo[sender]++;
                }else if (receivedPacket.S > this.packet.sequenceNo[sender] + 1){
                    this.packet.holdBackQueue.add(packet);
                }

            }catch (IOException | ClassNotFoundException | InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
