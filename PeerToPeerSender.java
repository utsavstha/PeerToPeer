
public class PeerToPeerSender {

    private static MulticastSocket ms;
    private final Context context;
    private final SharedPreferences preferences;

    String MULTI_CAST_IP = "224.2.76.24";
    int MULTI_CAST_PORT = 5500;
    public PeerToPeerSender(Context context) {
        this.context = context;
        preferences = this.context.getSharedPreferences(CommonConstant.MY_PREFERENCES, Context.MODE_PRIVATE);
    }
    /**
    * Use this method to send messages to the network group
    */
    public void sendData(final String message) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                InetAddress sessAddr = null;
                try {
                    sessAddr = InetAddress.getByName(MULTI_CAST_IP);
                    ms = new MulticastSocket(MULTI_CAST_PORT);
                    ms.joinGroup(sessAddr);

                    DatagramPacket dp = new DatagramPacket(message.getBytes(), message.getBytes().length, sessAddr, MULTI_CAST_PORT);
                    ms.send(dp);
                } catch (SocketException se) {
                    System.err.println(se);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    ms.leaveGroup(sessAddr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
