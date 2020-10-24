import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


class Transaction {
    DbConnections conn1 = new DbConnections();
    DbConnections conn2 = new DbConnections();

    public void run() {
        try {
            Connection t1 = conn1.createLocalConnection();
            Connection t2 = conn2.createLocalConnection();
            Statement stmt1 = t1.createStatement();
            Statement stmt2 = t2.createStatement();
            t1.setAutoCommit(false);
            t2.setAutoCommit(false);
            String sqlT1 = "select * from customers where zip = 1151";
            String sqlT2 = "select * from customers where zip = 1151";
            String updateT1 = "update customers set city = \"T1 city\" where zip = 1151";
            String update = "update customers set city = \"T2 city\" where zip = 1151";
            stmt1.executeQuery(sqlT1);
            stmt2.executeQuery(sqlT2);
            stmt1.executeUpdate(updateT1);
            stmt2.executeUpdate(update);
            t2.commit();
            System.out.println("t2 commit done");
            t1.commit();
            System.out.println("t1 commit done");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

public class Part1 {
    public static void main(String[] args) {
        Transaction t = new Transaction();
        t.run();

    }
}
