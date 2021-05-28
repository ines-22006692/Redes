import java.net.*;
import java.io.*;
import java.util.Scanner;


public class Cliente extends Thread {
    private ServerSocket serverSocket;

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
    public void run(){
        try{
            menu();
            Scanner opcaoMenu = new Scanner(System.in);
            int userName = opcaoMenu.nextInt();

            do {
                switch (userName){
                    case 0:
                        //Apresenta o menu novamente
                        menu();
                        break;
                    case 1:
                        System.out.println("Utilizadores onlines:\n");
                        break;
                    case 2:
                        StringBuilder answer = new StringBuilder();
                        //for (host: Servidor)
                        break;
                    case 3:
                        break;
                    case 4:
                        System.out.println("Lista branca:\n");
                        System.out.println(Servidor.listaBranca);
                        break;
                    case 5:
                        System.out.println("Lista negra:\n");
                        //System.out.println(listaNegra);
                        break;
                    case 99:
                        System.out.println("A Sair\n");
                        System.out.println("Cliente Desconhectado..");
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
    public static void main(String args[]) throws Exception {
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
    }

}
