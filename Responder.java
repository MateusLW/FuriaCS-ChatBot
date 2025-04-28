package br.com.furiachatbotcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Responder 
{
    private final List<String> respostas;
    private final List<String> respostasErro;
    private final Random sorteio;

    // Enum para tipos de resposta melhorando a visibilidade
    public enum ResponseType 
    {
        SAUDACAO(0),
        LINEUP(1),
        LOJA(2),
        JOGOS(3),
        SUPORTE(4),
        COLLABS(5),
        PROXIMO_JOGO(6),
        ULTIMO_RESULTADO(7);

        private final int code;
        ResponseType(int code) { this.code = code; }
        public int getCode() { return code; }
    }

    public Responder() 
    {
        respostas = new ArrayList<>();
        respostasErro = new ArrayList<>();
        sorteio = new Random();

        inicializarRespostas();//Adiciona todas as possibilidades de respostas dentro das Arrays
    }

    public String generateResponse(int type) 
    {
        try 
        {
            if (type == -1)
            {
                return getRandomErrorMessage();//Retorna mensagem de erro caso houve a impossibilidade de determinar o tipo da pergunta
            }
            return getResponseByType(type);//Retorna a mensagem referente ao tipo passado
        } 
        catch (IndexOutOfBoundsException e) 
        {
            return getRandomErrorMessage();
        }
    }

    private String getResponseByType(int type) 
    {
        if (type < 0 || type >= respostas.size())//Verifica se esta dentro dos limites do ArrayList
        {
            throw new IndexOutOfBoundsException("Tipo de resposta inválido");
        }
        return respostas.get(type);
    }

    private String getRandomErrorMessage() 
    {
        return respostasErro.get(sorteio.nextInt(respostasErro.size()));//Sorteia uma mensagem de erro para ser enviada evitando repeticao
    }

    private void inicializarRespostas() 
    {
        respostas.add(ResponseType.SAUDACAO.getCode(), """
                                                       🔥Eai FURIOSO(A), sou o BOT representante da equipe de CS!🔥
                                                       
                                                       Como posso te ajudar hoje?
                                                       
                                                       1. Qual a Line atual de CS da FURIA?
                                                       2. Preciso de ajuda com a loja
                                                       3. Tenho dúvida quanto aos jogos do time
                                                       
                                                       🔢Envie o número da sua dúvida
                                                       🔄 Envie 0 para reiniciar""");

        respostas.add(ResponseType.LINEUP.getCode(), """
                                                     🤍🖤NOSSA LINE DE CS2🖤🤍
                                                     
                                                     🎮Jogadores:
                                                     - FalleN (IGL)
                                                     - KSCERATO
                                                     - yuurih
                                                     - YEKINDAR
                                                     - MOLODY
                                                     - skullz (reserva)
                                                     - chelo (reserva)
                                                     
                                                     📊Coachs:
                                                     - Hepa
                                                     - sidde
                                                     
                                                     🔄 Envie 0 para reiniciar""");

        respostas.add(ResponseType.LOJA.getCode(), """
                                                   LOJA FURIA🖤🤍
                                                   
                                                   1. Estou com dificuldades na loja
                                                   2. Quero ver o último lançamento!
                                                   
                                                   🔢 Escolha uma opção
                                                   🔄 Envie 0 para reiniciar""");

        respostas.add(ResponseType.JOGOS.getCode(), """
                                                    🤍🖤Não pode perder nenhum jogo desse TIMÃO certo?🖤🤍
                                                    
                                                    1.Quero saber quando e onde posso assistir o próximo jogo
                                                    
                                                    2. Queria saber o resultado do ultimo jogo da FURIA
                                                    
                                                    🔢Escreva e envie no chat o numero referente a sua dúvida.
                                                    🔄 Envie 0 para resetar.""");
        respostas.add(ResponseType.SUPORTE.getCode(), """
                                                      Se está com dificuldades não se preocupe! Pode buscar assistencia no nosso Whatsapp🗣️
                                                      https://api.whatsapp.com/send?l=pt&phone=5511945128297&text=Poderia%20me%20ajudar? 
                                                      
                                                      🔄 Envie 0 para resetar.""");
        respostas.add(ResponseType.COLLABS.getCode(), """
                                                      Cheque nossa coleção maravilhosa com a adidas!🖤🤍
                                                      https://www.furia.gg/produtos/collabs/adidas 
                                                      
                                                      🔄 Envie 0 para resetar.""");
        respostas.add(ResponseType.PROXIMO_JOGO.getCode(), """
                                                           No momento estamos sem partida agendada, mas nosso próximo torneio começa dia 12/05, então fique ligado!👾
                                                           
                                                           🔄 Envie 0 para resetar.""");
        respostas.add(ResponseType.ULTIMO_RESULTADO.getCode(), """
                                                               Nosso ultimo jogo foi:
                                                               
                                                               FURIA: 0
                                                               TheMongolZ: 2
                                                               
                                                               Não foi dessa fez, mas permaneceremos fortes para a próxima💪!
                                                               
                                                               🔄 Envie 0 para resetar.""");
        
        // Mensagens de erro
        respostasErro.add("❌ Ops, não reconheci essa opção!");
        respostasErro.add("⚠️ Por favor, escolha uma das opções válidas");
        respostasErro.add("🔢 Envie apenas números das opções mostradas");
    }
}