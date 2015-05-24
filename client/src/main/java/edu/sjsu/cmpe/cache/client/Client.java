package edu.sjsu.cmpe.cache.client;

import java.util.concurrent.CountDownLatch;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
	private static CacheServiceInterface nodeA = null;
	private static CacheServiceInterface nodeB = null;
	private static CacheServiceInterface nodeC = null;
	
    public static void main(String[] args) {
    	try {
    		System.out.println("Server nodes initialization....");
    		
            nodeA = new DistributedCacheService("http://localhost:3000");
            nodeB = new DistributedCacheService("http://localhost:3001");
            nodeC = new DistributedCacheService("http://localhost:3002");
            
	    	if (args.length > 0) {
	    		if (args[0].equals("write")) {
	    			write();
	    		} else if (args[0].equals("read")) {
	    			CRDT.readRepair(nodeA, nodeB, nodeC);
	    		}
	    	}
	    	
	    	System.out.println("Exiting....");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}        
    }
    
    public static void write() throws Exception {       
        long key = 1;
        String value = "a";
        
        Future<HttpResponse<JsonNode>> node0 = nodeA.put(key, value);
        Future<HttpResponse<JsonNode>> node1 = nodeB.put(key, value);
        Future<HttpResponse<JsonNode>> node2 = nodeC.put(key, value);
        
        final CountDownLatch countDown = new CountDownLatch(3);
        try {
        	node0.get();
        } catch (Exception e) {
        } finally {
        	countDown.countDown();
        }
        
        try {
        	node1.get();
        } catch (Exception e) {
        } finally {
        	countDown.countDown();
        }
        
        try {
        	node2.get();
        } catch (Exception e) {
        } finally {
        	countDown.countDown();
        }

        countDown.await();
        
        if (DistributedCacheService.successCounter.intValue() < 2) {	        	
        	nodeA.delete(key);
        	nodeB.delete(key);
        	nodeC.delete(key);
        } else {
        	nodeA.get(key);
        	nodeB.get(key);
        	nodeC.get(key);
        	Thread.sleep(1000);
        	System.out.println("Node A: " + nodeA.getValue());
    	    System.out.println("Node B: " + nodeB.getValue());
    	    System.out.println("Node C: " + nodeC.getValue());
        }
        DistributedCacheService.successCounter = new AtomicInteger();
    }
}
