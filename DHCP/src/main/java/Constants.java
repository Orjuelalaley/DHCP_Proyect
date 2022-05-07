public class Constants {
    public static final byte BOOTREQUEST = 1;
    public static final byte BOOTREPLY = 2;

    // HW types codes
    public static final byte HTYPE_ETHER = 1;

    // Message codes
    public static final byte DHCPDISCOVER = 1;
    public static final byte DHCPOFFER = 2;
    public static final byte DHCPREQUEST = 3;
    public static final byte DHCPDECLINE = 4;
    public static final byte DHCPACK = 5;
    public static final byte DHCPNAK = 6;
    public static final byte DHCPRELEASE = 7;
    public static final byte DHCPINFORM = 8;
    public static final byte DHCPFORCERENEW = 9;

    // Options codes
    public static final byte PAD = 0;
    public static final byte SUBNET_MASK = 1;
    public static final byte ROUTERS = 3;
    public static final byte DOMAIN_NAME_SERVERS = 6;
    public static final byte DHCP_REQUESTED_ADDRESS = 50;
    public static final byte DHCP_LEASE_TIME = 51;
    public static final byte DHCP_MSG_TYPE = 53;
    public static final byte DHCP_SERVER_ID = 54;
    public static final byte DHCP_MESSAGE = 56;
    public static final byte END = -1;

    // Valores de chequeo
    static final int DHCP_MIN_LEN = 548;
    static final int DHCP_DEFAULT_MAX_LEN = 576;
    static final int BOOTP_ABSOLUTE_MIN_LEN = 236;
    static final int DHCP_MAX_MTU = 1500;
    static final int DHCP_UDP_OVERHEAD = 42;
    static final int BOOTP_VEND_SIZE = 64;

    // Magic cookie
    static final int MAGIC_COOKIE = 0x63825363;

    // Puertos
    public static final int BOOTP_REQUEST_PORT = 67;
    public static final int BOOTP_REPLY_PORT   = 68;
}
