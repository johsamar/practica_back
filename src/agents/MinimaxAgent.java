package agents;

import comportamientos.MiniMaxBehavior;
import modelos.Movimiento;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import modelos.EstadoDelJuego;

public class MinimaxAgent extends Agent {

    private EstadoDelJuego estado;

    protected void setup() {
        System.out.println("Dentro del agente");
        Object[] args = getArguments();

        estado = (EstadoDelJuego) args[0];
        addBehaviour(new MiniMaxBehavior(this, estado));
    }

    public EstadoDelJuego getEstado() {
        return estado;
    }

}
