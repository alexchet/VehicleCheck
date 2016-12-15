package com.alexchetcuti.azure.coursework;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("queue")) {
			System.out.println("Reading from queue");
		}
		
		int timeSleep = 1000;
		int timeSleepMax = 128000;
		boolean contVehiclesRead = true;
	    while(true)  {
	    	if (contVehiclesRead) {
	    		//if argument queue is passed the application read data from queue
	    		if (args.length > 0 && args[0].equals("queue")) {
		    		contVehiclesRead = Common.receiveVehicleQueue();
	    		} else {
	    			contVehiclesRead = Common.receiveVehicleMessages();
	    		}
	    		
	    		if (contVehiclesRead) timeSleep = 1000;
	    	} else {
	            System.out.println("I'm tired, guess I'll sleep for " + timeSleep/1000 + " second"
	            		+ ((timeSleep/1000) > 1 ? "s" : "") + "!");
	            
	            if (timeSleep < timeSleepMax) {
	            	timeSleep  = timeSleep * 2;
	            }
				contVehiclesRead = true;
	            try {
					Thread.sleep(timeSleep);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	}
}
