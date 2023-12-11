package controllers;

import modelos.EstadoDelJuego;
import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

// Definir la clase del agente BackendInteligente
public class TrikiController implements HttpHandler {

    private EstadoDelJuego estado = new EstadoDelJuego();

    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = "";
        if ("GET".equals(t.getRequestMethod())) {
            System.out.println("Dentro del GET");
            jade.core.Runtime rt = jade.core.Runtime.instance();

            // Permite configurar los perfiles según tus necesidades
            Profile profile = new ProfileImpl();

            // Crea un contenedor
            ContainerController container = rt.createMainContainer(profile);
            EstadoDelJuego estadoDelJuego = new EstadoDelJuego();

            try {
                System.out.println("Creanado el agente");
                // Crea un agente
                jade.wrapper.AgentController agentMiniMax = container.createNewAgent("MinimaxAgent", "agents.MinimaxAgent", new Object[]{estadoDelJuego});
                jade.wrapper.AgentController agentJugador = container.createNewAgent("Jugador", "agents.Jugador", null);
                // Inicializa el agente
                agentMiniMax.start();
                agentJugador.start();
            } catch (StaleProxyException e) {
                System.out.println("Error handle: " + e.getMessage());
            }
        }
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
