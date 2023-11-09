import Model.Location;
import Model.Place;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
public class _03_ApiTestPOJO {
    //POJO : JSON nesnesi : locationNesnesi

    @Test
    public void extractJsonAll_POJO(){
        //Ogrenci ogr1=new Ogrenci();
        Location locationNesnesi =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .extract().body().as(Location.class)//Location kalıbına göre
                ;//dönen body bilgisini al Location class kalıbıyla çevir

        System.out.println("locationNesnesi.getCountry() = " + locationNesnesi.getCountry());
        System.out.println("locationNesnesi.getPlaces() = " + locationNesnesi.getPlaces());

        for (Place p:locationNesnesi.getPlaces())
            System.out.println("p = " + p);


        // JSonaDonustur(locationNesnesi); developer bu şekilde dönüştürmüştü
        // Json.Serialise(locationNesnesi);  bende tersine deSerialize yaptım.
        // yani NESNE yi elde ettim.


    }//bunun için bir dependency ekliyoruz burda



    @Test
    public void extractPOJO_Soru()
    {
        // http://api.zippopotam.us/tr/01000  endpointinden dönen verilerden "Dörtağaç Köyü" ait bilgileri yazdırınız

        Location locationObject=
       given()
               .when()
               .get("http://api.zippopotam.us/tr/01000")
               .then()
               .extract().body().as(Location.class);

        for (Place p:locationObject.getPlaces()){
            if (p.getPlacename().equals("Dörtağaç Köyü"))
                System.out.println("p = " + p);
        }
    }



}



