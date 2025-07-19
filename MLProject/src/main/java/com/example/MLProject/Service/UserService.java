package com.example.MLProject.Service;

//import liquibase.database.Database;
import com.example.MLProject.Model.UserTable;
import com.example.MLProject.Repository.UserRepository;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
//import org.hibernate.boot.model.relational.Database;
import liquibase.database.Database;
import org.hibernate.boot.spi.MetadataBuildingOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepo;


    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private  DataSource dataSource;


    public UserTable saveMainUser(UserTable users){
       return userRepo.save(users);
    }

    public void createTenant(String tenant_name) throws Exception {
        String tenantDbName = "tenant_" + tenant_name;
        createDatabase(tenantDbName);
        addUserDataTable(tenantDbName, tenant_name);
    }

    private void createDatabase(String databaseName) throws Exception {
        String url = "jdbc:mysql://localhost:3306/";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + databaseName);
        }
    }

    private void addUserDataTable(String dbName, String tenant_name) throws Exception {
        String url = "jdbc:mysql://localhost:3306/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            DatabaseConnection liquibaseConnection = new JdbcConnection(connection);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(liquibaseConnection);

            String changelog_master_path = "db/changelog/changelog-master.xml";
            Liquibase liquibase = new Liquibase(changelog_master_path, new ClassLoaderResourceAccessor(), database);

            liquibase.setChangeLogParameter("tenantName", tenant_name);
            liquibase.update("insert-data");
        }
    }




}
