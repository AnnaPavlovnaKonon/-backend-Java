import com.github.javafaker.Faker;
import dto.Category;
import dto.Product;
import enums.CategoryType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import utils.DbUtils;


import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class ProductTests extends BaseTest{
    Faker faker = new Faker();
    Product product;
    private static String resource = "mybatisConfig.xml";
    int productId;

    @BeforeEach
    void setUp() {
        product = new Product()
                .withId(faker.idNumber().hashCode())
                .withTitle(faker.harryPotter().house())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FURNITURE.getTitle());
    }

    @Test
    void postProductTest() throws IOException {
        Integer countProductsBefore = DbUtils.countProducts(productsMapper);
        Response<Product> response = productService.createProduct(product).execute();
        Integer countProductsAfter = DbUtils.countProducts(productsMapper);
        assertThat(countProductsAfter, equalTo(countProductsBefore+1));
        assert response.body() != null;
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        productId = response.body().getId();
    }

    @Test
    void getCategoryByIdTest() throws IOException {
        Integer id = CategoryType.FURNITURE.getId();
        Response<Category> response = categoryService.getCategory(id).execute();
        assert response.body() != null;
        assertThat(response.body().getTitle(), equalTo(CategoryType.FURNITURE.getTitle()));
        assertThat(response.body().getId(), equalTo(id));
    }

    @Test
    void returnProductByIdTest() throws IOException {
        Integer id = product.getId();
        Response<Product> response = productService.getProduct(id).execute();
        assertThat(response.body().getId(), equalTo(id));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
    }

    @Test
    void modifyProductTest() throws IOException{
        Response<Product> response = productService.modifyProduct(product).execute();
        assertThat(response.body().getId(), equalTo(product.getId()));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
    }

    @Test
    void deleteProductTest() throws IOException{
        Integer id = product.getId();
        Response<Product> response = productService.deleteProduct(id).execute();
        assertThat(response.body().getId(), equalTo(id));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
    }

    @AfterEach
    void tearDown() throws IOException {
        Integer countProductsBefore = DbUtils.countProducts(productsMapper);
        Response<Product> response = productService.deleteProduct(productId).execute();
        Integer countProductsAfter = DbUtils.countProducts(productsMapper);
        assertThat(countProductsAfter, equalTo(countProductsBefore-1));
    }
}
