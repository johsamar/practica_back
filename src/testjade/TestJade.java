package testjade;


import com.sun.net.httpserver.HttpServer;
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
        // Inicia el servidor HTTP
        int port = 8082; // Puerto en el que se ejecutará el servidor
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(null); // Usar el valor predeterminado


        System.out.println("Iniciando servidor en el puerto " + port);
        server.start();


        // Obtiene la instancia de tiempo de ejecución de JADE
        Runtime rt = Runtime.instance();


        // Crea un perfil por defecto
        Profile p = new ProfileImpl();


        // Inicia la plataforma principal con el perfil por defecto
        ContainerController mainContainer = rt.createMainContainer(p);


        try {
            // Inicia los agentes
            AgentController ac1 = mainContainer.createNewAgent("BackendInteligente", "controllers.BackendInteligente", null);
            ac1.start();
            AgentController ac2 = mainContainer.createNewAgent("Minimax", "agents.MinimaxAgent", null);
            ac2.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
