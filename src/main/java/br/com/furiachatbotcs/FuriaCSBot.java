package br.com.furiachatbotcs;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class FuriaCSBot extends TelegramLongPollingBot 
{
    private static final Logger LOGGER = Logger.getLogger(FuriaCSBot.class.getName());
    private final Responder responseGenerator;
    
    public FuriaCSBot() 
    { 
        responseGenerator = new Responder();
    }

    @Override
    public String getBotUsername() 
    {
        return System.getenv().getOrDefault("BOT_USERNAME", "FuriaCSBot");
    }
    
    @Override
    private String getBotToken() 
    {
        return System.getenv("BOT_TOKEN");
    }    
    
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleButtonPress(update);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update);
        }
    }

    private void handleButtonPress(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String callbackData = update.getCallbackQuery().getData();
        
        try {
            int choice = Integer.parseInt(callbackData);

            if(choice!=0)
            {
                String response = responseGenerator.generateResponse(choice);
                
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText(response);
                
                // Verifica se precisa adicionar sub-botões
                InlineKeyboardMarkup keyboard = createSubMenuKeyboard(choice);
                if (keyboard != null) {
                    message.setReplyMarkup(keyboard);
                } else if (choice != 0) { // Se não for menu principal nem submenu, adiciona botão Voltar
                    message.setReplyMarkup(createBackButton());
                }
                
                execute(message);
            }
            else 
             sendMainMenu(chatId);
        } catch (NumberFormatException | TelegramApiException e) {
            sendErrorMessage(chatId);
        }
    }
    private InlineKeyboardMarkup createSubMenuKeyboard(int choice) {
        return switch (choice) 
        {
            case 1 -> null; // LINEUP - Não precisa de sub-botões
            case 2 -> createStoreSubMenu();// LOJA
            case 3 -> createGamesSubMenu();// JOGOS
            case 4 -> null; // SUPORTE (sub-opção da LOJA)
            case 5 -> null; // COLLABS (sub-opção da LOJA)
            case 6 -> null; // PROXIMO_JOGO (sub-opção de JOGOS)
            case 7 -> null; // ULTIMO_RESULTADO (sub-opção de JOGOS)
            default -> null; // Menu principal ou outros
        };
    }

    private InlineKeyboardMarkup createStoreSubMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        
        // Botão 1 - Dificuldades na loja (SUPORTE)
        InlineKeyboardButton supportButton = new InlineKeyboardButton();
        supportButton.setText("Dificuldades na loja");
        supportButton.setCallbackData(String.valueOf(Responder.ResponseType.SUPORTE.getCode()));
        
        // Botão 2 - Último lançamento (COLLABS)
        InlineKeyboardButton collabsButton = new InlineKeyboardButton();
        collabsButton.setText("Último lançamento");
        collabsButton.setCallbackData(String.valueOf(Responder.ResponseType.COLLABS.getCode()));
        
        // Botão 3 - Voltar
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Voltar");
        backButton.setCallbackData("0");
        
        keyboard.add(List.of(supportButton));
        keyboard.add(List.of(collabsButton));
        keyboard.add(List.of(backButton));
        
        return new InlineKeyboardMarkup(keyboard);
    }
    
    private InlineKeyboardMarkup createGamesSubMenu() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        
        // Botão 1 - Próximo jogo
        InlineKeyboardButton nextGameButton = new InlineKeyboardButton();
        nextGameButton.setText("Próximo jogo");
        nextGameButton.setCallbackData(String.valueOf(Responder.ResponseType.PROXIMO_JOGO.getCode()));
        
        // Botão 2 - Último resultado
        InlineKeyboardButton lastResultButton = new InlineKeyboardButton();
        lastResultButton.setText("Último resultado");
        lastResultButton.setCallbackData(String.valueOf(Responder.ResponseType.ULTIMO_RESULTADO.getCode()));
        
        // Botão 3 - Voltar
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Voltar");
        backButton.setCallbackData("0");
        
        keyboard.add(List.of(nextGameButton));
        keyboard.add(List.of(lastResultButton));
        keyboard.add(List.of(backButton));
        
        return new InlineKeyboardMarkup(keyboard);
    }

    private void handleTextMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        
        if (update.getMessage().getText().equals("/start")) {
            sendMainMenu(chatId);
        }
    }

    private void sendMainMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(responseGenerator.generateResponse(Responder.ResponseType.SAUDACAO.getCode()));
        message.setReplyMarkup(createMainMenuKeyboard());
        
        try {
            execute(message);
        } catch (TelegramApiException e) {
            sendErrorMessage(chatId);
        }
    }

    private InlineKeyboardMarkup createMainMenuKeyboard() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        
        // Linha 1 - Line atual
        InlineKeyboardButton lineupButton = new InlineKeyboardButton();
        lineupButton.setText("Line atual");
        lineupButton.setCallbackData(String.valueOf(Responder.ResponseType.LINEUP.getCode()));
        
        // Linha 2 - Ajuda com a loja
        InlineKeyboardButton storeButton = new InlineKeyboardButton();
        storeButton.setText("Ajuda com a loja");
        storeButton.setCallbackData(String.valueOf(Responder.ResponseType.LOJA.getCode()));
        
        // Linha 3 - Dúvidas sobre jogos
        InlineKeyboardButton gamesButton = new InlineKeyboardButton();
        gamesButton.setText("Dúvidas sobre jogos");
        gamesButton.setCallbackData(String.valueOf(Responder.ResponseType.JOGOS.getCode()));
        
        // Adiciona os botões em linhas separadas
        keyboard.add(List.of(lineupButton));
        keyboard.add(List.of(storeButton));
        keyboard.add(List.of(gamesButton));
        
        return new InlineKeyboardMarkup(keyboard);
    }

    private InlineKeyboardMarkup createBackButton() {
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Voltar ao menu");
        backButton.setCallbackData("0");
        
        return new InlineKeyboardMarkup(List.of(List.of(backButton)));
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
