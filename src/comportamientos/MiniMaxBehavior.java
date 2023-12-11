/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comportamientos;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import modelos.EstadoDelJuego;
import modelos.Movimiento;

/**
 *
 * @author johsamar
 */
public class MiniMaxBehavior extends SimpleBehaviour {

    Agent miAgente;
    EstadoDelJuego estado;

    public MiniMaxBehavior(Agent miAgente, EstadoDelJuego estadoDelJuego) {
        System.out.println("Comportaminto minimax");
        this.miAgente = miAgente;
        this.estado = estadoDelJuego;
    }

    @Override
    public void action() {
        try {
            System.out.println("Accion Comportaminto minimax");
            // Crear una plantilla de mensaje para recibir mensajes HTTP
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            AID receiverAID = new AID("jugador", AID.ISLOCALNAME);
            mt = MessageTemplate.and(mt, MessageTemplate.MatchReceiver(new AID[]{receiverAID}));

            // Recibir el mensaje HTTP
            ACLMessage msg = miAgente.receive(mt);

            // Si se recibió un mensaje
//            if (msg != null) {
            // Actualizar el estado del juego con la respuesta recibida
//                estado.actualizar(msg.getContent());
            // Calcular el mejor movimiento
//                        Movimiento mejorMovimiento = calcularMejorMovimiento();
//                Movimiento mejorMovimiento = null;
            // Notificar el mejor movimiento al backend inteligente
            notificarMejorMovimiento("No hay movimientos");
//            }
        } catch (Throwable e) {
            System.out.println("Error minimax" + e.getMessage());
        }
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
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("jugador", AID.ISLOCALNAME));
        msg.setContent(mejorMovimiento);

        // Enviar el mensaje HTTP
        this.miAgente.send(msg);
    }

    @Override
    public boolean done() {
        System.out.println("ya terminé el minimax");
        return true;
    }
}
