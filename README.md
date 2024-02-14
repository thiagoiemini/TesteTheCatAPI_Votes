# TesteTheCatAPI_Votes

- Teste Rest API usando **Rest-Assured** Java Framework;
- API utilizada como exemplo - **https://api.thecatapi.com**;
- Testando recurso **Votes**;
- Verbos básicos (POST, GET e DELETE);
- Biblioteca para JSON - **json-simple**;

## Desenvolvido no Java InteliJIdea Community + Maven

- Dependência necessárias **jdk-21**, e informadas no arquivo **pom.xml** ou abaixo:

		<dependency>		
		    <groupId>io.rest-assured</groupId>
		    <artifactId>rest-assured</artifactId>
		    <version>3.3.0</version>
		    <scope>test</scope>
		</dependency>
  
		<dependency>
			<groupId>io.rest-assured</groupId>
			<artifactId>rest-assured</artifactId>
			<version>5.4.0</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.testng</groupId>
			<artifactId>testng</artifactId>
			<version>7.7.0</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>io.rest-assured</groupId>
			<artifactId>json-path</artifactId>
			<version>5.4.0</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>io.rest-assured</groupId>
			<artifactId>json-schema-validator</artifactId>
			<version>5.4.0</version>
		</dependency>
		<dependency>
			<groupId>com.googlecode.json-simple</groupId>
			<artifactId>json-simple</artifactId>
			<version>1.1.1</version>
		</dependency>

## Sobre o teste
- Implementado blocos no Rest Assured - given, when, then
- Utilizada classe **RestAssured** para setar configurações iniciais e configs - RestAssured.basepath, RestAssured.baseURI etc.
- Criada classe **VotesTest** para extends de configurações gerais de todos os testes;
- Os testes GET, POST e TEST estão dividos em pacotes na src/test/java/TesteTheCatAPI;

		Contemplando:
			Testes Funcionais;
			Testes de Campos;
			Testes de StatusCode;
			Testes invalides e exceções;
			Testes de Contrato;
  
- Documentação API seguida como regra para **Votes**:
  
	https://developers.thecatapi.com/view-account/ylX4blBYT9FaoVd6OhvR?report=R0qdMcvVV
	https://documenter.getpostman.com/view/5578104/RWgqUxxh#fc3df122-ea8f-4cad-aa4f-32d06d62d02c

## Melhorias identificadas no Votes
- Melhorias Autenticação:

  		Poderia trabalhar com geração de Token;

- Melhorias GET Votes:

		Retorno de N mais votados com ordenação DESC ou ASC - onde N é a quantidade limite desejada;
		Filtros por image_id - Exemplo: saber quantos usuarios interagiram com esta imagem;
		Filtros por image_id com sub_id - Exemplo: saber se usuario interagiu com a imagem mais de uma vez, like e deslike;
		Filtros por regiao country_code;

- Melhorias GET Votes:

		Ao passar value deveria somar ou subtrair ao invés de setar o valor;
		Criar um param somarVote=N para somar N votos;
		Criar um param subVote=N para subtrair N votos;
