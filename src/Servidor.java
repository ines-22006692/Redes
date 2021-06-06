import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Servidor {
    static final File blackList = new File("listaPreta.txt");
    static final File whiteList = new File("listaBranca.txt");
    static HashMap<String, String> listaNegra = new HashMap();
    static HashMap<String, String> listaBranca = new HashMap();
     static ArrayList<String> listaOnline = new ArrayList<String>();
    static BufferedReader leitura;
    static PrintStream printStream;

    public static boolean verificacaoLista(String ipAddress) {
        try {
            //lista Preta em primeiro uma vez que em prioridade
            Scanner listaLeituraPreta = new Scanner(blackList);
            while (listaLeituraPreta.hasNextLine()) {
                if (ipAddress.contains(listaLeituraPreta.nextLine())) {
                    listaLeituraPreta.close();
                    return false;
                }
            }
            listaLeituraPreta.close();



            Scanner listaLeituraBranca = new Scanner(whiteList);


            while (listaLeituraBranca.hasNextLine()) {
                String linha = listaLeituraBranca.nextLine();
                if (ipAddress.contains(linha)) {
                    listaLeituraBranca.close();
                    return true;
                }
            }
            listaLeituraBranca.close();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
            }

    }

     static class Server_Manager implements Runnable {
        private  final String menu() {
            return "MENU CLIENTE\n" +
                    "0 - Menu Inicial\n" +
                    "1 - Listar utilizadores online\n" +
                    "2 - Enviar mensagem a um utilizador\n" +
                    "3 - Enviar mensagem a todos os utilizadores\n" +
                    "4 - lista branca de utilizadores\n" +
                    "5 - lista negra de utilizadores\n" +
                    "99 – Sair\n";
        }
        public Server_Manager (Socket s) throws IOException{
             this.socket = s;
             //s.start()
             leitura= new BufferedReader(new InputStreamReader(socket.getInputStream()));
             printStream = new PrintStream(socket.getOutputStream());
         }
        private DatagramSocket com;
        private byte[] pos = new byte[256];
        private Socket socket;
        private InetAddress address;
        private byte[] buf = new byte[256];

        public void run() {
            int userName = 0;
            String clientIP = socket.getInetAddress().toString();
            System.out.println(clientIP);
            if (verificacaoLista(clientIP))
            {
                listaOnline.add(clientIP);
            } else {
                try {
                    socket.close();
                    System.out.println("Connecção rejeitada");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Scanner opcaoMenu = new Scanner(System.in);
                userName = opcaoMenu.nextInt();
                do {
                    switch (userName) {
                        case 0:
                            printStream.println(menu());
                            break;
                        case 1: {
                            printStream.println(listaOnline);
                            break;
                        }
                        case 2: {
                            System.out.println("Qual é o IP que deseja enviar uma mensagem? ");
                            String mensagemIP = leitura.readLine();
                            StringBuilder answer = new StringBuilder();
                            System.out.println("Qual é a mensagem que pretende enviar? ");
                            Scanner op = new Scanner(System.in);
                            String mensage = op.nextLine();
                            byte[] mensagemBytes = mensage.getBytes();
                            com = new DatagramSocket();

                            try {
                                this.address = InetAddress.getByName(mensagemIP);
                                DatagramPacket packet = new DatagramPacket(mensagemBytes, mensagemBytes.length, address, 9031);
                                com.send(packet);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            printStream.println("mensagem entregue");
                            com.close();
                            //printStream a fechar??
                            break;
                        }
                        case 3: {
                            System.out.println("Qual é a mensagem que pretende enviar? ");
                            Scanner opc = new Scanner(System.in);
                            String mens = opc.nextLine();
                            byte[] mensagemBytes = mens.getBytes();
                            com = new DatagramSocket();

                            for (String i : listaOnline ) {
                                try {
                                    this.address = InetAddress.getByName(i);
                                    DatagramPacket packet = new DatagramPacket(mensagemBytes, mensagemBytes.length, address, 9031);
                                    com.send(packet);
                                } catch (IOException error) {
                                    error.printStackTrace();
                                }
                            }
                            printStream.println("mensagem entregue");
                            com.close();
                            break;
                        }
                        case 4: {
                            StringBuilder branca = new StringBuilder();
                            for (String host : listaBranca.keySet()){
                                branca.append(host).append(listaBranca.get(host));
                            }
                            printStream.println(whiteList);
                            break;
                        }
                        case 5: {
                            StringBuilder negra = new StringBuilder();
                            for (String host : listaNegra.keySet()){
                                negra.append(host).append(listaNegra.get(host));
                            }
                           // System.out.println(blackList);
                           // printStream.println(blackList);
                            break;
                        }
                        case 99: {
                            System.out.println("A Sair\n");
                            System.out.println("Cliente Desconhectado..");
                            break;
                        }
                    }
                } while (userName != 99);
            } catch (IOException error) {
                error.printStackTrace();
            }

        }

    }

    public static void main(String args[]) throws Exception {
        ServerSocket server = new ServerSocket(7142);
        InetAddress myIPaddress = InetAddress.getLocalHost();
        //System.out.println(myIPaddress.toString() + ":" + 7142 );
        Socket client = null;
        try {
            while (true){
                client = server.accept();
                Thread t = new Thread(new Server_Manager(client));
                t.start();
            }
        }catch (Exception e) {
            System.err.println(e);
            server.close();
        }
    }
}


