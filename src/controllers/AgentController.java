/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 *
 * @author Asus
 */
public class AgentController implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                // Manejar solicitudes GET
                String response = "{\"message\": \"Hola Mundo (GET)\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                //Iniciando agente
                // Inicializa la plataforma JADE
                jade.core.Runtime rt = jade.core.Runtime.instance();

                // Permite configurar los perfiles según tus necesidades
                Profile profile = new ProfileImpl();

                // Crea un contenedor
                ContainerController container = rt.createMainContainer(profile);

                try {
                    // Crea un agente
                    jade.wrapper.AgentController agent = container.createNewAgent("Robot1", "agents.Robot", null);
                    // Inicializa el agente
                    agent.start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            } else if ("POST".equals(exchange.getRequestMethod())) {
                // Procesar la solicitud POST
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String postData = sb.toString();
                // Aquí puedes procesar los datos POST, por ejemplo, almacenarlos en una base de datos
                System.out.println("Datos POST recibidos: " + postData);
                String response = "{\"message\": \"Datos POST recibidos\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Manejar otros métodos (por ejemplo, PUT, DELETE, etc.) aquí
                String response = "{\"message\": \"Solicitud no admitida\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(405, response.length()); // 405 Method Not Allowed
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }