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

    public static Scanner lido;

    public void run()
    {
        Socket socket;

        try {
            socket = new Socket(ip, 7142);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream ps = new PrintStream(socket.getOutputStream());

            int userName = 0;
            do {
                System.out.println(menu());
                System.out.println("Indique o comando que queira executar: ");
                Scanner opcaoMenu = new Scanner(System.in);
                userName = opcaoMenu.nextInt();
                switch (userName){
                    case 0:
                        //Apresenta o menu novamente
                        ps.println("0");
                        String texto = input.readLine();
                        while (!texto.equals("")){
                            System.out.println(input.readLine());
                            texto = input.readLine();
                        }
                        break;
                    case 1:
                        ps.println("1");
                        System.out.println("Utilizadores onlines:\n");
                        break;
                    case 2:
                        ps.println("2");
                        try {
                            input.readLine();
                            String mensagemIP = lido.nextLine();
                            ps.println(mensagemIP);
                            input.readLine();
                            String mensagem = lido.nextLine();
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
                        String mensagem = lido.nextLine();
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
                        System.out.println(Servidor.listaBranca);
                        break;
                    case 5:
                        ps.println("5");
                        System.out.println("Lista negra:\n");
                        //System.out.println(listaNegra);
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
        /*
        while (true){

            try {
                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());

                System.out.println(in.readUTF());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
                        + "\nGoodbye!");
                server.close();

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

         */
    }
    // usage: java EchoClient <servidor> <mensagem>
    public static void main(String args[]) {
        Cliente novo = new Cliente();
        novo.start();
        ip = args[0];



       /*
       lido= new Scanner(System.in);



       //Apresenta o menu
        menu();

        //Leitura da opção
        Scanner opcaoMenu = new Scanner(System.in);
        int userName = opcaoMenu.nextInt();




        Socket socket = new Socket(args[0], 6500);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        PrintStream ps = new PrintStream(socket.getOutputStream());
        ps.println(args[1]); // escreve mensagem na socket
// imprime resposta do servidor
        System.out.println("Recebido : " + br.readLine());
// termina socket
        socket.close();



        teclado = new Scanner(System.in);
        Cliente cliente = new Cliente();
        cliente.start();
        */
    }

}
