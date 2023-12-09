package agents;

import jade.core.Agent;

public class MiAgente extends Agent {

    protected void setup() {
        System.out.println("Hola, soy un agente!");
        System.out.println("Mi nombre es " + getLocalName());
    }
}
