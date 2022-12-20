package basicRestAssured;


import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateProjectTest {

    @Test
    public  void  verifyCreateProject(){
        JSONObject body= new JSONObject();
        body.put("Content","Bootcamp");

        Response response=given()
                .auth()
                .preemptive()
                .basic("testapi@test.com","12345")
                .body(body.toString())
                .log().all()

        .when()
                .post("https://todo.ly/api/projects.json");


        response.then()
                .log().all()
                .statusCode(200)
                .body("Content",equalTo("Bootcamp"));

        int idProj = response.then().extract().path("Id");

        body.put("Content","Bootcampupdate");
        body.put("Icon",5);

        response=given()
                    .auth()
                    .preemptive()
                    .basic("testapi@test.com","12345")
                    .body(body.toString())
                    .log().all()

                .when()
                    .put("https://todo.ly/api/projects/"+idProj+".json");


        response.then()
                .log().all()
                .statusCode(200)
                .body("Icon",equalTo(5))
                .body("Content",equalTo("Bootcampupdate"));



    }


}
