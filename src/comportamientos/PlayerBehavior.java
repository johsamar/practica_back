/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comportamientos;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author johsamar
 */
public class PlayerBehavior extends SimpleBehaviour {

    Agent miAgente;

    public PlayerBehavior(Agent miAgente) {
        System.out.println("Comportamiento player");
        this.miAgente = miAgente;
    }

    @Override
    public void action() {
        ACLMessage mensajeRecibido = this.miAgente.blockingReceive();
        System.out.println("Action player");
//        ACLMessage msg = miAgente.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
//        if (msg != null) {
        // Obtener el mejor movimiento del mensaje recibido
//            String mejorMovimiento = msg.getContent();
        // Enviar el mejor movimiento al backend de voz artificial
        notificarMovimientoVozArtificial("Mi movimiento es en la columna X");
//        } else {
//            block();
//        }
    }

    // Método para notificar al backend de voz artificial el mejor movimiento
    public void notificarMovimientoVozArtificial(String mejorMovimiento) {

        try {
            HttpClientBuilder builder = HttpClientBuilder.create();

            HttpClient httpClient = builder.build();

            HttpPost request = new HttpPost("http://127.0.0.1:5000/say-movement");
            StringEntity input = new StringEntity(mejorMovimiento, "UTF-8");
            input.setContentType("application/json; charset=UTF-8");
            request.setEntity(input);

            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Error : Codigo de error HTTP : " + response.getStatusLine().getStatusCode());
            }

        } catch (Throwable e) {
            System.out.println("Falló");
        }
    }

    @Override
    public boolean done() {
        System.out.println("ya terminé de enviar el mensaje");
        return true;
    }

}
