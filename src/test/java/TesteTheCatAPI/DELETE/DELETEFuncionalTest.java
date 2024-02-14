package TesteTheCatAPI.DELETE;

import TesteTheCatAPI.VotesTest;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;

public class DELETEFuncionalTest extends VotesTest {

    @BeforeTest
    public static void setup() {
        VotesTest.setup();
        VotesTest.setBasePath(VotesTest.basePaths.VOTES.getBasePath());
    }

    ResponseBody retornaVotoNullMaisAntigo() {

        // retorna voto mais atualizado de my-user-1234
        return given()
            .spec(VotesTest.requestSpecification())
            .log().all()
            .get(RestAssured.basePath+"?sub_id=&{limit}&{orderDESC}",
                "limit=1", GETtagOrdenacao.ASC.getOrdenacao())
            .getBody();
    }

    @Test(description = "DELETE vote_id - Deve excluir registro")
    public void DELETEVoteIdDeveExcluirRegistro() {
        ResponseBody listaVotoDoUsuario = retornaVotoNullMaisAntigo();

        var id = listaVotoDoUsuario.jsonPath().getList("id").getFirst();
        given()
            .spec(VotesTest.requestSpecification())
        .when()
            .log().all()
            .delete(RestAssured.basePath+"/{vote_id}", id)
        .then()
            .log().all()
            .statusCode(200)
            .body("message",equalTo("SUCCESS"));

        // garantir que o id registro foi excluído
        given()
            .spec(VotesTest.requestSpecification())
        .when()
            .log().all()
            .get(RestAssured.basePath+"/{vote_id}", id)
        .then()
            .log().all()
            .statusCode(404)
            .body(containsString("NOT_FOUND"));
    }

}
