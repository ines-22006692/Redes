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
     static ArrayList<String> onlineUsers = new ArrayList<String>();

    static BufferedReader leitura;
    static PrintStream printStream;

    public static boolean verificacaoLista(String ipAddress) {
        try {
            //lista Preta em primeiro uma vez que em prioridade
            Scanner listaPre = new Scanner(blackList);
            while (listaPre.hasNextLine()) {
                if (ipAddress.contains(listaPre.nextLine())) {
                    listaPre.close();
                    return false;
                }
            }
            listaPre.close();


            Scanner listaBra = new Scanner(whiteList);

            if (!listaBra.hasNextLine()) {
                listaBra.close();
                return true;
            }
            while (listaBra.hasNextLine()) {
                String linha = listaBra.nextLine();
                if (ipAddress.contains(linha)) {
                    listaBra.close();
                    return true;
                }
            }
            listaBra.close();
            return false;
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return false;
            }

    }

    public static void leituraListas()  throws FileNotFoundException {
        //Forma errada, tem que estar num ficheiro txt
        listaNegra= new HashMap<>();
        listaNegra.put("192.168.10.10","OFFLINE");
        listaNegra.put("192.168.10.11","OFFLINE");
        listaNegra.put("192.168.20.17","OFFLINE");


        listaNegra = new HashMap<>();
        listaNegra.put("192.168.10.07","OFFLINE");
        listaNegra.put("192.168.0.09","OFFLINE");
        listaNegra.put("127.0.0.1","OFFLINE");

        /*
        Scanner leitorFicheiroBranca = new Scanner(new FileReader("listaBranca.txt"));
        System.out.println("2");
        while (leitorFicheiroBranca.hasNext()) {
            String linha = leitorFicheiroBranca.next();
            String[] dados = linha.split(",");
            if (dados.length ==2) {
                String ip = dados[0].trim();
                System.out.println(ip);

                String estado = dados[1].trim();
                //listaBranca.put(ip,estado);
            }
        }
        Scanner leitorFicheiroPreta = new Scanner("listaPreta.txt");
        System.out.println("1");
        while (leitorFicheiroPreta.hasNextLine()) {
            String linha = leitorFicheiroPreta.nextLine();
            String[] dados = linha.split(",");
            if (dados.length==2) {
                System.out.println("aqui");
                String ip = dados[0].trim();
                String estado = dados[1].trim();
                System.out.println(estado);
                //listaNegra.put(ip, estado);
            }
        }
        //System.out.println(listaBranca);
        System.out.println("lista");
        //System.out.println(listaNegra);
 */
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
        int posicaoLista = 0;
        private InetAddress address;
        private byte[] buf = new byte[256];

        public void run() {
            int userName = 0;
            String clientIP = socket.getInetAddress().toString();
            System.out.println(clientIP);
            if (verificacaoLista(clientIP))
            {
                onlineUsers.add(clientIP);
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
                            //Apresenta o menu novamente
                            //System.out.println("0");
                            printStream.println(menu());
                            break;
                        case 1: {
                            printStream.println(onlineUsers);
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

                            for (String i : onlineUsers ) {
                                try {
                                    this.address = InetAddress.getByName(i);
                                    DatagramPacket packet = new DatagramPacket(mensagemBytes, mensagemBytes.length, address, 9031);
                                    com.send(packet);
                                } catch (IOException error) {
                                    error.printStackTrace();
                                }
                            }

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


