<h1>Furia ChatBot CS</h1>
<h2>Arquivos</h2>
<ol>
  <li>Main- Inicializa o BOT telegram</li>
  <li>FuriaCSBot- Recebe o update, checa se possui alguma mensagem, se sim processa a informação com objetos Questions e Responder para enviar uma mensagem de resposta.</li>
  <li>Questions- Processa a informação da mensagem recebida, verificando a escolha do usuario, o type da resposta anterior, e retorna o type da nova resposta.</li>
  <li>Responder- Identifica o type da nova resposta e retorna a String referente ao type.</li>
  <li>pom.xml- Arquivo Maven que aplica a API do telegram e aplicações como gerar um executavel.</li>
</ol>
<h2>DockerFile e .gitignore</h2>
<h3>Arquivos necessários para aplicar o projeto em um Host, atualmente o RailWay.</h3>
