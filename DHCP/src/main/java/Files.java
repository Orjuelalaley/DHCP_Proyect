import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Files {
    //BORRAR ARCHIVO PREVIO
    public static void deleteFile() {
        File file = new File("Log.txt");
        file.delete();
    }

    //LEER DATOS network PRINCIPAL DEL .TXT
    public static network leerConfiguracionred() {
        File doc = new File("E:\\codes\\Comunicación y Redes\\DHCP\\DHCP\\src\\CONFIG.txt");
        BufferedReader br = null;
        network subNet = new network();
        int l, h;
        try {
            br = new BufferedReader(new FileReader(doc));
            String linea = br.readLine();
            while (!linea.equalsIgnoreCase("SUBRED")) {
                if (linea.equalsIgnoreCase("IP")) {
                    linea = br.readLine();
                    subNet.setIP(linea);
                    linea = br.readLine();
                } else if (linea.equalsIgnoreCase("GATEWAY")) {
                    linea = br.readLine();
                    subNet.setGateway(linea.replace(" ", ""));
                    linea = br.readLine();
                } else if (linea.equalsIgnoreCase("MASK")) {
                    linea = br.readLine();
                    subNet.setMascara(linea);
                    linea = br.readLine();
                } else if (linea.equalsIgnoreCase("RANGO")) {
                    linea = br.readLine();
                    l = Integer.parseInt(linea);
                    linea = br.readLine();
                    h = Integer.parseInt(linea);
                    for (int i = l; i <= h; i++) {
                        subNet.addIPToList(new Ip(Utils.getBeginingRed(subNet.getIP())+ String.valueOf(i), false));
                    }
                    linea = br.readLine();

                } else if (linea.equalsIgnoreCase("EXCLUIDAS")) {
                    linea = br.readLine();
                    while (!linea.equalsIgnoreCase("TIME")) {
                        for (Ip d : subNet.getListIP()) {
                            if (d.getdir().equalsIgnoreCase(linea))
                                d.setBusy(true);
                        }
                        linea = br.readLine();
                    }
                } else if (linea.equalsIgnoreCase("TIME")) {
                    linea = br.readLine();
                    subNet.setTime(LocalTime.ofSecondOfDay(Long.parseLong(linea)));
                    linea = br.readLine();
                } else if (linea.equalsIgnoreCase("DNS")) {
                    linea = br.readLine();
                    subNet.setDns(linea);
                    linea = br.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//END TRY-CATCH
        return subNet;
    }

    //LEER DATOS SUBredes DEL .TXT
    public static List<network> leerConfiguracionSub() {
        BufferedReader br = null;
        FileReader fr = null;
        List<network> subn = new ArrayList<>();
        network subNet = new network();
        int l, h;
        boolean band = false;
        try {
            fr = new FileReader("E:\\codes\\Comunicación y Redes\\DHCP\\DHCP\\src\\CONFIG.txt");
            br = new BufferedReader(fr);
            String linea = br.readLine();
            while (!linea.equalsIgnoreCase("FIN")) {
                if (!band) {
                    linea = br.readLine();
                }
                if (linea.equalsIgnoreCase("SUBRED")) {
                    band = true;
                }
                if (band) {
                    linea = br.readLine();
                    if (linea.equalsIgnoreCase("IP")) {
                        subNet.setIP(br.readLine());
                        linea = br.readLine();
                    }
                    if (linea.equalsIgnoreCase("GATEWAY")) {
                        subNet.setGateway(br.readLine().replace(" ", ""));
                        linea = br.readLine();
                    }
                    if (linea.equalsIgnoreCase("MASK")) {
                        subNet.setMascara(br.readLine());
                        linea = br.readLine();
                    }
                    if (linea.equalsIgnoreCase("RANGO")) {
                        l = Integer.parseInt(br.readLine());
                        h = Integer.parseInt(br.readLine());
                        for (int i = l; i <= h; i++) {
                            subNet.addIPToList(new Ip(Utils.getBeginingRed(subNet.getIP()) + String.valueOf(i), false));
                        }
                        linea = br.readLine();
                    }//ENDIF
                    if (linea.equalsIgnoreCase("EXCLUIDAS")) {
                        linea = br.readLine();
                        while (!linea.equalsIgnoreCase("TIME")) {
                            for (Ip d : subNet.getListIP()) {
                                if (d.getdir().equalsIgnoreCase(linea))
                                    d.setBusy(true);
                            }
                            linea = br.readLine();
                        }
                    }
                    if (linea.equalsIgnoreCase("TIME")) {
                        subNet.setTime(LocalTime.ofSecondOfDay(Long.parseLong(br.readLine())));
                        linea = br.readLine();
                    }
                    if (linea.equalsIgnoreCase("DNS")) {
                        subNet.setDns(br.readLine());
                        linea = br.readLine();
                    }
                    subn.add(subNet);
                } //ENDIF
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } //END TRY-CATCH
        }
        return subn;
    }

    //ESCRIBIR HISTORIAL
    public static void write(String log) throws IOException {
        FileWriter escribir = new FileWriter("Log.txt", true);
        PrintWriter evento = new PrintWriter(escribir);
        evento.println(log);
        evento.close();
    }
}
