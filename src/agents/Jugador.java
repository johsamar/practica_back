package agents;

import comportamientos.PlayerBehavior;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author johsamar
 */
public class Jugador extends Agent {
    // Método de inicialización del agente

    protected void setup() {
        System.out.println("Creacion de jugador agente");
        // Crear un comportamiento para recibir el mejor movimiento del agente Minimax y enviarlo al backend de voz artificial
        addBehaviour(new PlayerBehavior(this));
    }

}
