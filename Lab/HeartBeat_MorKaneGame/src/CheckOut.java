import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Timer;


public class CheckOut {

	public static void main(String[] args) throws InterruptedException, IOException  {
		
		System.out.println("\n#################################################################");
		System.out.println("#");
		System.out.println("#\t\t ~\t MorKaneGame HeartBeat\t~\t\t");
		System.out.println("#");
		System.out.println("#################################################################");
		
		getStatus("http://127.0.0.1:8080/SR_Game");
	}

	/**
	 * Check if the MorKaneGame Server is being working, if it's not the case, he launch a bat file 
	 * @param url : path of bat file
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void getStatus(String url) throws IOException, InterruptedException { 
		while (true) {
			try {
				while(true) {
					URL siteURL = new URL(url);
					HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
					connection.setRequestMethod("GET");
					connection.connect();
					int code = connection.getResponseCode();
					if (code == 200) {
						System.out.println("OK");
					}
					Thread.sleep(2000);
				}
			} catch (Exception e) {
				Runtime r = Runtime.getRuntime();
				//String command = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.0\\bin\\startup.bat";
				String current = new java.io.File( "." ).getCanonicalPath();
				System.out.println("/!\\ Server is Dead ! You will live ! ");
				String command = current+"/launchServer.bat";
				Process p = r.exec(command);
				p.waitFor();
			}
			Thread.sleep(15000);
		}

	}
}
