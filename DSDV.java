
package dsdv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Aistė Mackevičiūtė
 */
public class DSDV {


    private final List<Node1> nodeList;
   
    public DSDV() {
        nodeList = new ArrayList();
    }
  
    public boolean addNeighbours(String node1Id, String node2Id, int weight) throws Exception{
        Node1 node1 = getNode(node1Id);
        Node1 node2 = getNode(node2Id);
        if(nodeList.contains(node1) && nodeList.contains(node2)){
            node1.addNeighbours(node2, weight);
            node2.addNeighbours(node1, weight);
            node1.sendUpdates();
            node2.sendUpdates();
            return true;
        }else{
            return false;
        }
            
    }

    private Node1 getNode(String id){
        
        for(Node1 n : nodeList){
            if(n.getId().equals(id)){
                return n;
            }
        }
        return null;
    }
    
    public boolean addNode(String id){
        Node1 node = new Node1(id);
        if(getNode(id) == null){
           nodeList.add(node);
           return true;
        }else{
            return false;
        }
        
        
    }

    public List<Node1> getNodeList() {
        return nodeList;
    }
    
    public void print(){
        for(Node1 nd : nodeList){
            System.out.println(nd.getId());
            nd.print();
        }
    }
    
    public boolean removeLink(String idNodeFrom, String idNodeTo) throws Exception{
        
        Node1 nodeFrom =getNode(idNodeFrom);
        Node1 nodeTo = getNode(idNodeTo);
        if(nodeFrom.removeNeighbour(nodeTo)){
            if(nodeTo.removeNeighbour(nodeFrom)){
               nodeFrom.sendUpdates(); 
               nodeTo.sendUpdates();
               return true;  
            } 
            return false; 
        }else{
            return false;
        }
        
    }
    public boolean sendpackets(String start, String end){
        Node1 nodeStar = getNode(start);
        Node1 nodeEnd = getNode(end);
        if((nodeStar != null) && (nodeEnd != null)){
            if(nodeStar.sendpackets(end)){
            return true;
            }else{
            return false;
            }
        }
        return false;
    }
    
    public boolean removeLinks(String idNode) throws InterruptedException, Exception{
        Node1 node = getNode(idNode);
        
        if(node != null){
            HashMap <Node1, Integer> nodeMap = node.getNeighboursList();
            for(Iterator<HashMap.Entry<Node1, Integer>> it = nodeMap.entrySet().iterator(); it.hasNext(); ) {
             HashMap.Entry<Node1, Integer> entry = it.next();
                entry.getKey().removeNeighbour(node);
            }
            
            for(Iterator<HashMap.Entry<Node1, Integer>> it = nodeMap.entrySet().iterator(); it.hasNext(); ) {
                 HashMap.Entry<Node1, Integer> entry = it.next();
                 entry.getKey().sendUpdates();
                 entry.getKey().sendRemoveNode(idNode);
                it.remove();
            }
            nodeList.remove(node);
            return true;
        }else{
            return false;
        }
    }
    
   
    
   
}
