package dto;

import lombok.*;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@ToString

public class Category {
    Integer id;
    String title;
    ArrayList<Product> products;
}
