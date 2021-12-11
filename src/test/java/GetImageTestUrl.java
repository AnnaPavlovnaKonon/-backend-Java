import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class GetImageTestUrl extends BaseTests {
    private RequestSpecification requestSpecificationWithUrl;
    private MultiPartSpecification urlMultipartSpec;
    private Response response;
    private String imageId;
    private String imageDeleteHash;

    @BeforeEach
    void beforeGetTest() {
        urlMultipartSpec = new MultiPartSpecBuilder(Endpoints.IMAGE_URL)
                .controlName("image")
                .build();

        requestSpecificationWithUrl = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(urlMultipartSpec)
                .build();

        response = given(requestSpecificationWithUrl, positiveResponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response();
        imageId = response.body().as(GetImagePojo.class).getData().getId();
        imageDeleteHash = response.jsonPath().getString("data.deletehash");
    }

    @DisplayName("4. Get Image URL")
    @Test
    void getImageUrlTest() {
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
}
