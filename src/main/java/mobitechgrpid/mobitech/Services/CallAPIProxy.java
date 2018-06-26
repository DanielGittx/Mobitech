/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobitechgrpid.mobitech.Services;

/**
 *
 * @author danial
 */
import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class CallAPIProxy {
    Timer timer;

    public CallAPIProxy(int seconds) {
        timer = new Timer();
        // timer.schedule(new APICallerTask(), seconds * 1000);
        timer.scheduleAtFixedRate(new APICallerTask(), 30000, 30000);        //fetch every 30 minutes

    }

    class APICallerTask extends TimerTask {
        public void run() {
            String httpsURL = "http://159.65.197.113:8080/sms";
            // String httpsURL = "https://myrestserver/path" + id;
            // Proxy proxy = new Proxy(Proxy.Type.HTTP, new
            // InetSocketAddress("myproxy", 8080));
            URL myurl;
            try {
                myurl = new URL(httpsURL);
                System.setProperty("http.agent", "Chrome");

                // HttpsURLConnection con = (HttpsURLConnection)
                // myurl.openConnection(proxy);
                HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
                con.setRequestMethod("GET");
                // con.setRequestProperty("Content-Type", "application/json");
                // con.setRequestProperty("Authorization", authString);
                con.setDoInput(true);
                DataInputStream input = new DataInputStream(con.getInputStream());
                // String result = new
                // Scanner(input).useDelimiter("\\Z").next();
                // Scanner result = new Scanner(input);
                StringBuffer inputLine = new StringBuffer();
                String tmp;
                while ((tmp = input.readLine()) != null) {
                    inputLine.append(tmp);
                    System.out.println(tmp);
                }
                input.close();
                System.out.println("Result " + inputLine);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}