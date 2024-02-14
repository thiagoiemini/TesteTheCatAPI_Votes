package TesteTheCatAPI.GET.GETVotesTagLimitEOrderTest;

import TesteTheCatAPI.VotesTest;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class GETVotesTagLimitEOrdenacaoTest {

    @BeforeTest
    public static void setup() {
        VotesTest.setup();
        VotesTest.setBasePath(VotesTest.basePaths.VOTES.getBasePath());
    }

    @Test(description = "GET limit=10 & order=DESC - Deve retornar limite de registros com ordenacao DESC")
    public void GETRetornarLimit10EOrdenacaoDESCPeloID() {
        List<Integer> listaIdsAtual = given()
                .spec(VotesTest.requestSpecification())
            .when()
                .log().all()
                .get(RestAssured.basePath+"?{limit}&{order}", "limit=10", VotesTest.GETtagOrdenacao.DESC.getOrdenacao())
            .then()
                .log().all()
                .statusCode(200)
                .extract().jsonPath().getList("id");

        List<Integer> listaIDsSortedDESC = new ArrayList<>();
        listaIDsSortedDESC.addAll(listaIdsAtual);

        // reverse somente inverte a ordem, sendo necessáro ordenar pelo sort e depois reverter a ordem para DESC
        Collections.sort(listaIDsSortedDESC);
        Collections.reverse(listaIDsSortedDESC);
        Assert.assertEquals(listaIdsAtual, listaIDsSortedDESC, "Listagem deve estar em ordem DESC");
        Assert.assertEquals(listaIdsAtual.size(), 10, "Deve conter 10 registros");
    }

    @Test(description="GET limit=10 & SubId=Null & order=DESC - Deve retornar 10 usuarios null DESC")
    public void GETDeveRetornar10UsuariosNullDESC() {
        ResponseBody responseBody = given()
                .spec(VotesTest.requestSpecification())
            .when()
                .log().all()
                .get(RestAssured.basePath+"?{limit}&{order}&{sub_id}",
                        "limit=10", VotesTest.GETtagOrdenacao.DESC.getOrdenacao(), "sub_id=")
            .then()
                .log().all()
                .statusCode(200)
                .extract().response().getBody();

        List<Integer> listaIdsAtual = new ArrayList<>();
        listaIdsAtual.addAll(responseBody.jsonPath().getList("id"));
        List<Integer> listaIDsSortedDESC = new ArrayList<>();
        listaIDsSortedDESC.addAll(listaIdsAtual);

        // reverse somente inverte a ordem, sendo necessáro ordenar pelo sort e depois reverter a ordem para DESC
        Collections.sort(listaIDsSortedDESC);
        Collections.reverse(listaIDsSortedDESC);
        Assert.assertEquals(listaIdsAtual, listaIDsSortedDESC, "Listagem deve estar em ordem DESC");
        Assert.assertEquals(listaIdsAtual.size(), 10, "Deve conter 10 registros");
    }
}
