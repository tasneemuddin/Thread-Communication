class Data {
	int data;
	boolean valueSet = false;
	
	public synchronized void put(int data) {
		//wait until the value is consumed
		while(this.valueSet) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.data = data;
		System.out.println("put Data : " + this.data);
		this.valueSet = true;
		notify();//notify to wait
	}
	
	public synchronized void get() {
		//wait until the value is generated
		while(!this.valueSet) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("get Data : " + this.data);
		this.valueSet = false;
		notify();//notify to wait
	}
}

/***
 * Thread to produce the value
 * @author TASNEEM
 *
 */
class Producer implements Runnable {
	Data dataObj;
	
	public Producer(Data dataObj) {
		this.dataObj = dataObj;
		Thread t = new Thread(this, "Producer Thread");
		t.start();
	}
	
	@Override
	public void run() {
		int i = 0;
		while(true) {
			dataObj.put(++i);//generating new value
			try { Thread.sleep(500);}catch (Exception e) {}
		}
	}
}

/***
 * Thread to consume the value
 * @author TASNEEM
 *
 */
class Consumer implements Runnable {
	Data dataObj;
	
	public Consumer(Data dataObj) {
		this.dataObj = dataObj;
		Thread t = new Thread(this, "Consumer Thread");
		t.start();
	}
	
	@Override
	public void run() {
		while(true) {
			dataObj.get();//consuming the value
			try { Thread.sleep(2000);}catch (Exception e) {}
		}
	}
}

public class InterThread {

	public static void main(String[] args) {
		Data dataObj = new Data();
		new Producer(dataObj);
		new Consumer(dataObj);
	}

}
