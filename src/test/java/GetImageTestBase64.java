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

public class GetImageTestBase64 extends BaseTests {
    //private final String PATH_TO_IMAGE = "src/test/resources/christmas.jpg";
    static String encodedFile;
    private MultiPartSpecification base64MultipartSpec;
    private RequestSpecification requestSpecificationWithAuthWithBase64;
    private Response response;
    private String imageId;
    private String imageDeleteHash;

    @BeforeEach
    void beforeGetTest() {
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
        //imageId = response.jsonPath().getString("data.id");
        imageId = response.body().as(GetImagePojo.class).getData().getId();
        imageDeleteHash = response.jsonPath().getString("data.deletehash");
    }

        @DisplayName("5. Get Image Base64")
        @Test
        void getImageBase64Test() {
            given(requestWithAuth)
                    .get("/image/{imageHash}", imageId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();
        }

        @AfterEach
        void tearDown() {
            given(requestWithAuth)
                    .delete("/account/{username}/image/{deleteHash}", username, imageDeleteHash)
                    .prettyPeek()
                    .then()
                    .statusCode(200);
            }

        private byte[] getFileContent () {
            byte[] byteArray = new byte[0];
            try {
                byteArray = FileUtils.readFileToByteArray(new File(Endpoints.PATH_TO_IMAGE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return byteArray;
        }
}
