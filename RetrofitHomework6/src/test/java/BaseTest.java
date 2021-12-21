import db.dao.ProductsMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import retrofit2.Retrofit;
import service.CategoryService;
import service.ProductService;
import utils.DbUtils;
import utils.RetrofitUtils;

@Slf4j
public class BaseTest {
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;
    int productId;
    static ProductsMapper productsMapper;


    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
        productsMapper = DbUtils.getProductsMapper();
    }
}
