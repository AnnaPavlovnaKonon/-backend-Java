import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;

public class UploadImageTests extends BaseTests {
    //private final String PATH_TO_IMAGE = "src/test/resources/christmas.jpg";
    static String encodedFile;
    private MultiPartSpecification base64MultipartSpec;
    private RequestSpecification requestSpecificationWithAuthWithBase64;
    private MultiPartSpecification urlMultipartSpec;
    private MultiPartSpecification fileMultipartSpec;
    private RequestSpecification requestSpecificationWithUrl;
    private RequestSpecification requestSpecificationWithFile;
    private String imageDeleteHash;


    @BeforeEach
    void beforeTest() {
        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);

        base64MultipartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(base64MultipartSpec)
                .build();

        urlMultipartSpec = new MultiPartSpecBuilder(Endpoints.IMAGE_URL)
                .controlName("image")
                .build();

        requestSpecificationWithUrl = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(urlMultipartSpec)
                .build();

        fileMultipartSpec = new MultiPartSpecBuilder(Endpoints.PATH_TO_IMAGE)
                .controlName("image")
                .build();

        requestSpecificationWithFile = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(fileMultipartSpec)
                .build();
    }

    @DisplayName("1. Upload File")
    @Test
    void uploadFileTest() {
        Response response = given(requestSpecificationWithFile, positiveResponseSpecification)
                .post("/upload")
                .prettyPeek()
                .then()
                .extract()
                .response();
        imageDeleteHash = response.jsonPath().getString("data.deletehash");
    }

    @DisplayName("2. Upload URL")
    @Test
    void uploadURLTest() {
        Response response = given(requestSpecificationWithUrl, positiveResponseSpecification)
                .post("/upload")
                .prettyPeek()
                .then()
                .extract()
                .response();
        imageDeleteHash = response.jsonPath().getString("data.deletehash");
    }

    @DisplayName("3. Upload Base64")
    @Test
    void uploadBase64ImageTest() {
        Response response = given(requestSpecificationWithAuthWithBase64, positiveResponseSpecification)
                .post("/upload") //.post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response();
        imageDeleteHash = response.jsonPath().getString("data.deletehash");
    }

    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("/account/{username}/image/{deleteHash}", username, imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    private byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(Endpoints.PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}