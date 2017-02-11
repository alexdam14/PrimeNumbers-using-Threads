import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class PrimeNumbers {

	public static Thread t1;
	public static String scan = new String();
	public static int totalElemente;
	public static int[] primes = new int[100000000];
	public static final LinkedBlockingQueue<Boolean> stop = new LinkedBlockingQueue<>();
	public static final LinkedBlockingQueue<Boolean> gata = new LinkedBlockingQueue<>();
	public static final LinkedBlockingQueue<Integer> toVerify = new LinkedBlockingQueue<>();
	public static int iterator;
	static Boolean lock = new Boolean(true);
	static Scanner sc;

	static boolean isPrime(int n) {
		if (n % 2 == 0)
			return false;
		for (int i = 3; i * i <= n; i += 2) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		sc = new Scanner(System.in);
		iterator = 0;
		totalElemente = 100000000;
		//stop.add(false);
		toVerify.add(2);
		primes[0] = 2;

		t1 = new Thread(new Runnable() {
			public void run() {
				for (int i = 3; i < totalElemente; i += 2) {
					try {
						if (stop.peek()!=null){
							return;
						}
						toVerify.put(i);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		for (int i = 0; i < 2; i++) {
		//	final int temp_i=i;
			new Thread(new Runnable() {
				public void run() {
					while (stop.peek() == null) {
						try {
							Object temp = toVerify.poll(100, TimeUnit.MILLISECONDS);
							if (temp!=null && isPrime((int)temp)) {
								synchronized (lock) {
									primes[iterator] = (int)temp;
									iterator++;
									//System.out.println("Fir " + temp_i + " nr prim: " + temp);
								}
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					gata.add(true);
				}
			}).start();
		}

		t1.start();

		sc.nextLine();
		stop.add(true);
		for (int i=0;i<2;i++){
			try {
				gata.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Total:" +iterator);
	}
}