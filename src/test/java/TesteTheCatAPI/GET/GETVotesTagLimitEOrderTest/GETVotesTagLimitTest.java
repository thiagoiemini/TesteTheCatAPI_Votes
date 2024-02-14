package TesteTheCatAPI.GET.GETVotesTagLimitEOrderTest;

import TesteTheCatAPI.VotesTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class GETVotesTagLimitTest extends VotesTest {

    @BeforeTest
    public static void setup() {
        VotesTest.setup();
        VotesTest.setBasePath(basePaths.VOTES.getBasePath());
    }

    @Test(description="GET Limit=10 - Deve retornar 10 registros")
    public void GETDeveRetornarSomente10Registros() {
        ResponseBody responseBody = given()
                .spec(VotesTest.requestSpecification())
            .when()
                .get(RestAssured.basePath+"?{limit}","limit=10")
            .then()
                .statusCode(200)
                .extract().response().getBody();

        Assert.assertEquals(responseBody.jsonPath().getList("id").toArray().length, 10);
        responseBody.jsonPath().getList("id").forEach(listaIDs -> {
            Assert.assertNotNull(listaIDs.toString());
        });
//        Arrays.stream(responseBody.jsonPath().getList("value").toArray()).filter(value -> value.equals(1)).toArray();
//        assert responseBody != null;

    }

    @Test(description="GET minLimitPadrao-1 - Deve informar Bad Request")
    public void GETLimitMinimoMenos1DeveInformarErro() {
        given()
            .spec(VotesTest.requestSpecification())
        .when()
            .get(RestAssured.basePath+"?{limit}","limit="+(VotesTest.GETMinLimitPadrao -1))
        .then()
            .statusCode(400)
            .body(containsString("INVALID_DATA"))
            .log().body();
    }

    @Test(description="GET Limit=0 - Deve informar Erro")
    public void GETLimit0DeveInformarErro() {
        given()
            .spec(VotesTest.requestSpecification())
        .when()
            .get(RestAssured.basePath+"?{limit}","limit=0")
        .then()
            .log().status()
            .statusCode(400);
    }

    @Test(description="GET maxLimitPadrao+2 - Deve retornar lista com maxLimitPadrao + 2, com detalhes dos Votes")
    public void GETMaxLimitPadraoMais2DeveRetornarVotes() {
        ResponseBody responseBody = given()
                .spec(VotesTest.requestSpecification())
            .when()
                .get(RestAssured.basePath+"?{limit}","limit="+(VotesTest.GETMaxLimitPadrao +2))
            .then()
                .statusCode(200)
                .extract().response().getBody();

        Assert.assertEquals(responseBody.jsonPath().getList("id").toArray().length, (VotesTest.GETMaxLimitPadrao +2),
                "Deve retornar "+(VotesTest.GETMaxLimitPadrao +2)+" registros");
        responseBody.jsonPath().getList("id").forEach(listaIDs -> {
            Assert.assertNotNull(listaIDs.toString());
        });

//        Arrays.stream(responseBody.jsonPath().getList("value").toArray()).filter(value -> value.equals(1)).toArray();
//        assert responseBody != null;

    }

    @Test(description="GET - Filtro sem params, deve retornar lista com limite padrão máximo, com detalhes de Votes")
    public void GETFiltrarTodosDeveRetornarListaPadrao100Votes() {
        ResponseBody responseBody = given()
                .spec(VotesTest.requestSpecification())
            .when()
                .get(RestAssured.basePath)
            .then()
                .statusCode(200)
                .extract().response().getBody();

        Assert.assertEquals(responseBody.jsonPath().getList("id").toArray().length, VotesTest.GETMaxLimitPadrao,
                "Por padrão esta api possui "+VotesTest.GETMaxLimitPadrao+" registros");
        responseBody.jsonPath().getList("id").forEach(listaIDs -> {
            Assert.assertNotNull(listaIDs.toString());
        });

//        Arrays.stream(responseBody.jsonPath().getList("value").toArray()).filter(value -> value.equals(1)).toArray();
//        assert responseBody != null;

    }

    @Test(description="GET - Garante que todos os Dados(id, image_id, image.id e image.url) estão preenchidos corretamente")
    public void GETFiltrarTodosDeveRetornarCamposPreenchidos() {
        Response response = given()
            .spec(VotesTest.requestSpecification())
        .when()
            .get(RestAssured.basePath)
        .then()
            .statusCode(200)
            .extract().response();


        List listaGeral = response.getBody().jsonPath().get();
        List listaNodeImage = new ArrayList<>();
        listaGeral.forEach(lista -> {
            Assert.assertNotNull(((LinkedHashMap) lista).get("id"));
            Assert.assertNotNull(((LinkedHashMap) lista).get("image_id"));

            listaNodeImage.add( ((LinkedHashMap) lista).getOrDefault("image", null) );
            Assert.assertNotNull(((LinkedHashMap) listaNodeImage.getLast()).get("id"));
            Assert.assertNotNull(((LinkedHashMap) listaNodeImage.getLast()).get("url"));

            Assert.assertEquals(((LinkedHashMap) listaNodeImage.getLast()).get("id"), ((LinkedHashMap) lista).get("image_id"),
                    "Valida que id_image = image.id");
        });
//        List listaIDs = responseBody.jsonPath().getList("id");
//        List listaImageIDs = responseBody.jsonPath().getList("image_id");
//        List listaSubImageIDsUrls = responseBody.jsonPath().getList("image");
//
//        listaIDs.forEach(id -> {
//            Assert.assertNotNull(id.toString());
//        });
//
//        listaSubImageIDsUrls.forEach(lstImage -> {
//            Assert.assertNotNull(((LinkedHashMap) lstImage).get("id"));
//            Assert.assertNotNull(((LinkedHashMap) lstImage).get("url"));
//        });


//        Assert.assertEquals(responseBody.jsonPath().getList("value").toArray().length, 100,
//                "Por padrão esta api sempre possui 100 registros");
//        Assert.assertEquals(responseBody.jsonPath().getList("id").toArray().length, 100,
//                "Por padrão esta api possui 100 registros");
//        Arrays.stream(responseBody.jsonPath().getList("value").toArray()).filter(value -> value.equals(1)).toArray();
//        assert responseBody != null;

    }


}
