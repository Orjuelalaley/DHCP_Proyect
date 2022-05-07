import java.time.LocalTime;
import java.util.*;

public class network {
    private String IP, gateway, mascara, dns;
    LocalTime time;
    private List<Ip> ListIP = new ArrayList<Ip>();

    //METHODS

    public void setIP(String IP){
        this.IP = IP;
    }

    public void setGateway(String gateway){
        this.gateway = gateway;
    }

    public void setMascara(String mascara){
        this.mascara = mascara;
    }

    public void setDns(String dns){
        this.dns = dns;
    }

    public LocalTime getTime() {return time;}

    public void setTime(LocalTime time) {this.time = time;}

    public void setListIP(List<Ip> IPbusy){
        this.ListIP = IPbusy;
    }

    public String getIP() {
        return IP;
    }

    public String getGateway() {
        return gateway;
    }

    public String getMascara() {
        return mascara;
    }

    public String getDns() {
        return dns;
    }

    public List<Ip> getListIP() {
        return ListIP;
    }

    public void addIPToList(Ip IP) {
        this.ListIP.add(IP);
    }
}
