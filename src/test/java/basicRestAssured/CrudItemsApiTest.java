package basicRestAssured;


import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CrudItemsApiTest {

    @Test
    public  void  verifyCreateProject(){

        //Get Token
        Response response=given()
                .auth()
                .preemptive()
                .basic("testapi@test.com","12345")
                .log().all()

        .when()
                .get("https://todo.ly/api/authentication/token.json");

        response.then()
                .log().all()
                .statusCode(200);
        String accessToken = response.then().extract().path("TokenString");


        //Create New Item
        String NewItem = "Item"+new Date().getTime();

        JSONObject body= new JSONObject();
        body.put("Content",NewItem);

       response=given()
                .header("Token",accessToken)
                .body(body.toString())
                .log().all()

                .when()
                    .post("https://todo.ly/api/items.json");

        response.then()
                    .log().all()
                    .statusCode(200)
                    .body("Content",equalTo(NewItem));

        int idItem = response.then().extract().path("Id");

        //Get item by ID

        response=given()
                    .header("Token",accessToken)
                    .log().all()
                .when()
                    .get("https://todo.ly/api/items/"+idItem+".json");

        response.then()
                    .log().all()
                    .statusCode(200)
                    .body("Id",equalTo(idItem));

        // Update item by id
        String UpdatedItem = "Updated"+NewItem;
        body.put("Checked","false");
        body.put("Content",UpdatedItem);

        response=given()
                    .header("Token",accessToken)
                    .body(body.toString())
                    .log().all()
                .when()
                    .put("https://todo.ly/api/items/"+idItem+".json");

        response.then()
                    .log().all()
                    .statusCode(200)
                    .body("Content",equalTo(UpdatedItem));


        // Delete item by id

        response=given()
                .header("Token",accessToken)
                .log().all()
                .when()
                    .delete("https://todo.ly/api/items/"+idItem+".json");

        response.then()
                .log().all()
                .statusCode(200)
                .body("Content",equalTo(UpdatedItem));
        //Response contain the details of the deleted item


    }


}
