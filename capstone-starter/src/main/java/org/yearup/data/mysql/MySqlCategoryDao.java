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
    public Category getById(int categoryId) {
        Category category = null;
        try (Connection connection = getConnection()) {

            String sql = "SELECT * FROM categories WHERE category_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);


            statement.setInt(1, categoryId);
            ResultSet results = statement.executeQuery();

            if ((results.next())) {
                category = mapRow(results);
            } else {
                System.out.println("Error Retrieving Category");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public Category create(Category category) {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO categories VALUES (null,?,?);";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.execute();

            ResultSet gen_key = statement.getGeneratedKeys();

            if (gen_key.next()) {
                int id = gen_key.getInt(1);
                return new Category(id, category.getName(), category.getDescription());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public void update(int categoryId, Category category) {
        // update category
        try (Connection connection = getConnection()) {

            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE categories
                    SET name = ?, description = ?
                    WHERE category_id = ?
                    """);

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(int categoryId) {
        // delete category
        try (Connection connection = getConnection()) {

            PreparedStatement statement = connection.prepareStatement("""
                    DELETE FROM categories WHERE category_id = ?
                    """);

            statement.setInt(1, categoryId);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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