import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;


public class DHCPMessageCreator {

    //OFFER
    public static DHCP createOffer(DHCP peticion, String ipOfrecida, Option[] options) throws UnknownHostException {
        DHCP offer = new DHCP();

        offer.setOp(Constants.BOOTREPLY);
        offer.setHtype(peticion.getHtype());
        offer.setHlen(peticion.getHlen());
        offer.setHops((byte) 0);
        offer.setXid(peticion.getXid());
        offer.setSecs(Utils.shortToBytes((short) 0));
        offer.setFlags(peticion.getFlags());
        offer.setCiaddr(InetAddress.getByName("0.0.0.0").getAddress());
        offer.setYiaddr(InetAddress.getByName(ipOfrecida).getAddress());
        offer.setGiaddr(peticion.getGiaddr().getAddress());
        offer.setChaddr(peticion.getChaddr().getAddress());

        // opciones
        if (options != null) {
            for (Option opt : options) {
                offer.setOption(opt);
            }
        }

        // set address and port
        offer.setAddressAndPort(getDefaultSocketAddress(peticion, Constants.DHCPOFFER));

        return offer;
    }

    //ACK
    public static DHCP createACK(DHCP peticion, String ipOfrecida, Option[] options) throws UnknownHostException {
        DHCP ack = new DHCP();

        ack.setOp(Constants.BOOTREPLY);
        ack.setHtype(peticion.getHtype());
        ack.setHlen(peticion.getHlen());
        ack.setHops((byte) 0);
        ack.setXid(peticion.getXid());
        ack.setSecs(Utils.shortToBytes((short) 0));
        ack.setFlags(peticion.getFlags());
        ack.setCiaddr(peticion.getCiaddr().getAddress());
        ack.setYiaddr(InetAddress.getByName(ipOfrecida).getAddress());
        ack.setGiaddr(peticion.getGiaddr().getAddress());
        ack.setChaddr(peticion.getChaddr().getAddress());

        // opciones
        if (options != null) {
            for (Option opt : options) {
                ack.setOption(opt);
            }
        }

        // set address and port
        ack.setAddressAndPort(getDefaultSocketAddress(peticion, Constants.DHCPACK));

        return ack;
    }

    //NACK
    public static DHCP createNAK(DHCP peticion, String ipOfrecida, String ipServer) throws UnknownHostException {
        DHCP nack = new DHCP();

        nack.setOp(Constants.BOOTREPLY);
        nack.setHtype(peticion.getHtype());
        nack.setHlen(peticion.getHlen());
        nack.setHops((byte) 0);
        nack.setXid(peticion.getXid());
        nack.setSecs(Utils.shortToBytes((short) 0));
        nack.setFlags(peticion.getFlags());
        nack.setCiaddr(InetAddress.getByName("0.0.0.0").getAddress());
        nack.setYiaddr(InetAddress.getByName("0.0.0.0").getAddress());
        nack.setGiaddr(peticion.getGiaddr().getAddress());
        nack.setChaddr(peticion.getChaddr().getAddress());

        // opciones
        nack.setDHCPMessageType(Constants.DHCPNAK);
        nack.setOption(new Option(Constants.DHCP_SERVER_ID, Utils.StringAddress2Bytes(ipServer)));

        // set address and port
        nack.setAddressAndPort(getDefaultSocketAddress(peticion, Constants.DHCPNAK));

        return nack;
    }

    // ADDRESS DHCP PACKET
    public static InetSocketAddress getDefaultSocketAddress(DHCP peticion, byte type) {
        InetAddress giaddr = peticion.getGiaddr();
        InetAddress ciaddr = peticion.getCiaddr();

        InetAddress address0 = null, broadcast = null;
        try {
            address0 = InetAddress.getByName("0.0.0.0");
            broadcast = InetAddress.getByName("255.255.255.255"); //DHCP - CAPA 3
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }//END TRY-CATCH
        if (type == Constants.DHCPOFFER || type == Constants.DHCPACK) {
            if (giaddr.equals(address0)) {
                if (ciaddr.equals(address0)) {
                    return new InetSocketAddress(broadcast, Constants.BOOTP_REPLY_PORT);
                } else {
                    return new InetSocketAddress(ciaddr, Constants.BOOTP_REPLY_PORT);
                }
            } else {// unicast
                return new InetSocketAddress(giaddr, Constants.BOOTP_REQUEST_PORT);
            }
        } else if (type == Constants.DHCPNAK) {
            if (giaddr.equals(address0)) {
                return new InetSocketAddress(broadcast, Constants.BOOTP_REPLY_PORT);
            } else { // unicast
                return new InetSocketAddress(giaddr, Constants.BOOTP_REQUEST_PORT);
            }
        } else {
            throw new IllegalArgumentException("tipo no valido");
        }
    }
}