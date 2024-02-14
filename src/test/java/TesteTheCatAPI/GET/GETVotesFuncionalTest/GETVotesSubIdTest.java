package TesteTheCatAPI.GET.GETVotesFuncionalTest;

import TesteTheCatAPI.VotesTest;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GETVotesSubIdTest extends VotesTest {

    @BeforeTest
    public static void setup() {
        VotesTest.setup();
        VotesTest.setBasePath(basePaths.VOTES.getBasePath());
    }

    @Test(description="GET SubId Válido - Deve retornar lista Votes do usuário específico")
    public void GETEnviaSubIdDeveRetornarListaDoUsuarioEspecificado() {
        ResponseBody responseBody = given()
            .spec(VotesTest.requestSpecification())
        .when()
            .log().all()
            .get(RestAssured.basePath+"?{sub_id}", "sub_id=demo-0.40524506327750644")
        .then()
            .log().all()
            .statusCode(200)
            .extract().response().getBody();

        responseBody.jsonPath().getList("sub_id").forEach(listaSubIDs -> {
            Assert.assertEquals(listaSubIDs.toString(), "demo-0.40524506327750644");
        });
    }

    @Test(description="GET SubId inexistente - Deve retornar vazio")
    public void GETEnviaSubIdInexistenteDeveRetornarVazio() {
        ResponseBody responseBody = given()
                .spec(VotesTest.requestSpecification())
            .when()
                .log().all()
                .get(RestAssured.basePath+"?{sub_id}", "sub_id=?")
            .then()
                .log().all()
                .statusCode(200)
                .extract().response().getBody();

        Assert.assertEquals(responseBody.jsonPath().getList("sub_id").size(), 0);
    }

    @Test(description="GET SubId Null - Deve retornar usuarios null")
    public void GETEnviaNullDeveRetornarCamposNulos() {
        ResponseBody responseBody = given()
                .spec(VotesTest.requestSpecification())
            .when()
                .log().all()
                .get(RestAssured.basePath+"?{sub_id}", "sub_id=")
            .then()
                .log().all()
                .statusCode(200)
                .extract().response().getBody();

        Assert.assertNotEquals(responseBody.jsonPath().getList("sub_id").size(), 0);
        responseBody.jsonPath().getList("sub_id").forEach(listaSubIDs -> {
            Assert.assertNull(listaSubIDs, "sub_id deve ser null");
        });
    }
    
}
