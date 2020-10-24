
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DbConnections {
    public Connection createLocalConnection() throws SQLException {
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        String localUrl = "jdbc:mysql://localhost:3306/local_database";
        Connection localCon = DriverManager.getConnection(localUrl, "root", "root");


        return localCon;
    }

    public Connection createRemoteConnection() throws SQLException {
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        String remoteUrl = "jdbc:mysql://35.244.63.63:3306/remote_database";
        Connection remoteCon = DriverManager.getConnection(remoteUrl, "root", "rootpassword");
        return remoteCon;
    }
}



