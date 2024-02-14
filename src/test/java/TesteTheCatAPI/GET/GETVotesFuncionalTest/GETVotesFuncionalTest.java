package TesteTheCatAPI.GET.GETVotesFuncionalTest;

import TesteTheCatAPI.VotesTest;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;

public class GETVotesFuncionalTest extends VotesTest {
    @BeforeTest
    public static void setup() {
        VotesTest.setup();
        VotesTest.setBasePath(basePaths.VOTES.getBasePath());
    }

    @Test(description="GET - Faz request sem header para filtro ID, deve exigir autenticação")
    public void GETFiltroIDDeveExigirAutenticacao() {
        given()
        .when()
            .get(RestAssured.basePath+"/{vote_id}", 31514)
        .then()
            .statusCode(401)
            .body(containsString("AUTHENTICATION_ERROR"));
    }

    @Test(description="GET - Faz request sem header para filtrar todos, deve exigir autenticação")
    public void GETFiltrarTodosDeveExigirAutenticacao() {
        given()
        .when()
            .log().all()
            .get(RestAssured.basePath)
        .then()
            .log().all()
            .statusCode(401)
            .body(containsString("AUTHENTICATION_ERROR"));
    }

    @Test(description="GET - Informa ID, deve retornar detalhes deste Votes")
    public void GETFiltraIDDeveRetornar1Votes() {
        given()
                .spec(VotesTest.requestSpecification())
        .when()
                .get(RestAssured.basePath+"/{vote_id}", 31514)
        .then()
                .statusCode(200)
                .body("id", equalTo(31514))
                .body("image_id",equalTo("8sc"));
    }

    @Test(description="GET - Informa ID inexistente, deve retornar erro")
    public void GETFiltraIDInvalidoDeveRetornarErro() {
        given()
                .spec(VotesTest.requestSpecification())
            .when()
                .log().all()
                .get(RestAssured.basePath+"/{vote_id}", 12)
            .then()
                .log().all()
                .statusCode(404)
                .spec(RestAssured.expect().body(containsString("NOT_FOUND")));
    }

    @Test(description="GET - Valida integridade e referencia do image_id para usuario especifico")
    public void GETValidaIntegridadeEReferenciaDoImageID() {
        List listaGeral = given()
                .spec(VotesTest.requestSpecification())
            .when()
                .log().all()
                .get(RestAssured.basePath+"?{sub_id}&{orderDESC}", "sub_id=my-user-1234", GETtagOrdenacao.DESC.getOrdenacao())
            .then()
                .log().all()
                .statusCode(200)
                .extract().response().getBody().jsonPath().get();

        List listaNodeImage = new ArrayList<>();
        listaGeral.forEach(lista -> {
            Assert.assertNotNull(((LinkedHashMap) lista).get("id"));
            Assert.assertNotNull(((LinkedHashMap) lista).get("image_id"));

            listaNodeImage.add( ((LinkedHashMap) lista).getOrDefault("image", null) );
            Assert.assertNotNull(((LinkedHashMap) listaNodeImage.getLast()).get("id"), "Node image não pode ser null, ou seja, image:{}");
            Assert.assertNotNull(((LinkedHashMap) listaNodeImage.getLast()).get("url"), "Node image não pode ser null, ou seja, image:{}");

            Assert.assertEquals(((LinkedHashMap) listaNodeImage.getLast()).get("id"), ((LinkedHashMap) lista).get("image_id"),
                    "Valida que id_image = image.id");
        });
    }

}

