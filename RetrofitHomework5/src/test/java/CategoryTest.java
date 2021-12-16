import com.github.javafaker.Faker;
import dto.Category;
import dto.Product;
import enams.CategoryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CategoryTest extends BaseTest{
    Category category;
    Product product;
    Faker faker = new Faker();

    @BeforeEach
    void beforeEach() {
        product = new Product()
                .withId(faker.idNumber().hashCode())
                .withTitle(faker.harryPotter().house())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FURNITURE.getTitle());

        category = new Category()
                .withId(CategoryType.FURNITURE.getId())
                .withTitle(CategoryType.FURNITURE.getTitle());
    }

    @Test
    void returnProductsTest() throws IOException {
        Response<ArrayList<Product>> response = productService.getProducts().execute();
/*        assertThat(response.body().getTitle, equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));*/
    }
}

