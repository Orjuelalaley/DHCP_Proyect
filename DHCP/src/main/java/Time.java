import java.time.Duration;
import java.time.LocalTime;

public class Time {
    //HORA ACTUAL
    public static LocalTime getHora() {
        return LocalTime.now();
    }

    //CALCULAR DURACION
    public static long difference(LocalTime time) {
        LocalTime now = LocalTime.now();
        Duration duration = Duration.between(now, time);
        System.out.println("Duration: "+duration.getSeconds());
        return Math.abs(duration.getSeconds());
    }
}

//#ENDOF