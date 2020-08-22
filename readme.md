### Ferramentas
Gradle versão 6.6
<br/>
Java versão 11.0.8

### Construção
```shell
gradle clean build
```

O comando irá baixar todas as dependências do projeto e criar um diretório *build/libs* com os artefatos construídos, que incluem o arquivo jar do projeto. Além disso, serão executados os testes unitários, e se algum falhar, o Maven exibirá essa informação no console.
<br/>
<br/>
 A escolha de utilização do socket Channel foi devida a leituda de sem bloqueios fazendo que que apenas uma thread se comunique com varias conexões abertas
  no mesmo servidor atraves da criação de multiplos SocketChannels registrados em um selector.
 evitando assim um overhead de criação de theads por conexão, pois a troca de contexto entre threads eh custosa para o sistema operacional
 
 <br/>
 <br/>
 Para executar o aplicativo, executar o seguinte comando após o build:
 
 ```shell
 java -jar build/libs/movie-imdb-searcher-1.0-SNAPSHOT.jar
 ```

 