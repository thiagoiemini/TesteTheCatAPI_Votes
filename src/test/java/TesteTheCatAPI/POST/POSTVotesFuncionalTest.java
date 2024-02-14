package TesteTheCatAPI.POST;

import TesteTheCatAPI.VotesTest;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.core.IsEqual.equalTo;

import static io.restassured.RestAssured.given;

public class POSTVotesFuncionalTest extends VotesTest {
    JSONObject jsonPost = new JSONObject();

    @BeforeTest
    public static void setup() {
        VotesTest.setup();
        VotesTest.setBasePath(VotesTest.basePaths.VOTES.getBasePath());
    }

    @AfterTest
    public void tearDown() {
        jsonPost.clear();
    }

    ResponseBody preparaCenarioAddRemVotos(int valorVoto) {
        // retorna voto mais atualizado de my-user-1234
        ResponseBody listaUltimoVotoDoUsuario = given()
            .spec(VotesTest.requestSpecification())
            .log().all()
            .get(RestAssured.basePath+"?{sub_id}&{limit}&{orderDESC}",
                    "sub_id=my-user-1234","limit=1", GETtagOrdenacao.DESC.getOrdenacao())
            .getBody();

        jsonPost.put("image_id", listaUltimoVotoDoUsuario.jsonPath().getList("image_id").getFirst());
        jsonPost.put("sub_id", listaUltimoVotoDoUsuario.jsonPath().getList("sub_id").getFirst());
        jsonPost.put("value", valorVoto);

        return listaUltimoVotoDoUsuario;
    }

    @Test(description = "POST - Deve somar um voto na imagem especificada")
    public void POSTDeveSomar1Voto() {
        // retorna voto mais atualizado de my-user-1234
        ResponseBody listaUltimoVotoDoUsuario = preparaCenarioAddRemVotos(1);

        given()
            .spec(VotesTest.requestSpecification())
            .body(jsonPost.toJSONString())
        .when()
            .log().all()
            .post(RestAssured.basePath)
        .then()
            .log().all()
            .statusCode(201)
            .body("message", equalTo("SUCCESS"))
            .body("image_id", equalTo(listaUltimoVotoDoUsuario.jsonPath().getList("image_id").getFirst()))
            .spec(
                RestAssured.expect().body("value",
                        equalTo(((Integer) listaUltimoVotoDoUsuario.jsonPath().getList("value").getFirst()).intValue()+1)
                )
            );
    }

    @Test(description = "POST - Deve subtrair 1 votos na imagem especificada")
    public void POSTDeveSubtrair1Voto() {
        // retorna voto mais atualizado de my-user-1234
        ResponseBody listaUltimoVotoDoUsuario = preparaCenarioAddRemVotos(-1);

        given()
            .spec(VotesTest.requestSpecification())
            .body(jsonPost.toJSONString())
        .when()
            .log().all()
            .post(RestAssured.basePath)
        .then()
            .log().all()
            .statusCode(201)
            .body("message", equalTo("SUCCESS"))
            .body("image_id", equalTo(listaUltimoVotoDoUsuario.jsonPath().getList("image_id").getFirst()))
            .spec(
                RestAssured.expect().body("value",
                        equalTo(((Integer) listaUltimoVotoDoUsuario.jsonPath().getList("value").getFirst()).intValue()-1)
                )
            );
    }

    @Test(description = "POST - Deve setar -5 voto na imagem especificada")
    public void POSTDeveSetarVotoValor5Negativo() {
        ResponseBody listaUltimoVotoDoUsuario = preparaCenarioAddRemVotos(-5);

        given()
            .spec(VotesTest.requestSpecification())
            .body(jsonPost.toJSONString())
        .when()
            .log().all()
            .post(RestAssured.basePath)
        .then()
            .log().all()
            .statusCode(201)
            .body("message", equalTo("SUCCESS"))
            .body("image_id", equalTo(listaUltimoVotoDoUsuario.jsonPath().getList("image_id").getFirst()))
            .spec(RestAssured.expect().body("value", equalTo(jsonPost.get("value"))));
    }

    @Test(description = "POST - Deve setar 5 votos positivos na imagem especificada")
    public void POSTDeveSetarVotosValor5Positivo() {
        ResponseBody listaUltimoVotoDoUsuario = preparaCenarioAddRemVotos(5);

        given()
            .spec(VotesTest.requestSpecification())
            .body(jsonPost.toJSONString())
        .when()
            .log().all()
            .post(RestAssured.basePath)
        .then()
            .log().all()
            .statusCode(201)
            .body("message", equalTo("SUCCESS"))
            .body("image_id", equalTo(listaUltimoVotoDoUsuario.jsonPath().getList("image_id").getFirst()))
            .spec(RestAssured.expect().body("value", equalTo(jsonPost.get("value"))));
    }
}
