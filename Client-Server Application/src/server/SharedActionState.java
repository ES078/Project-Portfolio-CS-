import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;

public class SharedActionState {
    private final Lock lock = new ReentrantLock();
    private final HashMap<String, Integer> accounts = new HashMap<>();

    public SharedActionState() {
        accounts.put("A", 1000);
        accounts.put("B", 1000);
        accounts.put("C", 1000);
    }

    public String processTransaction(String clientID, String command) {
        lock.lock();
        try {
            String[] parts = command.split(" ");
            String action = parts[0];

            switch (action) {
                case "Add_money":
                    int amountAdd = Integer.parseInt(parts[1]);
                    accounts.put(clientID, accounts.get(clientID) + amountAdd);
                    return "Added " + amountAdd + " to account " + clientID;

                case "Subtract_money":
                    int amountSubtract = Integer.parseInt(parts[1]);
                    accounts.put(clientID, accounts.get(clientID) - amountSubtract);
                    return "Subtracted " + amountSubtract + " from account " + clientID;

                case "Transfer_money":
                    String toAccount = parts[1];
                    int transferAmount = Integer.parseInt(parts[2]);
                    if (clientID.equals(toAccount)) {
                        return "Error: Cannot transfer money to the same account.";
                    }
                    if (!accounts.containsKey(toAccount)) {
                        return "Error: Account " + toAccount + " does not exist.";
                    }
                    accounts.put(clientID, accounts.get(clientID) - transferAmount);
                    accounts.put(toAccount, accounts.get(toAccount) + transferAmount);
                    return "Transferred " + transferAmount + " from " + clientID + " to " + toAccount;

                case "Check_balance":
                    return "Balance for account " + clientID + " is " + accounts.get(clientID);

                default:
                    return "Unknown command";
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return "Invalid command format";
        } finally {
            lock.unlock();
        }
    }
}