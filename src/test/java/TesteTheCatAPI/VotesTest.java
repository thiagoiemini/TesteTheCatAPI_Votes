package TesteTheCatAPI;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class VotesTest{

    public static Integer GETMaxLimitPadrao = 100;
    public static Integer GETMinLimitPadrao = 1;

    public enum GETtagOrdenacao{
        DESC("order=DESC"),
        ASC("order=ASC");

        public final String ordenacao;
        GETtagOrdenacao(String value) {
            this.ordenacao = value;
        }

        public String getOrdenacao() {
            return ordenacao;
        }
    }

    public enum basePaths {
        VOTES("/votes"),
        FACTS("/facts");
        public final String basePath;
        basePaths(String value) {
            this.basePath = value;
        }
        public String getBasePath() {
            return basePath;
        }
    }

    public static void setBasePath(String basePath) {
        RestAssured.basePath = RestAssured.baseURI + basePath;
    }

    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder().setBaseUri(RestAssured.baseURI)
//                .addQueryParam("version", "1.0")
                .addHeader("x-api-key", "DEMO-API-KEY")
                .addHeader("Content-Type","application/json")
                .build();
    }

    @BeforeClass
    public static void setup() {
        String baseHost = System.getProperty("server.host");
        if(baseHost == null){
            baseHost = "https://api.thecatapi.com/v1";
        }
        RestAssured.baseURI = baseHost;
    }
}
