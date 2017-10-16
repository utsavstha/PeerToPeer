
public class PeerToPeerReceiver{

    Intent p2p;
    Context context;
    String MULTI_CAST_IP = "224.2.76.24";
    int MULTI_CAST_PORT = 5500;
    public PeerToPeerReceiver(Context context) {
        p2p = new Intent("P2p");
        this.context = context;
    }

    /**
    * Starts listening on a network group
    * everytime a device sends a message to this group
    * this thread receives the message.
    */
    public void startListening(){
        Thread thread = new Thread(new Runnable() {

            private String stringData = null;

            @Override
            public void run() {
                InetAddress ia = null;
                byte[] buffer = new byte[65535];
                MulticastSocket ms = null;
                try {
                    ia = InetAddress.getByName(MULTI_CAST_IP);
                    DatagramPacket dp = new DatagramPacket(buffer, buffer.length, ia, CommonConstant.MULTI_CAST_PORT);
                    ms = new MulticastSocket(MULTI_CAST_PORT);
                    ms.joinGroup(ia);
                    while (true) {
                        ms.receive(dp);
                        String s = new String(dp.getData(), 0, dp.getLength());
                        p2p.putExtra("data", s);
                        context.sendBroadcast(p2p);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    ms.leaveGroup(ia);
                } catch (IOException e) {

                }
            }
        });
        thread.start();
    }

}
