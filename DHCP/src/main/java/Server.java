
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.*;


public class Server {

    //Atribute
    private final Map<String, String> ipsAsignadas; // <ip, mac>
    private final network server;
    private final List<network> subredes;
    private static DatagramSocket server_socket;

    //methods

    public Server() throws SocketException {
        ipsAsignadas = new LinkedHashMap<>();
        server = Files.leerConfiguracionred();
        subredes = Files.leerConfiguracionSub();
        server_socket = new DatagramSocket(67);
        server_socket.setBroadcast(true);
    }

    //LEER PAQUETE
    public DHCP readPacket() throws IOException {
        byte[] receive_data = new byte[1024];
        DatagramPacket receive_packet = new DatagramPacket(receive_data, receive_data.length);
        server_socket.receive(receive_packet);
        return DHCP.getPacket(receive_packet);
    }

    //ENVIAR RESPUESTAS
    public void sendResponse(DHCP request) throws IOException {
        byte tipo = request.getDHCPMessageType();
        if (tipo == Constants.DHCPDISCOVER)
            sendOffer(request);
        else if (tipo == Constants.DHCPREQUEST)
            respondRequest(request);
        else if (tipo == Constants.DHCPRELEASE)
            releaseIp(request);
    }

    //GENERAR OFFER
    public void sendOffer(DHCP peticion) throws IOException {
        String gateway, ipAsignar, mac, log;
        DHCP offer;
        mac = Utils.getHWadd(peticion.getChaddr().getAddress());
        log = ("Discover recibido MAC: " + mac + " Hora: " + Time.getHora());
        network network;
        Files.write(log);
        System.out.println(log);
        gateway = peticion.getGiaddr().getHostAddress();
        network = getnetworkPeticion(gateway);
        ipAsignar = buscarIpDisponible(network.getListIP());
        if (ipAsignar != null) {
            offer = DHCPMessageCreator.createOffer(peticion, ipAsignar, generateOptions(network.getTime(),
                    network.getMascara(), server.getIP(), network.getGateway(), network.getDns(), Constants.DHCPOFFER));
            sendMsg(offer);
            log = ("Se envia offer a MAC: " + mac + " Hora: " + Time.getHora() + " con la ip: " + ipAsignar + " tiempo: "
                    + network.getTime());
            Files.write(log);
            System.out.println(log);
        } else {
            log = "No hay IPs disponibles";
            Files.write(log);
                System.out.println(log);
        }
    }

    // RESPUESTA A LA PETICION
    public void respondRequest(DHCP peticion) throws IOException {
        String gateway, ipAsignar, mac, log;
        DHCP rta;
        network network;
        boolean f=true;
        mac = Utils.getHWadd(peticion.getChaddr().getAddress());
        log = ("Request recibido MAC: " + mac + " Hora: " + Time.getHora());
        Files.write(log);
        System.out.println(log);
        gateway = peticion.getGiaddr().getHostAddress();
        network = getnetworkPeticion(gateway);
        if (peticion.getOptionValue(Constants.DHCP_REQUESTED_ADDRESS) == null) {
            ipAsignar = peticion.getCiaddr().getHostAddress();
            if(!ipsAsignadas.get(ipAsignar).equals(mac))
                f=false;
        }
        else {
            ipAsignar = InetAddress.getByAddress(peticion.getOptionValue(Constants.DHCP_REQUESTED_ADDRESS))
                    .getHostAddress();
            if(!ipDisponible(ipAsignar, network.getListIP()))
                f=false;
        }
        if(f) { //ACK
            rta = DHCPMessageCreator.createACK(peticion, ipAsignar, generateOptions(network.getTime(), network.getMascara(),
                    server.getIP(), network.getGateway(), network.getDns(), Constants.DHCPACK));
            log = ("Se envia ack a: " + mac + " Hora: " + Time.getHora()+" con ip: "+ipAsignar);
            if (!ipsAsignadas.containsKey(ipAsignar)) {
                ipsAsignadas.put(ipAsignar, mac);
            }
            Files.write(log);
            System.out.println(log);
            sendMsg(rta);
            cambiar(ipAsignar, network.getListIP());
            Timer t = new Timer();
            t.schedule(new Clock(ipAsignar, network.getListIP()), network.getTime().toSecondOfDay()* 1000L);

        } else { //NACK
            log = ("No hay IPs disponibles");
            System.out.println(log);
            rta = DHCPMessageCreator.createNAK(peticion, ipAsignar, server.getIP());
            log = ("Se envia nack a: " + mac + " Hora: " + Time.getHora());
            Files.write(log);
            System.out.println(log);
            sendMsg(rta);
        }
    }

