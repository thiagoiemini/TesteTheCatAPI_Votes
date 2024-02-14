package TesteTheCatAPI.POST;

import TesteTheCatAPI.VotesTest;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;

public class POSTVotesValidarCamposTest extends VotesTest {
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

    void preencheJsonPost(Object image_id, Object sub_id, Object valorVoto) {
        if (image_id != null)
            jsonPost.put("image_id", image_id);
        if (sub_id != null)
            jsonPost.put("sub_id", sub_id);
        if (valorVoto != null)
            jsonPost.put("value", valorVoto);
    }

    ResponseBody retornaVotoMaisRecente(String sub_id) {

        // retorna voto mais atualizado de my-user-1234
        return given()
            .spec(VotesTest.requestSpecification())
            .log().all()
            .get(RestAssured.basePath+"?sub_id={sub_id}&{limit}&{orderDESC}",
                sub_id,"limit=1", GETtagOrdenacao.DESC.getOrdenacao())
            .getBody();
    }

    @Test(description = "POST Value String - Deve retornar INVALID_DATA")
    public void POSTValueStringDeveRetornarInvalido() {
        ResponseBody listaUltimoVotoDoUsuario = retornaVotoMaisRecente("demo-0.055034650525337162");

        preencheJsonPost(listaUltimoVotoDoUsuario.jsonPath().getList("image_id").getFirst(),
            listaUltimoVotoDoUsuario.jsonPath().getList("sub_id").getFirst(),
            "ashkahsjkd@#${js}");

        given()
            .spec(VotesTest.requestSpecification())
            .body(jsonPost)
        .when()
            .log().all()
            .post(RestAssured.basePath)
        .then()
            .log().all()
            .statusCode(400)
            .body(containsString("INVALID_DATA"));
    }

    @Test(description = "POST Limites Value - Deve retornar INVALID_DATA maxDoubleLimite+1 - 1.79769313486231570e+308 + 1")
    public void POSTLimiteMaxDoubleMais1DeveRetornarInvalido() {
        String sub_id = "demo-0.055034650525337162";
        String image_id = "132";

        given()
            .spec(VotesTest.requestSpecification())
            .body("{\n" +
                    "    \"image_id\": \""+ image_id +"\",\n" +
                    "    \"sub_id\": \"" + sub_id + "\",\n" +
                    "    \"value\": 1.79769313486231570e+309\n" +
                    "}")
        .when()
            .log().all()
            .post(RestAssured.basePath)
        .then()
            .log().all()
            .statusCode(400)
            .body(containsString("INVALID_DATA"));
    }

    @Test(description = "POST Limites Value - Deve setar maxDoubleLimite - 1.79769313486231570e+308")
    public void POSTLimiteMaxDoubleDeveSetarValue() {
        String sub_id = "my-user-1234";
        Object qtdeVotos = 1.79769313486231570e+308;
        ResponseBody listaUltimoVotoDoUsuario = retornaVotoMaisRecente(sub_id);

        preencheJsonPost(listaUltimoVotoDoUsuario.jsonPath().getList("image_id").getFirst(),
                listaUltimoVotoDoUsuario.jsonPath().getList("sub_id").getFirst(),
                qtdeVotos);

        given()
            .spec(VotesTest.requestSpecification())
            .body(jsonPost)
        .when()
            .log().all()
            .post(RestAssured.basePath)
        .then()
            .log().all()
            .statusCode(201)
            .body("message", equalTo("SUCCESS"))
            .body("image_id", equalTo(jsonPost.get("image_id")))
            .body("sub_id", equalTo(sub_id))
            .spec(RestAssured.expect().body("value", equalTo(qtdeVotos)));
    }

    @Test(description = "POST Limites Value - Deve setar minDoubleLimite - -1.79769313486231570e+308")
    public void POSTLimiteMinDoubleDeveSetarValue() {
        String sub_id = "my-user-1234";
        Object qtdeVotos = -1.79769313486231570e+308;
        ResponseBody listaUltimoVotoDoUsuario = retornaVotoMaisRecente(sub_id);

        preencheJsonPost(listaUltimoVotoDoUsuario.jsonPath().getList("image_id").getFirst(),
                listaUltimoVotoDoUsuario.jsonPath().getList("sub_id").getFirst(),
                qtdeVotos);

        given()
            .spec(VotesTest.requestSpecification())
            .body(jsonPost)
        .when()
            .log().all()
            .post(RestAssured.basePath)
        .then()
            .log().all()
            .statusCode(201)
            .body("message", equalTo("SUCCESS"))
            .body("image_id", equalTo(jsonPost.get("image_id")))
            .body("sub_id", equalTo(sub_id))
            .spec(RestAssured.expect().body("value", equalTo(qtdeVotos)));
    }

    @Test(description = "POST sem image_id - Deve requerer image_id")
    public void POSTDeveRequererImageID() {
        String sub_id = "demo-0.055034650525337162";

        preencheJsonPost(null, sub_id, 0);

        given()
            .spec(VotesTest.requestSpecification())
            .body(jsonPost)
        .when()
            .log().all()
            .post(RestAssured.basePath)
        .then()
            .log().all()
            .statusCode(400)
            .body(containsString("\"image_id\" is required"));
    }

    @Test(description = "POST sem value - Deve requerer image_id")
    public void POSTDeveRequererValue() {
        String sub_id = "demo-0.055034650525337162";

        preencheJsonPost("132", sub_id, null);

        given()
            .spec(VotesTest.requestSpecification())
            .body(jsonPost)
        .when()
            .log().all()
            .post(RestAssured.basePath)
        .then()
            .log().all()
            .statusCode(400)
            .body(containsString("\"value\" is required"));
    }

    @Test(description = "POST sem sub_id - Deve setar voto")
    public void POSTSemSubIdDeveSetarValue() {

        preencheJsonPost("132", null, 1);

        given()
            .spec(VotesTest.requestSpecification())
            .body(jsonPost)
        .when()
            .log().all()
            .post(RestAssured.basePath)
        .then()
            .log().all()
            .statusCode(201)
            .body("message", equalTo("SUCCESS"))
            .body("image_id", equalTo(jsonPost.get("image_id")))
            .body("sub_id", equalTo(null))
            .spec(RestAssured.expect().body("value", equalTo(jsonPost.get("value"))));
    }
}
