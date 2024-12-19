package org.yearup.data.mysql;

import com.mysql.cj.protocol.Resultset;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.management.Query;
import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM categories;";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet results = statement.executeQuery();

            while ((results.next())) {
                Category category = mapRow(results);
                categories.add(category);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;

    }


    @Override
    public Category getById(int categoryId)
    {   Category category = null;
        try(Connection connection = getConnection()){

            String sql = "SELECT * FROM categories WHERE category_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql_id);



        statement.setInt(1, categoryId );
        ResultSet results = statement.executeQuery();

        while ((results.next())){

       category = mapRow(results);
        }
    }

    @Override
    public Category create(Category category) {
        // create a new category
        return null;
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
    }

    @Override
    public void delete(int categoryId) {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}

