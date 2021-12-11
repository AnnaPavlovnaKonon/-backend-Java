import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UpdateImageTestsUrl extends BaseTests {
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

    @DisplayName("9. Update Image Information Un-Authed URL")
    @Test
    void updateImageInformationUnAuthedUrl() {
        given(requestWithAuth)
                .param("title", "Christmastime")
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

    @DisplayName ("10. Update Image Information Authed URL")
    @Test
    void updateImageInformationAuthedUrl() {
        given(requestWithAuth)
                .param("title", "Christmas Holiday")
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

    @DisplayName ("11. Favorite An Image URL")
    @Test
    void favoriteAnImageURL() {
        given(requestWithAuth)
                .post("/image/{imageHash}/favorite", imageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
