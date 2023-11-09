package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class _07_GoRestCommentTest {
    // GoRest Comment ı API testini yapınız.

    Faker randomCreater=new Faker();
    int commentID;
    RequestSpecification reqSpec;

    @BeforeClass
    public void setup(){
        baseURI ="https://gorest.co.in/public/v2/comments";
        reqSpec=new RequestSpecBuilder()
                .addHeader("Authorization","Bearer c2358b2bbfd6ae4a2f2d82f8a7963c14c58283eb89fbee57e6be963cbb61fb02")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void createComment(){

        String rndFullName=randomCreater.name().fullName();
        String rndEmail=randomCreater.internet().emailAddress();
        String body= randomCreater.lorem().paragraph();

        Map<String,String> newComment=new HashMap<>();
        newComment.put("post_id", "83962");
        newComment.put("name", rndFullName);
        newComment.put("email", rndEmail);
        newComment.put("body", body);

        commentID=
                given()
                        .spec(reqSpec)
                        .body(newComment)

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;

        System.out.println("commentID = " + commentID);
    }



    @Test(dependsOnMethods = "createComment")
    public void getCommentById(){
        given()
                .spec(reqSpec)

                .when()
                .get(""+commentID)

                .then()
                .log().body()
                .contentType(ContentType.JSON)
                .body("id", equalTo(commentID))
                ;
    }



    @Test(dependsOnMethods = "getCommentById")
    public void commentUpdate(){
        Map<String,String> updComment=new HashMap<>();
        updComment.put("name", "mustafa yumrutepe");

        given()
                .spec(reqSpec)
                .body(updComment)

                .when()
                .put(""+commentID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(commentID))
                .body("name", equalTo("mustafa yumrutepe"))
        ;
    }


    @Test(dependsOnMethods = "commentUpdate")
    public void deleteComment(){

        given()
                .spec(reqSpec)

                .when()
                .delete(""+commentID)

                .then()
                .log().body()
                .statusCode(204)
        ;
    }



    @Test(dependsOnMethods = "deleteComment")
    public void deleteCommentNegative(){

        given()
                .spec(reqSpec)

                .when()
                .delete(""+commentID)

                .then()
                .log().body()
                .statusCode(404)
        ;
    }

}
