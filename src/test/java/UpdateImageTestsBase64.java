import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;

import static io.restassured.RestAssured.given;

public class UpdateImageTestsBase64 extends BaseTests {
    //private final String PATH_TO_IMAGE = "src/test/resources/christmas.jpg";
    static String encodedFile;
    private MultiPartSpecification base64MultipartSpec;
    private RequestSpecification requestSpecificationWithAuthWithBase64;
    private Response response;
    private String imageId;
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

        response = given(requestSpecificationWithAuthWithBase64, positiveResponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response();
        imageId = response.body().as(GetImagePojo.class).getData().getId();
        imageDeleteHash = response.jsonPath().getString("data.deletehash");
    }

    @DisplayName("6. Update Image Information Un-Authed Base64")
    @Test
        void updateImageInformationUnAuthedBase64() {
            given(requestWithAuth)
                    .param("title", "Christmas")
                    .expect()
                    .statusCode(200)
                    .when()
                    .post("/image/{imageDeleteHash}", imageDeleteHash)
                    .prettyPeek()
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();
        }

    @DisplayName ("7. Update Image Information Authed Base64")
    @Test
        void updateImageInformationAuthedBase64() {
            given(requestWithAuth)
                    .param("title", "Christmas Fun")
                    .expect()
                    .statusCode(200)
                    .when()
                    .post("/image/{imageHash}", imageId)
                    .prettyPeek()
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();
        }

    @DisplayName ("8. Favorite An Image Base64")
    @Test
        void favoriteAnImageBase64() {
            given(requestWithAuth)
                    .post("/image/{imageHash}/favorite", imageId)
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
