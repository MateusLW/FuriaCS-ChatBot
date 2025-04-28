package br.com.furiachatbotcs;
import java.util.LinkedList;

public class Questions 
{
    private final LinkedList<Integer> typesQuestion; // Armazena o histórico de tipos de perguntas
    
    public Questions() 
    {
        typesQuestion = new LinkedList<>();
    }

    /**
     * Calcula o próximo tipo de pergunta com base na escolha do usuário.
     * @param choice Resposta do usuário (ex: 1, 2, 3).
     * @return Próximo tipo de pergunta ou -1 se a escolha for inválida.
     */
    public int nextQuestion(int choice) 
    {
        if (choice == 0) 
        {
            typesQuestion.clear();       // Reinicia o fluxo
            typesQuestion.add(choice);
        } 
        else 
        {
            int nextType = getNextType(typesQuestion.getLast(), choice);
            if (nextType == -1) 
                return -1; //Retorna o erro antes de ser armazenado

            typesQuestion.add(nextType); //Armazena o tipo usado
        }
        return typesQuestion.getLast();
    }

    /**
     * Lógica para determinar o próximo tipo de pergunta.
     */
    private int getNextType(int currentType, int choice) 
    {
        if(choice>0 && choice<=3 && currentType==0)//Verifica o numero de escolhas para a primeira pergunta
            return choice;
        else if(choice>0 && choice<=2)    //Verifica o numero de escolhas para a segunda pergunta
        {
            if(currentType==2)
                return choice+3;
            else if(currentType==3)
                return choice+5;
        }
        return -1;
    }
}