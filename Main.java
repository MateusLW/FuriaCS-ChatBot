package br.com.furiachatbotcs;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main 
{
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) 
    {
        try 
        {
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