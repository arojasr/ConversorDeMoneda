import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class Main {

    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/";
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String opcion;

            do {
                System.out.println("Conversor de Moneda");
                System.out.println("Seleccione la moneda a convertir:");
                System.out.println("1. USD - Dólar estadounidense");
                System.out.println("2. EUR - Euro");
                System.out.println("3. ARS - Peso argentino");
                System.out.println("4. BRL - Real brasileño");
                System.out.println("5. VES - Bolivar venezolano");
                System.out.println("6. KRW - Won surcoreano");
                System.out.println("7. Salir");
                System.out.print("Seleccione una opcion: ");
                opcion = reader.readLine();

                switch (opcion) {
                    case "1":
                        convertirMoneda("USD");
                        break;
                    case "2":
                        convertirMoneda("EUR");
                        break;
                    case "3":
                        convertirMoneda("ARS");
                        break;
                    case "4":
                        convertirMoneda("BRL");
                        break;
                    case "5":
                        convertirMoneda("VES");
                        break;
                    case "6":
                        convertirMoneda("KRW");
                        break;
                    case "7":
                        System.out.println("Saliendo del programa");
                        break;
                    default:
                        System.out.println("Opcion invalida. Pruebe de nuevo.");
                        break;
                }
            } while (!opcion.equals("7"));

            reader.close();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    private static void convertirMoneda(String monedaAConvertir) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Ingrese el monto a convertir en " + monedaAConvertir + ": ");
        double monto = Double.parseDouble(reader.readLine());

        String opcionMonedaDestino;
        do {
            System.out.println("Seleccione la moneda destino:");
            System.out.println("1. USD - Dólar estadounidense");
            System.out.println("2. EUR - Euro");
            System.out.println("3. ARS - Peso argentino");
            System.out.println("4. BRL - Real brasileño");
            System.out.println("5. VES - Bolivar venezolano");
            System.out.println("6. KRW - Won surcoreano");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opcion: ");
            opcionMonedaDestino = reader.readLine();

            switch (opcionMonedaDestino) {
                case "1":
                    solicitudHTTP(monedaAConvertir,"USD",monto);
                    break;
                case "2":
                    solicitudHTTP(monedaAConvertir,"EUR",monto);
                    break;
                case "3":
                    solicitudHTTP(monedaAConvertir,"ARS",monto);
                    break;
                case "4":
                    solicitudHTTP(monedaAConvertir,"BRL",monto);
                    break;
                case "5":
                    solicitudHTTP(monedaAConvertir,"VES",monto);
                    break;
                case "6":
                    solicitudHTTP(monedaAConvertir,"KRW",monto);
                    break;
                case "7":
                    System.out.println("Volviendo atras..");
                    break;
                default:
                    System.out.println("Opcion invalida. Pruebe de nuevo.");
                    break;
            }
        } while (!opcionMonedaDestino.equals("7"));

        //https://v6.exchangerate-api.com/v6/c4de725ba4c18ba1031a4dd6/pair/EUR/GBP/100
    }

    private static void solicitudHTTP (String monedaAConvertir, String monedaDestino, Double monto) throws Exception{
        String apiUrl = API_BASE_URL + "c4de725ba4c18ba1031a4dd6"+"/pair" + "/" + monedaAConvertir + "/" + monedaDestino+ "/" + monto;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        //connection.setRequestProperty("apikey", "c4de725ba4c18ba1031a4dd6");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = responseReader.readLine()) != null) {
                response.append(line);
            }

            responseReader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            //JSONObject ratesObject = jsonResponse.getJSONObject("conversion_rate");
            BigDecimal tasaCambio = jsonResponse.getBigDecimal("conversion_rate");
            Double tasaCambioo = tasaCambio.doubleValue();
            double MontoConvertido = monto * tasaCambioo;

            System.out.println(monto + " " + monedaAConvertir + " equivale a " + MontoConvertido + " " + monedaDestino);
        } else {
            System.out.println("Error al realizar la solicitud HTTP: " + responseCode);
        }
        connection.disconnect();
    }
}