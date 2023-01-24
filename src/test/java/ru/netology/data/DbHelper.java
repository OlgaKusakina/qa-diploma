package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import ru.netology.data.DbUtils.CreditRequestEntity;
import ru.netology.data.DbUtils.OrderEntity;
import ru.netology.data.DbUtils.PaymentEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNull;

public class DbHelper {

    private static String url = System.getProperty("db.url");
    private static String user = System.getProperty("db.user");
    private static String password = System.getProperty("db.password");

    public DbHelper() {
    }

    @SneakyThrows
    public static void cleanData() {
        QueryRunner runner = new QueryRunner();
        String cleanCreditRequest = "DELETE FROM credit_request_entity;";
        String cleanPayment = "DELETE FROM payment_entity;";
        String cleanOrder = "DELETE FROM order_entity;";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            runner.update(conn, cleanCreditRequest);
            runner.update(conn, cleanPayment);
            runner.update(conn, cleanOrder);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static PaymentEntity payData() {
        QueryRunner runner = new QueryRunner();
        String reqStatus = "SELECT * FROM payment_entity ORDER BY created DESC LIMIT 1;";


        PaymentEntity payData = new PaymentEntity();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            payData = runner.query(conn, reqStatus, new BeanHandler<>(PaymentEntity.class));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payData;
    }


    @SneakyThrows
    public static CreditRequestEntity creditData() {
        QueryRunner runner = new QueryRunner();
        String selectStatus = "SELECT * FROM credit_request_entity ORDER BY created DESC LIMIT 1;";

        CreditRequestEntity creditData = new CreditRequestEntity();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            creditData = runner.query(conn, selectStatus, new BeanHandler<>(CreditRequestEntity.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return creditData;
    }

    @SneakyThrows
    public static OrderEntity orderData() {
        var runner = new QueryRunner();
        var selectStatus = "SELECT * FROM order_entity ORDER BY created DESC LIMIT 1;";

        OrderEntity orderData = new OrderEntity();
        try (var conn = DriverManager.getConnection(url, user, password)) {
            orderData = runner.query(conn, selectStatus, new BeanHandler<>(OrderEntity.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderData;
    }

    @SneakyThrows
    public static void checkEmptyOrderEntity() {
        var runner = new QueryRunner();
        String orderRequest = "SELECT * FROM order_entity;";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            var orderBlock = runner.query(conn, orderRequest, new BeanHandler<>(OrderEntity.class));
            assertNull(orderBlock);
        }
    }


    @SneakyThrows
    public static void checkEmptyPaymentEntity() {
        var runner = new QueryRunner();
        var orderRequest = "SELECT * FROM payment_entity";

        try (var conn = DriverManager.getConnection(url, user, password)) {
            var paymentBlock = runner.query(conn, orderRequest, new BeanHandler<>(PaymentEntity.class));
            assertNull(paymentBlock);
        }
    }

    @SneakyThrows
    public static void checkEmptyCreditEntity() {
        var runner = new QueryRunner();
        var orderRequest = "SELECT * FROM credit_request_entity;";

        CreditRequestEntity creditBlock = new CreditRequestEntity();
        try (var conn = DriverManager.getConnection(url, user, password)) {
            creditBlock = runner.query(conn, orderRequest, new BeanHandler<>(CreditRequestEntity.class));
        } catch (SQLException e) {
            e.printStackTrace();
            assertNull(creditBlock);
        }
    }
}