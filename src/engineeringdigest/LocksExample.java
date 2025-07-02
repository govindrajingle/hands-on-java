package engineeringdigest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocksExample {
    public static void main(String[] args) {
        var bank = new BankAccount();
        var withdraw = new Runnable() {
            public void run() {
                bank.withdrawAmount(42);
            }
        };
        var threadOne = new Thread(withdraw, "Alex");
        var threadTwo = new Thread(withdraw, "Bob");
        threadOne.start();
        threadTwo.start();
    }
}

class BankAccount {
    int balance = 140;
    private final Lock lock = new ReentrantLock();

    void withdrawAmount(int amount) {
        boolean locked = false;
        try {
            locked = lock.tryLock(100, TimeUnit.MILLISECONDS);
            if (locked) {
                if (balance >= amount) {
                    Thread.sleep(200);
                    balance -= amount;
                    System.out.println(
                            "Available Balance given by thread name is " + Thread.currentThread().getName() + " is "
                                    + balance);
                } else {
                    System.out.println("Insufficient Balance");
                }
            } else {
                System.out.println("method is locked to " + Thread.currentThread().getName());
            }
        } catch (Exception e) {
            Thread.interrupted();
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }
}
