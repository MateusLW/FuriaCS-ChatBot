package br.com.furiachatbotcs;

import java.util.ArrayList;
import java.util.List;

public class Responder 
{
    private final List<String> respostas;

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
        inicializarRespostas();//Adiciona todas as possibilidades de respostas dentro das Arrays
    }

    public String generateResponse(int type) 
    {
        if (type < 0 || type >= respostas.size())//Verifica se esta dentro dos limites do ArrayList
        {
            throw new IndexOutOfBoundsException("Tipo de resposta inválido");
        }
        return respostas.get(type);
    }

    private void inicializarRespostas() 
    {
        respostas.add(ResponseType.SAUDACAO.getCode(), 
    "🔥Eai FURIOSO(A), sou o BOT representante da equipe de CS!🔥\n\n" +
    "Como posso te ajudar hoje?\n\n"
    );

    respostas.add(ResponseType.LINEUP.getCode(), 
        "🤍🖤NOSSA LINE DE CS2🖤🤍\n\n" +
        "🎮Jogadores:\n" +
        "- FalleN (IGL)\n" +
        "- KSCERATO\n" +
        "- yuurih\n" +
        "- YEKINDAR\n" +
        "- MOLODY\n" +
        "- skullz (reserva)\n" +
        "- chelo (reserva)\n\n" +
        "📊Coachs:\n" +
        "- Hepa\n" +
        "- sidde\n\n"
    );

    respostas.add(ResponseType.LOJA.getCode(), 
        "LOJA FURIA🖤🤍\n\n"
    );

    respostas.add(ResponseType.JOGOS.getCode(), 
        "🤍🖤Não pode perder nenhum jogo desse TIMÃO certo?🖤🤍\n\n"
    );

    respostas.add(ResponseType.SUPORTE.getCode(), 
        "Se está com dificuldades não se preocupe! Pode buscar assistencia no nosso Whatsapp🗣️\n" +
        "https://api.whatsapp.com/send?l=pt&phone=5511945128297&text=Poderia%20me%20ajudar?\n\n"
    );

    respostas.add(ResponseType.COLLABS.getCode(), 
        "Cheque nossa coleção maravilhosa com a adidas!🖤🤍\n" +
        "https://www.furia.gg/produtos/collabs/adidas\n\n"
    );

    respostas.add(ResponseType.PROXIMO_JOGO.getCode(), 
    "Próxima partida: 19/05 - 05:00\n"+
    "Furia x TheMongolZ\n\n"+
    "Transmissão: Sem informações por enquanto\n\n"+
    "Esperamos você lá!💪💪"
    );

    respostas.add(ResponseType.ULTIMO_RESULTADO.getCode(), 
        "Nosso ultimo jogo foi:\n\n" +
        "FURIA: 2\n" +
        "ODDIK: 0\n\n" +
        "Vamos pra cima com tudo!💪\n\n"
    );
        
    }
}
