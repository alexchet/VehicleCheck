package com.alexchetcuti.azure.coursework;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMode;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveQueueMessageResult;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveSubscriptionMessageResult;

public class Common {

	public Common() {
		// TODO Auto-generated constructor stub
	}
	
	private static final Configuration SSCServiceBusConfig = ServiceBusConfiguration.configureWithSASAuthentication(
			"smartspeedcamera",
			"RootManageSharedAccessKey",
			"e9eZDy/Vj+gzxhTsM2ZMzNlliqpO305GT/KB+GPEvas=",
			".servicebus.windows.net"
			);

	public static ServiceBusContract serviceConnect()
	{
		ServiceBusContract service = ServiceBusService.create(SSCServiceBusConfig);
		return service;
	}
	
	public static boolean isVehicleStolen(String vehicleRegistration)
	{
	    try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return (Math.random() > 0.95);
	}
	
	public static boolean receiveVehicleMessages()
	{
		try
		{
			ServiceBusContract service = serviceConnect();
			
		    ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
		    opts.setReceiveMode(ReceiveMode.PEEK_LOCK);

	        ReceiveSubscriptionMessageResult  resultSubMsg =
	            service.receiveSubscriptionMessage("MainTopic", "VehiclesSpeeding", opts);
	        BrokeredMessage message = resultSubMsg.getValue();
	        if (message != null)
	        {
	            Vehicle vehicle = new Vehicle(
	            		VehicleType.valueOf(message.getProperty("vehicleType").toString()),
			    		message.getProperty("regPlate").toString(),
			    		Integer.parseInt(message.getProperty("velocity").toString()),
			    		Integer.parseInt(message.getProperty("cameraUniqueID").toString())
	            		);
	            
	            System.out.println(vehicle.getRegPlate() + ": " + isVehicleStolen(vehicle.getRegPlate()));
	            
	            // Delete message.
	            service.deleteMessage(message);
		        
	            return true;
	        }
		}
		catch (ServiceException e) {
		    System.out.print("ServiceException encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
		catch (Exception e) {
		    System.out.print("Generic exception encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
		
		return false;
	}
	
	public static Boolean receiveVehicleQueue()
	{
		try
		{
			ServiceBusContract service = serviceConnect();
		    ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
		    opts.setReceiveMode(ReceiveMode.PEEK_LOCK);
	
	        ReceiveQueueMessageResult resultQM =
	                 service.receiveQueueMessage("vehiclespeedingqueue", opts);
	        BrokeredMessage message = resultQM.getValue();
	        if (message != null && message.getMessageId() != null)
		    {
	        	byte[] b = new byte[200];
	            String s = null;
	            int numRead = message.getBody().read(b);
	            while (-1 != numRead)
	            {
	                s = new String(b);
	                s = s.trim();
	                numRead = message.getBody().read(b);
	            }
	            System.out.println(s + ": " + isVehicleStolen(s));

	            service.deleteMessage(message);
	            
	            return true;
	        }
	        
	        return false;
		}
		catch (ServiceException e) {
		    System.out.print("ServiceException encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
		catch (Exception e) {
		    System.out.print("Generic exception encountered: ");
		    System.out.println(e.getMessage());
		    System.exit(-1);
		}
		
		return false;
	}
}
