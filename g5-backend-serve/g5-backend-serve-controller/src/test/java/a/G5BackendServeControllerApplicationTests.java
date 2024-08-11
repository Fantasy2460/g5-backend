package a;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

@SpringBootTest
class G5BackendServeControllerApplicationTests {

    @Test
    void contextLoads() {
        String resource = "mybatis-config.xml";
        InputStream inputStream;
        try {
            inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession session = sqlSessionFactory.openSession();
            try {
                // 你可以执行一个简单的查询来验证连接，比如查询一个表是否存在等
                // 这里仅打开和关闭session作为连接测试
            } finally {
                session.close();
            }
            System.out.println("Database connection successful!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing MyBatis.", e);
        }
    }

}
