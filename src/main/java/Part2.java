import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

class LockThread{
    Map<String,String> map = new HashMap();

    public void checkLock(String table){
    boolean flag = true;
    while(flag){

       if(map.containsKey(table)){
             if(map.get(table).equalsIgnoreCase(Thread.currentThread().getName())){
                flag=false;

            }
            else if(!map.get(table).equalsIgnoreCase(Thread.currentThread().getName())){

                continue;
            }

        }
       else{
           map.put(table,Thread.currentThread().getName());
           flag=false;

       }
    }
    }

    public void releaseLocks(){
        map.clear();
    }
}

class TransactionLocalRemoteT1 implements Runnable {

    LockThread lock = new LockThread();
    DbConnections conn = new DbConnections();

    public void run(){

        try {

        Connection t1 = conn.createLocalConnection();
        Connection t2 = conn.createRemoteConnection();
        Statement stmt1 = t1.createStatement();
        Statement stmt2 = t2.createStatement();
        t1.setAutoCommit(false);
        t2.setAutoCommit(false);

        String sqlUpdate1 = "update customers set city = 'Scotia' where state = 'BA'";
        String sqlUpdate2 = "update customers set zip = 29307 where state = 'ES'";
        String sqlUpdateRemote1 = "update payments set payment_value = 100 where payment_installments = 10";
        String sqlDeleteRemote = "delete from product_category_name_translation where product_category_name = 'artes'";
        String sqlDeleteRemote2 = "delete from payments where payment_type = 'voucher'";

            lock.checkLock("customers");
        stmt1.execute(sqlUpdate1);
            lock.checkLock("customers");
        stmt1.execute(sqlUpdate2);
            lock.checkLock("payments");
        stmt2.execute(sqlUpdateRemote1);
            lock.checkLock("product_category_name_translation");
        stmt2.execute(sqlDeleteRemote);
            lock.checkLock("payments");
        stmt2.execute(sqlDeleteRemote2);
        t1.commit();
        t2.commit();
        lock.releaseLocks();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

class TransactionLocalRemoteT2 implements Runnable {

    LockThread lock = new LockThread();
    Map<String,String> map = new HashMap();
    String threadName = "";
    DbConnections conn = new DbConnections();

    public void run(){

        try {

            Connection t1 = conn.createLocalConnection();
            Connection t2 = conn.createRemoteConnection();
            Statement stmt1 = t1.createStatement();
            Statement stmt2 = t2.createStatement();
            t1.setAutoCommit(false);
            t2.setAutoCommit(false);

            String sqlUpdate1 = "update customers set city = 'rio' where state = 'RJ'";
            String sqlUpdate2 = "update customers set zip = 12345 where city = 'belo horizonte'";
            String sqlUpdateRemote1 = "update payments set payment_installments = 5 where payment_type = 'boleto'";
            String sqlUpdateRemote2 = "update product_category_name_translation set product_category_name_english = 'mentos' where product_category_name = 'alimentos'";
            String sqlDeleteRemote = "delete from product where product_name_length = 22";
        Thread.sleep(1000);
            lock.checkLock("customers");
            stmt1.execute(sqlUpdate1);
            lock.checkLock("customers");
            stmt1.execute(sqlUpdate2);
            lock.checkLock("payments");
            stmt2.execute(sqlUpdateRemote1);
            lock.checkLock("product_category_name_translation");
            stmt2.execute(sqlUpdateRemote2);
            lock.checkLock("product");
            stmt2.execute(sqlDeleteRemote);
            t1.commit();
            t2.commit();
            lock.releaseLocks();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Part2 {

    public static void main(String[] args) throws InterruptedException {

        TransactionLocalRemoteT1 lRemote1 = new TransactionLocalRemoteT1();
        TransactionLocalRemoteT2 lRemote2 = new TransactionLocalRemoteT2();

        Thread th1 = new Thread(lRemote1);
        Thread th2 = new Thread(lRemote2);

        th1.start();
        th1.join();
        th2.start();


    }
}
