package br.com.furiachatbotcs;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import com.sun.net.httpserver.HttpServer;

public class Main 
{
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) 
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", exchange -> {
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().close();
        });
        server.start();
        try 
        {
            System.out.println("Iniciando bot...");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new FuriaCSBot()); //inicia o bot
            System.out.println("Bot iniciado com sucesso!");
        }
         
        catch (TelegramApiException e) 
        {
            e.printStackTrace();
        }
    }
}
