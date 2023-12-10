package controllers;
// Importar las librerías necesarias
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

// Definir la clase del agente BackendInteligente
public class BackendInteligente extends Agent {

    private EstadoDelJuego estado;

    // Método de inicialización del agente
    protected void setup() {
        estado = new EstadoDelJuego();

        // Crear un servidor web para recibir notificaciones HTTP con el estado del juego
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLocalHost(), 8000), 0);
            server.createContext("/estado", new EstadoHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException ex) {
            Logger.getLogger(BackendInteligente.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Crear un comportamiento para recibir el mejor movimiento del agente Minimax y enviarlo al backend de voz artificial
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                if (msg != null) {
                    // Obtener el mejor movimiento del mensaje recibido
                    String mejorMovimiento = msg.getContent();
                    // Enviar el mejor movimiento al backend de voz artificial
                    notificarMovimientoVozArtificial(mejorMovimiento);
                } else {
                    block();
                }
            }
        });
    }

    // Clase para manejar las solicitudes HTTP con el estado del juego
    class EstadoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "";
            if ("GET".equals(t.getRequestMethod())) {
                // Actualizar el estado del juego
                estado.actualizar(t.getRequestURI().getQuery());

                // Notificar el estado del juego al agente Minimax
                notificarEstadoJuego(new AID("AgenteMinimax", AID.ISLOCALNAME), estado.toString());

                // Preparar la respuesta HTTP con el estado del juego actualizado
                response = estado.toString();
            }
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // Método para notificar al agente Minimax el estado del juego
    private void notificarEstadoJuego(AID agenteMinimax, String estadoJuego) {
        ACLMessage notificacion = new ACLMessage(ACLMessage.INFORM);
        notificacion.addReceiver(agenteMinimax);
        notificacion.setContent(estadoJuego);
        send(notificacion);
    }

    // Método para notificar al backend de voz artificial el mejor movimiento
    private void notificarMovimientoVozArtificial(String mejorMovimiento) {
        ACLMessage notificacion = new ACLMessage(ACLMessage.INFORM);
        notificacion.addReceiver(new AID("BackendVozArtificial", AID.ISLOCALNAME));
        notificacion.setContent(mejorMovimiento);
        send(notificacion);
    }
}