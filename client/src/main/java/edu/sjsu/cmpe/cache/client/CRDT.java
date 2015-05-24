package edu.sjsu.cmpe.cache.client;
import java.util.*;

public class CRDT {
    public static void readRepair(CacheServiceInterface arg1, CacheServiceInterface arg2,
    	CacheServiceInterface arg3) throws Exception {
	long key = 1;
        String value = "a";
    	CacheServiceInterface nodeA  = arg1;
    	CacheServiceInterface nodeB  = arg2;
    	CacheServiceInterface nodeC  = arg3;
        
        nodeA.put(key, value);
        nodeB.put(key, value);
        nodeC.put(key, value);
        
        System.out.println("Value initialized: a");
        Thread.sleep(30000);
        
        nodeA.get(1);
	nodeB.get(1);
	nodeC.get(1);
	System.out.println("Value read: a");
	Thread.sleep(1000);
	    
	    System.out.println("Node A: " + nodeA.getValue());
	    System.out.println("Node B: " + nodeB.getValue());
	    System.out.println("Node C: " + nodeC.getValue());
        
        value = "b";
        nodeA.put(key, value);
        nodeB.put(key, value);
        nodeC.put(key, value);
        System.out.println("Value initialized: b");
        Thread.sleep(30000);
	        
	    nodeA.get(1);
	    nodeB.get(1);
	    nodeC.get(1);
	        
	    System.out.println("Value read: b");
	    Thread.sleep(1000);
	    
	    System.out.println("Node A: " + nodeA.getValue());
	    System.out.println("Node B: " + nodeB.getValue());
	    System.out.println("Node C: " + nodeC.getValue());
	        
	    String[] values = {nodeA.getValue(), nodeB.getValue(), nodeC.getValue()};
	    
	    Map<String, Integer> map = new HashMap<String, Integer>();
	    String max = null;
	    for (String x : values) {
	        Integer count = map.get(x);
	        map.put(x, count != null ? count+1 : 1);
	        if (map.get(x) > values.length / 2) {
	        	max = x;
	        	break;
	        }	
	    }
	    
	nodeA.put(key, max);
        nodeB.put(key, max);
        nodeC.put(key, max);
        System.out.println("Read on repair....");
	    Thread.sleep(1000);
	    
	    nodeA.get(key);
        nodeB.get(key);
        nodeC.get(key);
        
        System.out.println("After repair: b");
	    Thread.sleep(1000);
	    
	    System.out.println("Node A: " + nodeA.getValue());
	    System.out.println("Node B: " + nodeB.getValue());
	    System.out.println("Node C: " + nodeC.getValue());
    }
}
