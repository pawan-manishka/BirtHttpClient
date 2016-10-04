import java.util.Iterator;

/**
 * Created by pawan on 8/26/16.
 */
public class StockData {

    static RestClient hp =new RestClient();

    public static void main(String args[]) throws Exception {

        hp.getHttpsClient();
        hp.getClientStuff(hp.client);
        print();
    }

    public static void print(){
        hp.history.isEmpty();
        // Use iterator to display contents of array list
        System.out.print("Original contents of al: ");
        Iterator itr = hp.history.iterator();
        while(itr.hasNext()) {
            Object element = itr.next();
            System.out.print(element + "\n");
        }

        System.out.print("\n\n"+hp.history.get(1));
    }
}
