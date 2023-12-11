package testjade;

import com.sun.net.httpserver.HttpServer;
import controllers.TrikiController;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 *
 * @author Asus
 */
public class TestJade {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int port = 8081; // Puerto en el que se ejecutará el servidor
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new TrikiController());
        server.setExecutor(null); // Usar el valor predeterminado
        System.out.println("Iniciando servidor en el puerto " + port);
        server.start();
    }
}
