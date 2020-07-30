# Onde eu aponto?

Essa pequena aplicação abre uma interface para buscar issues no Jira de acordo com um critério (palavra ou frase curta). 

A busca no Jira é feita com a linguagem de consulta do Jira (jql) e segue o seguinte padrão:

```
assignee = USUÁRIO and (description ~ "BUSCA" OR summary ~ "BUSCA") and statusCategory not in (Done)
```

# Executando

```
java -jar onde-eu-aponto.jar /
--jira.baseUrl=https://seu-jira.com/rest /
--jira.username=USUARIO /
--jira.password=SENHA /
--jira.maxResults=20 /
--server.port=8090
```

Acesse em `localhost:8090`.

# Estado atual

Há uma lista de issues registrada localmente. Essas issues são as fixas de todos os Chapters. A busca atualmente é feita localmente e no Jira, portanto podem haver resultados duplicados.

Tem muitos pequenos probleminhas, por enquanto só gastei 6 horas nesse projeto inteiro. Colabora comigo se quiser! =)
