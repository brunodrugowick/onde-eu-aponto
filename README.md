# Onde eu aponto?

Essa pequena aplicação abre uma interface para buscar issues no Jira de acordo com um critério (palavra ou frase curta). 

A busca no Jira é feita com a linguagem de consulta do Jira (jql) e segue o seguinte padrão:

```
assignee = USUÁRIO and (description ~ "BUSCA" OR summary ~ "BUSCA") and statusCategory not in (Done)
```

# Executando

Disponibilize as seguintes propriedades (no Spring há diversas formas de fazer isso):

```properties
server.port=8090

app.jiraUserStrategy=parse
jira.baseUrl=https://seu-jira.com/rest
jira.integrationUsername=username
jira.integrationPassword=password
jira.maxResults=15
# Consulta padrão para quando nenhum termo é inserido
jira.defaultQuery=assignee = :twikiName and statusCategory not in (Done)

# Params for the REST connection with Jira
feign.client.config.jira.connect-timeout=2000
feign.client.config.jira.read-timeout=3000

# Your client information provided by Google
spring.security.oauth2.client.registration.google.client-id=
spring.security.oauth2.client.registration.google.client-secret=
```

E execute:

```
java -jar ondeeuaponto.jar
```

# Estado atual

O app basicamente replica a função de busca existente no Jira, com passos extras e mais limitado! Hahahaha.

Mas foi divertido implementar...
