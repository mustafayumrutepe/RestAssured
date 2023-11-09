import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _01_ApiTest {

    @Test
    public void test1() {
        given()
                // Hazırlık işlemleri kodları
                .when()
                // endpoint (url), metod u verip istek gönderiliyor
                .then()
        // assertion, test, data işlemleri
        ;
    }

    @Test
    public void statusCodeTest() {
        given()
                // hazırlık kısmı boş
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()  //dönen body (json data) , log().all() : gidip gelen  her şey
                .statusCode(200) // test kısmı oldugundan assertion status code 200 mü
        ;
    }


    @Test
    public void contentTypeTest() {

        given()
                // hazırlık kısmı boş
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // dönen body json data , log().all() : gidip gelen her şey
                .statusCode(200)  // test kısmı olduğundan assertion status code 200 mü
                .contentType(ContentType.JSON) // dönen datanın tipi JSON mu
        ;
    }


    @Test
    public void checkCountryInResponseBody() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)   //bu da bir assertion
                .body("country", equalTo("United States")) //bu da bir assertion
        //body nin country değişkeni "United States" eşit Mİ

        ;
    }



    @Test
    public void checkStateInResponseBody() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu doğrulayınız
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California"))  //places.state bunu yazınca
        ;                                                 //bütün state leri alır, [0] bu şekilde
    }                                                 // places dizisindeki sıfırıncı indeksi alıyoruz




    @Test
    public void checkHasItem() {
        // Soru : "http://api.zippopotam.us/tr/01000"  endpoint in dönen
        // place dizisinin herhangi bir elemanında  "Dörtağaç Köyü" değerinin
        // olduğunu doğrulayınız
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                //.log().body()
                //.log().all()
                .body("places.'place name'", hasItem("Dörtağaç Köyü"))  //'place name' yazarken tek
                .statusCode(200)                                    //tırnakla kullandıgımızın sebebi
                                                        //ifadenin içinde boşluk olodugu içindir
        ;
    }


    @Test
    public void bodyArrayHasSizeTest() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint in dönen
        // place dizisinin dizi uzunluğunun 1 olduğunu doğrulayınız.
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                //.log().all()
                .body("places",hasSize(1))  //.body("places.size()",equalTo(1)) de yazılabilir

        ;
    }
    //bunları hep body den aldıgımızı jsonpathfinder sitesine yapıştırıp neyi
    // hangi formsatta yazacağımızı ordan buluyoruz.




    @Test
    public void combiningTest() {  //hepsini beraber yapıyoruz
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                //.log().all()
                .statusCode(200)
                .body("places",hasSize(1))
                .body("places[0].state", equalTo("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))

        ;
    }




    @Test
    public void pathParamTest(){

        given()
                .pathParam("ulke","us")
                .pathParam("postaKod",90210)
                .log().uri() // request link çalışmadan önceki hali

                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKod}")

                .then()
                .statusCode(200)
        ;
    }



    @Test
    public void queryParamTest(){
        given()
                .param("page", 1)  //?page=1 şeklinde linke ekleniyor  // queryParam ile de kullanılabilir
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users") // ?page=1
                .then()
                .statusCode(200)
                .log().body()

        ;
    }



    @Test
    public void queryParamTest2(){
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

        for (int i = 1; i <= 10; i++) {

            given()
                    .param("page", i)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .statusCode(200)
                    //.log().body()
                    .body("meta.pagination.page", equalTo(i))
            ;
        }
    }





    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void setup(){
        baseURI = "https://gorest.co.in/public/v1";

        requestSpec=new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI)   // log().uri()
                .build();

        responseSpec=new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void requestResponseSpecificationn(){
        given()
                .param("page", 1)
                .spec(requestSpec)

                .when()
                .get("/users") // Başında http yok ise baseUri baş tarafına gelir.

                .then()
                .spec(responseSpec)
        ;
    }





}
