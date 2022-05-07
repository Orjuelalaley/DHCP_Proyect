public class Ip {
    private String dir;
    private Boolean busy;

    //METODOS

    public Ip(String ip, boolean busy){
        this.dir= ip;
        this.busy= busy;
    }

    public String getdir() {
        return dir;
    }

    public Boolean getBusy() {
        return busy;
    }

    public void setBusy(Boolean available) {this.busy = available;}

    public void cambiar ( ){
        this.busy = !busy;
    }
}
