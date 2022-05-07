import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

public class Clock extends TimerTask{
    private String ip;
    private List<Ip>ips;
    public Clock (String ip, List<Ip>ips) {
        this.ip=ip;
        this.ips=ips;
    }
    @Override
    public void run() {
        Server.cambiar(ip, ips);
        String log = "Se libera ip por vencimiento de tiempo: "+ ip + " Hora: " + Time.getHora();
        try {
            Files.write(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.cancel();
    }
}
