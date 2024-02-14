package TesteTheCatAPI.GET.GETVotesTagLimitEOrderTest;

import TesteTheCatAPI.VotesTest;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class GETVotesTagOrdenacaoTest extends VotesTest {

    @BeforeTest
    public static void setup() {
        VotesTest.setup();
        VotesTest.setBasePath(basePaths.VOTES.getBasePath());
    }

    @Test(description="GET - Ordenação ASC pelo ID")
    public void GETDeveOrdenarASCPeloID() {
        List<Integer> listaIdsAtual = given()
                .spec(VotesTest.requestSpecification())
            .when()
                .log().all()
                .get(RestAssured.basePath+"?{order}", GETtagOrdenacao.ASC.ordenacao)
            .then()
                .statusCode(200)
                .extract().jsonPath(). getList("id");

        List<Integer> listaIDsSortedASC = new ArrayList<>();
        listaIDsSortedASC.addAll(listaIdsAtual);
        Collections.sort(listaIDsSortedASC);
        Assert.assertEquals(listaIdsAtual, listaIDsSortedASC, "Listagem deve estar em ordem ASC");
    }

    @Test(description="GET - Ordenação DESC pelo ID")
    public void GETDeveOrdenarDESCPeloID() {
        List<Integer> listaIdsAtual = given()
                .spec(VotesTest.requestSpecification())
            .when()
                .log().all()
                .get(RestAssured.basePath+"?{order}", GETtagOrdenacao.DESC.ordenacao)
            .then()
                .statusCode(200)
                .extract().jsonPath().getList("id");

        List<Integer> listaIDsSortedDESC = new ArrayList<>();
        listaIDsSortedDESC.addAll(listaIdsAtual);
        Collections.sort(listaIDsSortedDESC);
        Collections.reverse(listaIDsSortedDESC);
        Assert.assertEquals(listaIdsAtual, listaIDsSortedDESC, "Listagem deve estar em ordem DESC");
    }
}
