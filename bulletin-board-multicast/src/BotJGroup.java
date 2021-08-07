import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ObjectMessage;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class BotJGroup implements Receiver{
    JChannel channel;
    String user_name = System.getProperty("user.name", "n/a");
    final List<String> state=new LinkedList<>();

    private void start() throws Exception{
        System.out.println(System.setProperty("java.net.preferIPv4Stack", "true"));
        channel = new JChannel().setReceiver(this).connect("BulletinBoard");
        channel.getState(null, 10000);
        eventLoop();
        channel.close();
    }

    public void getState(OutputStream output) throws Exception {
        synchronized(state) {
            Util.objectToStream(state, new DataOutputStream(output));
        }
    }

    public void setState(InputStream input) throws Exception {
        List list= Util.objectFromStream(new DataInputStream(input));
        synchronized(state) {
            state.clear();
            state.addAll(list);
        }
        System.out.println("received state (" + list.size() + " messages in chat history):");
        list.forEach(System.out::println);
    }


    @Override
    public void viewAccepted(View new_view){
        //viewAccepted() is called whenever a new instance joins the cluster, or existing instance leaves
        System.out.println("view: " + new_view);
    }

    @Override
    public void receive(Message msg){
        //receive() is called whenever a message is received from external
        System.out.println(msg.getSrc() + ": " + msg.getObject());
    }

    private void eventLoop(){
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            System.out.println("> ");
            System.out.flush();
            try{
                String line = in.readLine().toLowerCase(Locale.ROOT);
                if (line.startsWith("quit") || line.startsWith("exit")){
                    break;
                }
                line = "[" + user_name + "]" + line;
                Message msg = new ObjectMessage(null, line);
                channel.send(msg);
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new BotJGroup().start();
    }
}