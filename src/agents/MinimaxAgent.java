package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import agents.EstadoDelJuego;

public class AgenteMinimax extends Agent {

    private EstadoDelJuego estado;

    protected void setup() {
        estado = new EstadoDelJuego();

        // Crear un comportamiento cíclico para recibir el estado del juego del backend inteligente
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                try {
                    // Crear una plantilla de mensaje para recibir mensajes HTTP
                    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.POST);
                    mt.addReceiver(new AID("BackendInteligente", AID.ISLOCALNAME));

                    // Recibir el mensaje HTTP
                    ACLMessage msg = receive(mt);

                    // Si se recibió un mensaje
                    if (msg != null) {
                        // Actualizar el estado del juego con la respuesta recibida
                        estado.actualizar(msg.getContent());

                        // Calcular el mejor movimiento
                        Movimiento mejorMovimiento = calcularMejorMovimiento();

                        // Notificar el mejor movimiento al backend inteligente
                        notificarMejorMovimiento(mejorMovimiento.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private int minimax(EstadoDelJuego estado, int profundidad, boolean esMaximizador, int alpha, int beta) {
        // Actualiza el estado del juego
        // ...

                this.estado = estado;

                int valorTablero = estado.evaluar();

                // Caso base: Si el juego ha terminado
                if (Math.abs(valorTablero) == 10 || estado.esFinalDelJuego() || profundidad == 0) {
                    return valorTablero;
                }

        if (esMaximizador) {
            int mejorValor = Integer.MIN_VALUE;

            for (Movimiento movimiento : estado.obtenerMovimientosPosibles()) {
                estado.hacerMovimiento(movimiento);
                mejorValor = Math.max(mejorValor, minimax(estado, profundidad - 1, false, alpha, beta));
                estado.revertirMovimiento(movimiento);

                if (beta <= mejorValor) {
                    return mejorValor;
                }

                alpha = Math.max(alpha, mejorValor);
            }

            return mejorValor;
        } else {
            int mejorValor = Integer.MAX_VALUE;

            for (Movimiento movimiento : estado.obtenerMovimientosPosibles()) {
                estado.hacerMovimiento(movimiento);
                mejorValor = Math.min(mejorValor, minimax(estado, profundidad - 1, true, alpha, beta));
                estado.revertirMovimiento(movimiento);

                if (alpha >= mejorValor) {
                    return mejorValor;
                }

                beta = Math.min(beta, mejorValor);
            }

            return mejorValor;
        }
    }

    private void notificarMejorMovimiento(String mejorMovimiento) {
        // Crear un mensaje HTTP
        ACLMessage msg = new ACLMessage(ACLMessage.POST);
        msg.addReceiver(new AID("BackendInteligente", AID.ISLOCALNAME));
        msg.setContent(mejorMovimiento);

        // Enviar el mensaje HTTP
        send(msg);
    }
}
