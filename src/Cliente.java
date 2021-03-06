import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Cliente extends Thread {
    private ServerSocket serverSocket;
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] pos = new byte[256];
    int posicaoLista=0;
    private static String ip;
    private DatagramSocket com;

    public Cliente() {
    }

    public Cliente(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    private static final String menu() {
        return "MENU CLIENTE\n" +
                "0 - Menu Inicial\n"+
                "1 - Listar utilizadores online\n"+
                "2 - Enviar mensagem a um utilizador\n"+
                "3 - Enviar mensagem a todos os utilizadores\n"+
                "4 - lista branca de utilizadores\n"+
                "5 - lista negra de utilizadores\n"+
                "99 – Sair\n";
    }

    public void run()
    {
        Socket socket;

        try {
            socket = new Socket(ip, 7142);
            //BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream ps = new PrintStream(socket.getOutputStream());

            int userName = 0;
            do {
                System.out.println(menu());
                System.out.println("Indique o comando que queira executar: ");
                Scanner opcaoMenu = new Scanner(System.in);
                userName = opcaoMenu.nextInt();
                switch (userName){
                    case 0:
                        System.out.println(menu());
                        continue;
                    case 1:
                        ps.println("1");
                        System.out.println("Utilizadores onlines:\n");
                        break;
                    case 2:
                        ps.println("2");
                        try {
                           // input.readLine();
                            Scanner op = new Scanner(System.in);
                            String mensagemIP = op.nextLine();
                            ps.println(mensagemIP);
                            //input.readLine();
                            Scanner opc = new Scanner(System.in);
                            String mensagem = opc.nextLine();
                            ps.println(mensagem);
                            this.address = InetAddress.getByName(ip);
                            pos = mensagem.getBytes();
                            DatagramPacket packet = new DatagramPacket(pos, pos.length, address, 9031);
                            com.send(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        ps.println("3");
                        Scanner op = new Scanner(System.in);
                        String mensagem = op.nextLine();
                        ps.println(mensagem);
                        com = new DatagramSocket();
                        this.address = InetAddress.getByName(ip);
                        pos = mensagem.getBytes();
                        DatagramPacket packet = new DatagramPacket(pos, pos.length, address, 9031);
                        com.send(packet);
                        break;

                    case 4:
                        ps.println("4");
                        System.out.println("Lista branca:\n");
                        System.out.println(Servidor.listaOnline);
                        break;
                    case 5:
                        ps.println("5");
                        System.out.println("Lista negra:\n");
                        System.out.println(Servidor.listaNegra);
                        break;
                    case 99:
                        System.out.println("A Sair\n");
                        System.out.println("Cliente Desconhectado..");
                        socket.close();
                        break;
                }
            }while (userName != 99);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // usage: java EchoClient <servidor> <mensagem>
    public static void main(String args[])
    {
        Cliente novo = new Cliente();
        novo.start();
    }
}
