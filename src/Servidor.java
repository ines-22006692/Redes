import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Servidor {

    static HashMap<String, String> listaNegra = new HashMap();
    static HashMap<String, String> listaBranca = new HashMap();
    static HashMap<String, String> listaOnline = new HashMap();

    public static void leituraListas()  throws FileNotFoundException {

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
    }

    public static void main(String args[]) throws Exception {


        leituraListas();
        //Criar lista online, e a medida que as pessoas vao entrando, ele vao conectares, tem que ler de um ficheiro e selecionar o que sao da lista negra e da lista branca
        /*
        listaNegra.put("192.168.10.10", "Offline");
        listaNegra.put("192.168.10.15", "Offline");
        listaNegra.put("192.168.10.110", "Offline");

        listaBranca.put("192.168.10.12", "Online");
        listaBranca.put("192.168.10.22", "Online");
        listaBranca.put("192.168.10.110", "Online");
        listaBranca.put("192.168.10.74", "Online");
        */


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

    /* Notas:
    - Lista Negra tem prioridade

    */
    private static final String menu() {
        return "MENU CLIENTE\n" +
                "0 - Menu Inicial\n" +
                "1 - Listar utilizadores online\n" +
                "2 - Enviar mensagem a um utilizador\n" +
                "3 - Enviar mensagem a todos os utilizadores\n" +
                "4 - lista branca de utilizadores\n" +
                "5 - lista negra de utilizadores\n" +
                "99 – Sair\n";
    }

    class ClienteServidor implements Runnable {

        public void run() {
            menu();
            Scanner opcaoMenu = new Scanner(System.in);
            int userName = opcaoMenu.nextInt();

            do {
                switch (userName) {
                    case 0:
                        //Apresenta o menu novamente
                        menu();
                        break;
                    case 1:
                        System.out.println("Utilizadores onlines:\n");
                        break;
                    case 2:
                        StringBuilder answer = new StringBuilder();

                        break;
                    case 3:
                        break;
                    case 4:
                        System.out.println("Lista branca:\n");
                        System.out.println(listaBranca);
                        break;
                    case 5:
                        System.out.println("Lista negra:\n");
                        System.out.println(listaNegra);
                        break;
                    case 99:
                        System.out.println("A Sair\n");
                        System.out.println("Cliente Desconhectado..");
                        break;
                }
            } while (userName != 99);
        }
    }
}


