import java.io.IOException;
import java.net.*;


public class Udp {
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buf = new byte[256];

    public Udp(String address) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        this.address = InetAddress.getByName(address);
    }

    public String sendEcho(String msg) throws IOException {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 9031);
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }


    public static void main(String[] args) {
        try {
            Udp client = new Udp(args[0]);
            System.out.println("Mensagem recebida: " + client.sendEcho(args[1]));
            client.sendEcho("end");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
