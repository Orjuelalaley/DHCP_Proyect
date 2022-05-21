import java.io.IOException;

public class Proyecto{

    public static void main(String[] args) throws IOException {

        //Se elimina el archivo log anterior para iniciar la ejecucion del servidor
        Files.deleteFile();

        //Se pide memoria para crear el servidor
        Server server = new Server();

        System.out.println("Servidor Configurado con:\nIP: " +
                server.getServer().getIP() + "\nGateway: " + server.getServer().getGateway() +
                "\nMascara : " + server.getServer().getMascara() + "\nDNS: " + server.getServer().getDns()+
                "\nTiempo de arrendamiento: " + server.getServer().getTime().toSecondOfDay());



        String log = "Servidor arriba. Hora: " + Time.getHora();
        Files.write(log);
        System.out.println(log);

        //FUNCIONAMIENTO CONSTANTE
        while (true) {
            DHCP request = server.readPacket();
            server.sendResponse(request);
        }
    }
}
