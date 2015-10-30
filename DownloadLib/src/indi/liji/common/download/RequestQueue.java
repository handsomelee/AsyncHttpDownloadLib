package indi.liji.common.download;

import java.util.LinkedList;

class RequestQueue {
	
	private LinkedList<Request> queue = new LinkedList<Request>();
	public RequestQueue() {}
	
	public synchronized Request getRequest(){
		while (queue.size() == 0){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return queue.remove();
	}
	
	public synchronized void addRequest(Request request){
		queue.add(request);
		notifyAll();
	}
}
