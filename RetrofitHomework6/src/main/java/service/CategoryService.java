package service;

import dto.Category;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.ArrayList;

public interface CategoryService {
    @GET("categories/{id}")
    Call<Category> getCategory(@Path("id") Integer id);
}
