import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class ImageTests extends BaseTests {
    private final String PATH_TO_IMAGE = "src/test/resources/christmas.jpg";
    static String encodedFile;
    String uploadedFileImageId;
    String uploadedFileImageDeleteHash;
    String uploadedUrlImageId;
    String uploadedUrlImageDeleteHash;
    String uploadedBase64ImageId;
    String uploadedBase64ImageDeleteHash;

    @BeforeEach
    void beforeTest() {
        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);
    }

    @DisplayName ("1. Upload File")
    @Test
    void uploadFileTest() {
        Response response = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/christmas.jpg"))
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response();
        uploadedFileImageId = response.jsonPath().getString("data.id");
        uploadedFileImageDeleteHash = response.jsonPath().getString("data.deletehash");
    }

    @DisplayName ("2. Upload URL")
    @Test
    void uploadURLTest() {
        Response response = given()
                .headers("Authorization", token)
                .multiPart("image", "https://www.321.by/ii/image/100112000000sfr4331C2%20750.jpg")
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response();
        uploadedUrlImageId = response.jsonPath().getString("data.id");
        uploadedUrlImageDeleteHash = response.jsonPath().getString("data.deletehash");
    }

    @DisplayName ("3. Upload Base64")
    @Test
    void uploadBase64ImageTest() {
        Response response = given()
                .headers("Authorization", token)
                .multiPart("image", encodedFile)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response();
        uploadedBase64ImageId = response.jsonPath().getString("data.id");
        uploadedBase64ImageDeleteHash = response.jsonPath().getString("data.deletehash");
    }

  // 3 get imageUrl
    @DisplayName ("4. Get Image URL")
    @Test
    void getImageUrlTest() {
        given()
                .header("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}", uploadedBase64ImageId)
                .then()
                .statusCode(200)
                .extract()
                .response();
  }

    @DisplayName ("5. Get Image Base64")
    @Test
    void getImageBase64Test() {
        given()
                .header("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}", uploadedBase64ImageId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }


    @DisplayName ("6. Update Image Information Un-Authed Base64")
    @Test
    void updateImageInformationUnAuthedBase64() {
        given()
                .headers("Authorization", token)
                .param("title", "Christmas")
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image/{imageDeleteHash}", uploadedBase64ImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @DisplayName ("7. Update Image Information Un-Authed URL")
    @Test
    void updateImageInformationUnAuthedUrl() {
        given()
                .headers("Authorization", token)
                .param("title", "Christmastime")
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image/{imageDeleteHash}", uploadedUrlImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @DisplayName ("8. Update Image Information Authed URL")
    @Test
        void updateImageInformationAuthedUrl() {
            given()
                    .headers("Authorization", token)
                    .param("title", "Christmas Holiday")
                    .expect()
                    .statusCode(200)
                    .when()
                    .post("https://api.imgur.com/3/image/{imageHash}", uploadedUrlImageId)
                    .prettyPeek()
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();
        }

    @DisplayName ("9. Update Image Information Authed Base64")
    @Test
    void updateImageInformationAuthedBase64() {
      given()
              .headers("Authorization", token)
              .param("title", "Christmas Fun")
              .expect()
              .statusCode(200)
              .when()
              .post("https://api.imgur.com/3/image/{imageHash}", uploadedBase64ImageId)
              .prettyPeek()
              .then()
              .statusCode(200)
              .extract()
              .response();
  }


    @DisplayName ("10. Favorite An Image URL")
    @Test
    void favoriteAnImageURL() {
        given()
                .headers("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", uploadedUrlImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
  }

    @DisplayName ("11. Favorite An Image Base64")
    @Test
    void favoriteAnImageBase64() {
        given()
                .headers("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", uploadedBase64ImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
  }

/*
    @AfterAll
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "testprogmath", uploadedBase64ImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "testprogmath", uploadedUrlImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "testprogmath", uploadedFileImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{imageHash}", "testprogmath", uploadedUrlImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{imageHash}", "testprogmath", uploadedBase64ImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{imageHash}", "testprogmath", uploadedFileImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
*/

    private byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }


}