    //PETICION LIBERAR IP
    public void releaseIp(DHCP peticion) throws IOException {
        String gateway, ipAsignar, mac, log;
        network network;
        mac = Utils.getHWadd(peticion.getChaddr().getAddress());
        log = "Release recibido MAC: " + mac + " Hora: " + Time.getHora();
        Files.write(log);
        System.out.println(log);
        ipAsignar = peticion.getCiaddr().getHostAddress();
        gateway = peticion.getGiaddr().getHostAddress();
        network = getnetworkPeticion(gateway);
        cambiar(ipAsignar, network.getListIP());
        log = "Se libera ip por peticion: " + ipAsignar + " Hora: " + Time.getHora();
        Files.write(log);
        System.out.println(log);
    }

    //ENVIO MSG
    public void sendMsg(DHCP packet) throws IOException {
        byte[] send_data = packet.serialize();
        DatagramPacket send_packet = new DatagramPacket(send_data, send_data.length, packet.getAddress(), packet.getPort());
        server_socket.send(send_packet);
    }

    //SELECCIONAR network CORRESPONDIENTE
    public network getnetworkPeticion(String gateway) {
        if (gateway.equalsIgnoreCase("0.0.0.0")) // network del servidor
        {
            return server;
        }
        else { // otra network
            for (network r : subredes) {
                if (r.getGateway().equalsIgnoreCase(gateway)) {
                    return r;
                }
            }
        }
        return null;
    }

    //ADDRESS IP LIBRE
    public String buscarIpDisponible( List<Ip> ips) {
        for (Ip ip : ips) {
            if (!ip.getBusy())
                return ip.getdir();
        }
        return null;
    }


    //GENERAR OPCIONES
    public Option[] generateOptions(LocalTime tiempo, String mascara, String ip, String gateway, String dns, int tipo)
            throws UnknownHostException {
        Option[] opciones = new Option[6];
        opciones[0] = new Option(Constants.DHCP_MSG_TYPE, Utils.byte2Bytes((byte) tipo));
        opciones[1] = new Option(Constants.SUBNET_MASK, InetAddress.getByName(mascara).getAddress());
        opciones[2] = new Option(Constants.ROUTERS, InetAddress.getByName(gateway).getAddress());
        opciones[3] = new Option(Constants.DHCP_SERVER_ID, InetAddress.getByName(ip).getAddress());
        opciones[4] = new Option(Constants.DOMAIN_NAME_SERVERS, InetAddress.getByName(dns).getAddress());
        opciones[5] = new Option(Constants.DHCP_LEASE_TIME, Utils.intToBytes(tiempo.toSecondOfDay()));
        return opciones;
    }

    //HAY IP LIBRE
    public boolean ipDisponible(String ip, List<Ip> ips) {
        for (Ip aux : ips) {
            if (aux.getdir().equalsIgnoreCase(ip) && !aux.getBusy())
                return true;
        }
        return false;
    }

    //CAMBIAR IP LISTA
    public static void cambiar(String ip, List<Ip> ips) {
        for (Ip aux : ips) {
            if (aux.getdir().equalsIgnoreCase(ip)) {
                aux.cambiar();
            }
        }
    }
    public network getServer() {
        return server;
    }
}
