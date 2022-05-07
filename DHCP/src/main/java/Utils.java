
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class Utils {

    public static byte[] byte2Bytes(byte val) {
        byte[] raw = { val };
        return raw;
    }

    //MAC DE BINARIO A STRING
    public static String getHWadd(byte[] hAdd) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < 6; i++)
            string.append(String.format("%02X%s", hAdd[i], (i < 6 - 1) ? ":" : ""));
        return (string.toString());
    }

    //CAMBIAR IP
    public static void cambiar(String direccion, List<Ip> ips) {
        for (Ip aux : ips) {
            if (aux.getdir().equalsIgnoreCase(direccion))
                aux.cambiar();
        }
    }

    public static byte[] StringAddress2Bytes(String val) {
        try {
            return InetAddress.getByName(val).getAddress();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static int bytesToInt(byte[] b) {
        return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
    }

    public static short bytesToShort(byte[]b) {
        return (short)((b[1] & 0xFF)  | ((b[0] & 0xFF))<< 8);
    }

    public static final byte[] intToBytes(int x) {
        return new byte[] { (byte) ((x >> 24) & 0xFF), (byte) ((x >> 16)& 0xFF), (byte) ((x >> 8)& 0xFF), (byte) (x& 0xFF) };
    }

    public static byte[] shortToBytes(short x) {
        return new byte[] {(byte) ((x >> 8) & 0xff), (byte) (x & 0xff)};
    }

    public static String getBeginingRed(String red) {
        int cont=0, pos=0;
        for(int i=0; i<red.length(); ++i){
            if(red.charAt(i)=='.')
                cont++;
            if(cont==3)
                pos=i;
        }
        return red.substring(0, pos-1);
    }
}

//#ENDOF