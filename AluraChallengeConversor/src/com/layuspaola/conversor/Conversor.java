package com.layuspaola.conversor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Conversor {
    private static final String API_KEY = "654f5c6c579f1c611f440ce7";
    private static final String BASE_API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";
    private static final String[] OPCIONES_MONEDA = {"Dolar =>> Peso argentino", "Peso argentino =>> Dolar",
            "Dolar =>> Real brasileño", "Real brasileño =>> Dolar",
            "Dolar =>> Peso colombiano", "Peso colombiano =>> Dolar", "Salir"};

    public static void main(String[] args) {

        System.out.println("******************************************************");
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Bienvenido al conversor de monedas");

            while (true) {
                System.out.println("Seleccione la conversión que desea realizar:");
                mostrarOpcionesMoneda();
                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        convertirMoneda("USD", "ARS", scanner);
                        break;
                    case 2:
                        convertirMoneda("ARS", "USD", scanner);
                        break;
                    case 3:
                        convertirMoneda("USD", "BRL", scanner);
                        break;
                    case 4:
                        convertirMoneda("BRL", "USD", scanner);
                        break;
                    case 5:
                        convertirMoneda("USD", "COP", scanner);
                        break;
                    case 6:
                        convertirMoneda("COP", "USD", scanner);
                        break;
                    case 7:
                        System.out.println("Gracias por utilizar el conversor de monedas.");
                        return; // Salir del programa
                    default:
                        System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error al obtener tasas de cambio: " + e.getMessage());
        }

    }

    private static void mostrarOpcionesMoneda() {
        for (int i = 0; i < OPCIONES_MONEDA.length; i++) {
            System.out.println((i + 1) + ". " + OPCIONES_MONEDA[i]);

        }
        System.out.println("******************************************************");
    }

    private static void convertirMoneda(String monedaOrigen, String monedaDestino, Scanner scanner) throws IOException {
        System.out.println("Ingrese la cantidad de dinero en " + monedaOrigen + ": ");
        double cantidad = scanner.nextDouble();

        double tasaCambio = obtenerTasaDeCambio(monedaOrigen, monedaDestino);
        double equivalente = cantidad * tasaCambio;
        System.out.println("Equivalente de " + cantidad + " " + monedaOrigen + " a " + monedaDestino + ": " + equivalente);
        System.out.println();
    }

    private static double obtenerTasaDeCambio(String monedaOrigen, String monedaDestino) throws IOException {
        String apiUrl = BASE_API_URL + monedaOrigen;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(inputStreamReader, JsonObject.class);

        connection.disconnect();

        return jsonResponse.get("conversion_rates").getAsJsonObject().get(monedaDestino).getAsDouble();
    }
}
