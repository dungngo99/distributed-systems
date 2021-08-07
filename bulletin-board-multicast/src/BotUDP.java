import java.io.*;
import java.net.*;
import java.util.*;

public class BotUDP {
    // Constant class variables that are shared by all processes
    static final String TERMINATE = "exit";
    static final int MAX_MEMBERS = 10;
    static String name = System.getProperty("user.name", "n/a");
    static volatile boolean finished = false;

    // instance variables that are unique to each process
    private final InetAddress group;
    private final int portNum;
    private final MulticastSocket socket;
    private final MyPacket packet;

    /**
     *
     * @param host identifies a server (host) that can multicast messages
     * @param port determines a specific port to send or receive messages
     * @param id determines a unique id of a process
     * @throws IOException ...
     */
    public BotUDP(String host, String port, int id) throws IOException {
        packet = new MyPacket(new LinkedList<>(), 0, new int[MAX_MEMBERS], "", id);
        group = InetAddress.getByName(host);
        portNum = Integer.parseInt(port);
        socket = new MulticastSocket(portNum);
        socket.joinGroup(group);
    }

    /**
     * A While Loop to illustrate the chat action
     * @throws IOException ...
     * @throws InterruptedException ...
     */
    public void chat() throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name:");
        name = name + "-" + sc.nextLine();

        System.out.println("Start typing messages...");
        while (true){
            packet.message = name + ":" + sc.nextLine();
            packet.S++; // increment sequence number of last message sent

            // user tries to exit the chat
            if (packet.message.contains(BotUDP.TERMINATE)){
                finished = true;
                socket.leaveGroup(group);
                socket.close();
                break;
            }

            // convert a packet as an object to array of bytes
            byte[] buffer = serialize(packet);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, portNum);

            // randomize the message delay time
            Thread.sleep((long) (Math.random()*5000));

            // send the message through socket then multicast to other ports
            socket.send(packet);
        }
    }

    /**
     * serialize an object to an array of bytes
     * @param obj ...
     * @return ...
     * @throws IOException ...
     */
    public static byte[] serialize(MyPacket obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static void main(String[] args){
        if (args.length < 3){
            System.out.println("Two arguments required: <multicast-host> <port-number>");
        }else{
            try{
                // create an chat bot to send messages
                BotUDP bot = new BotUDP(args[0], args[1], Integer.parseInt(args[2]));

                // generate a thread of a current chat bot to receive messages
                Thread thread = new Thread(new ReadThread(bot.socket, bot.group, bot.portNum, bot.packet));
                thread.start();

                // start a chat process
                bot.chat();
            }catch (IOException | InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
