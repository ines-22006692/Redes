import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Scanner;

public class Servidor {

    static HashMap<String, String> listaNegra = new HashMap();
    static HashMap<String, String> listaBranca = new HashMap();
    static HashMap<String, String> listaOnline = new HashMap();
    static BufferedReader leitura;
    static PrintStream printStream;

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
        //envio

        public void run() {

            try {
                String userName = leitura.readLine();
                do {
                    switch (userName) {
                        case "0":
                            //Apresenta o menu novamente
                            System.out.println("0");
                            printStream.println(menu());
                            break;
                        case "1": {
                            System.out.println("Utilizadores onlines:\n");
                            System.out.println(listaOnline.toString());
                            break;
                        }
                        case "2": {
                            System.out.println("Qual é o IP que deseja enviar uma mensagem? ");
                            String mensagemIP = leitura.readLine();
                            StringBuilder answer = new StringBuilder();
                            System.out.println("Qual é a mensagem que pretende enviar? ");
                            String mensage = leitura.readLine();
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
                        case "3": {
                            System.out.println("Qual é a mensagem que pretende enviar? ");
                            String mens = leitura.readLine();
                            byte[] mensagemBytes = mens.getBytes();
                            com = new DatagramSocket();

                            for (String i : listaOnline.keySet()) {
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
                        case "4": {
                            System.out.println("Lista branca:\n");
                            System.out.println(listaBranca);
                            break;
                        }
                        case "5": {
                            System.out.println("Lista negra:\n");
                            System.out.println(listaNegra);
                            break;
                        }
                        case "99": {
                            System.out.println("A Sair\n");
                            System.out.println("Cliente Desconhectado..");
                            break;
                        }
                    }
                } while (!userName.equals("99"));
            } catch (IOException error) {
                error.printStackTrace();
            }
            com.close();
        }

    }

    public static void main(String args[]) throws Exception {
        listaNegra.put("192.168.10.10", "Offline");
        listaNegra.put("192.168.10.15", "Offline");
        listaNegra.put("192.168.10.110", "Offline");

        listaBranca.put("192.168.10.12", "Offline");
        listaBranca.put("192.168.10.22", "Offline");
        listaBranca.put("192.168.10.110", "Offline");
        listaBranca.put("192.168.10.74", "Offline");

        listaOnline.put("192.168.10.191","Online");
        ServerSocket server = new ServerSocket(7142);
        Socket socket = null;
        while (true){
            socket = server.accept();
            PrintStream stream = new PrintStream(socket.getOutputStream());
            stream.println(" ");
            Thread thread = new Thread(new Server_Manager(socket));
            thread.start();
        }




        // leituraListas();
        //Criar lista online, e a medida que as pessoas vao entrando, ele vao conectares, tem que ler de um ficheiro e selecionar o que sao da lista negra e da lista branca
        /*
/*
        ServerSocket server = new ServerSocket(6500);
        Socket socket= null;
        InetAddress IP = InetAddress.getLocalHost();
        while(true)  {
            socket = server.accept();
            for (String ipLista : listaBranca.keySet()){
                if (ipLista.equals(IP)){

                }
            }
        }*/






    }
}


