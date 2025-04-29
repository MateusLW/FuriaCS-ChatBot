package br.com.furiachatbotcs;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class FuriaCSBot extends TelegramLongPollingBot 
{
    private static final Logger LOGGER = Logger.getLogger(FuriaCSBot.class.getName());
    private final Questions questionManager;
    private final Responder responseGenerator;
    
    public FuriaCSBot() 
    {
        questionManager = new Questions();
        responseGenerator = new Responder();
    }

    @Override
    public String getBotUsername() 
    {
        return System.getenv().getOrDefault("BOT_USERNAME", "FuriaCSBot");
    }
    
    @Override
    public String getBotToken() 
    {
        return System.getenv().getOrDefault("BOT_TOKEN", "7671407981:AAFjCNqJEG5_ohgZrBM9cHqhJ4C215JmGSE");
    }
    
    @Override
    public void onUpdateReceived(Update update) 
    {
        if (!update.hasMessage() || !update.getMessage().hasText()) //Verifica se foi enviado alguma mensagem
        {
            return;
        }

        long chatId = update.getMessage().getChatId(); //Armazena o chatId
        String userInput = update.getMessage().getText().trim(); //Armazena a mensagem enviada
        
        try 
        {
            Optional<Integer> userChoice = parseUserChoice(userInput);//Recebe o valor inteiro da String se houver
            int choice = userChoice.orElse(0); // 0 para reset ou inválido
            
            String response = processUserChoice(choice);//Retorna a proxima mensagem a ser enviada dada a escolha do usuario
            sendResponse(chatId, response);//Envia a mensagem
            
        } 
        catch (Exception e) 
        {
            LOGGER.log(Level.SEVERE, "Erro ao processar mensagem", e);
            sendErrorMessage(chatId);
        }
    }

    private Optional<Integer> parseUserChoice(String input) 
    {
        try 
        {
            int choice = Integer.parseInt(input);
            return Optional.of(choice);
        } 
        catch (NumberFormatException e) 
        {
            return Optional.empty();
        }
    }

    private String processUserChoice(int choice) 
    {
        int questionType = questionManager.nextQuestion(choice);//Descobre qual o tipo da proxima mensagem
        return responseGenerator.generateResponse(questionType);//Retorna a resposta respectiva ao tipo
    }

    private void sendResponse(long chatId, String text) 
    {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));//Localiza o Id do chat
        message.setText(text);//Informa o texto a ser enviado
        message.enableMarkdown(true);
        
        try 
        {
            execute(message);
        } catch (TelegramApiException e) 
        {
            LOGGER.log(Level.SEVERE, "Falha ao enviar mensagem", e);
        }
    }

    private void sendErrorMessage(long chatId) 
    {
        SendMessage errorMsg = new SendMessage();
        errorMsg.setChatId(String.valueOf(chatId));
        errorMsg.setText("⚠️ Ocorreu um erro inesperado. Tente novamente mais tarde.");
        
        try 
        {
            execute(errorMsg);
        } 
        catch (TelegramApiException e) 
        {
            LOGGER.log(Level.SEVERE, "Falha ao enviar mensagem de erro", e);
        }
    }
}