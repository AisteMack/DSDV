
package dsdv;


import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Aistė Mackevičiūtė
 */
public class Node1 {
    
    private HashMap<String, String[]> nodeMap;  //String[] = next, cost, seqNo, install 
    private String id;
     
    private final HashMap<Node1, Integer> neighboursList;
    public String getId() {
        return id;
    }
 
    public HashMap<Node1, Integer> getNeighboursList() {
        return neighboursList;
    }
     
     public Node1(String id) {
        neighboursList = new HashMap<>();
        this.id = id;
        String[] info = new String[4]; //next, cost, seqNo, install 
        info[0] = id;
        info[1] = "0";
        info[2] = "0";
        info[3] = id;
        
        
        nodeMap = new HashMap<>();
        nodeMap.put(id, info);
    }
     
    public boolean sendUpdates() throws Exception{
      for(HashMap.Entry<Node1, Integer> map : neighboursList.entrySet()){
             if(!map.getKey().receiveUpdates(id, nodeMap)){
                 return false;
             }
         }
      return true;
     }
     
    public void addNeighbours(Node1 node, int weight){

            neighboursList.put(node, weight);
            String[] info = new String[4]; //next, cost, seqNo, install 
            info[0] = node.getId();
            info[1] = String.valueOf(weight);
            info[2] = "0";
            info[3] = node.getId();
            nodeMap.put(node.getId(), info);
        
    }
    
    public boolean removeNeighbour(Node1 node){
        
        if(neighboursList.containsKey(node)){
            neighboursList.remove(node);
            double inf = Double.POSITIVE_INFINITY;
            String[] routingTable = this.nodeMap.get(node.id);
            routingTable[1] = String.valueOf(inf);
            routingTable[2] = String.valueOf(Integer.parseInt(routingTable[2]) + 1);
            this.nodeMap.replace(node.id, routingTable);
            
            for(Iterator<HashMap.Entry<String, String[]>> it = this.nodeMap.entrySet().iterator(); it.hasNext(); ) {
                HashMap.Entry<String, String[]> entry = it.next();
                String[] routingTable1 = entry.getValue();
                String next = routingTable1[0];
                if(next.equals(node.id) && !this.hasNeighbour(entry.getKey())){
                    routingTable1[1] = String.valueOf(inf);
                    routingTable1[2] = String.valueOf(Integer.parseInt(routingTable1[2]));
                    this.nodeMap.replace(entry.getKey(), routingTable1);
                }else if(this.hasNeighbour(entry.getKey()) && next.equals(node.id)){
                    routingTable1[0] = entry.getKey();
                    routingTable1[1] = String.valueOf(this.getNeighboursWeight(entry.getKey()));
                    routingTable1[2] = String.valueOf(Integer.parseInt(routingTable1[2]));
                    this.nodeMap.replace(entry.getKey(), routingTable1);
                }
            }
            this.sendInfUpdates();
            return true;
        }
        return false;
    }
    
     private int getNeighboursWeight(String id){
         for(HashMap.Entry<Node1, Integer> map : neighboursList.entrySet()){
             Node1 temp = map.getKey();
             if(temp.getId().equals(id)){
                 return map.getValue();
             }
         }
         return -1;
     }
    
     private boolean hasNeighbour(String id){
         
         for(HashMap.Entry<Node1, Integer> map : neighboursList.entrySet()){
             Node1 temp = map.getKey();
             if(temp.getId().equals(id)){
                 return true;
             }
         }
         return false;
     }
      private Node1 getNeighbour(String id){
         
         for(HashMap.Entry<Node1, Integer> map : neighboursList.entrySet()){
             Node1 temp = map.getKey();
             if(temp.getId().equals(id)){
                 return temp;
             }
         }
         return null;
     }
      public void updateInf(String friendId, HashMap routingTableFriend){
          HashMap<String, String[]> tableFriend = routingTableFriend;
          boolean update = false;
           for (HashMap.Entry<String, String[]> entry : tableFriend.entrySet()){
            String destFriend = entry.getKey();     
            String[] valueFriend = entry.getValue();
            double weight = Double.parseDouble(valueFriend[1]);
            if(weight == Double.POSITIVE_INFINITY){
               String[] myRoutingTable = this.nodeMap.get(destFriend);
               if(myRoutingTable[0].equals(friendId) && !this.hasNeighbour(destFriend)){
                   myRoutingTable[1] = String.valueOf(Double.POSITIVE_INFINITY);
                   myRoutingTable[2] = String.valueOf(Integer.parseInt(myRoutingTable[2]));
                   this.nodeMap.replace(destFriend, myRoutingTable);
                   update = true;
               }else if(myRoutingTable[0].equals(friendId) && this.hasNeighbour(destFriend)){
                   myRoutingTable[0] = entry.getKey();
                   myRoutingTable[1] = String.valueOf(this.getNeighboursWeight(entry.getKey()));
                   myRoutingTable[2] = String.valueOf(Integer.parseInt(myRoutingTable[2]));
                   this.nodeMap.replace(entry.getKey(), myRoutingTable);
               }
            }
            
           }
           if(update){
               this.sendInfUpdates();
           }
          
      }
    public void sendInfUpdates(){
        for(HashMap.Entry<Node1, Integer> map : this.neighboursList.entrySet()){
            
             map.getKey().updateInf(this.id, this.nodeMap);
              
         }
    }
     public boolean receiveUpdates(String friendsId, HashMap friendsInfo) throws Exception{
         
         HashMap<String, String[]> friendsInfoo = friendsInfo;
         boolean needToSendUpdate = false;
         double weightToFriend = Double.parseDouble(this.nodeMap.get(friendsId)[1]);
         String nextToFriend = this.nodeMap.get(friendsId)[0];  
         
         for (HashMap.Entry<String, String[]> entry : friendsInfoo.entrySet()) {
            String destFriend = entry.getKey();     
            String[] valueFriend = entry.getValue();
            
            String nextHop = valueFriend[0];
            double weightFriendsToDestination = Double.parseDouble(valueFriend[1]);
            int seqNoFriend = Integer.parseInt(valueFriend[2]);
            
            if(nodeMap.containsKey(destFriend)){        
          
                String destThis = destFriend;           
                String[] valueThis = this.nodeMap.get(destThis);    
                double weightToDestNow = Double.parseDouble(valueThis[1]);   
                int seqNoThis = Integer.parseInt(valueThis[2]);     
              
                if(weightToDestNow != Double.POSITIVE_INFINITY          
                    && weightFriendsToDestination != Double.POSITIVE_INFINITY ){ 
                    if(weightToDestNow > (weightFriendsToDestination + weightToFriend)){ 
                        if(weightToDestNow < 0 && (weightFriendsToDestination + weightToFriend) < 0){
                            throw new Exception("Graph contains negative weight cycle");
                        }else{
                            valueThis[0] = nextToFriend;
                            valueThis[1] = String.valueOf(weightFriendsToDestination + weightToFriend);

                            if(seqNoThis < seqNoFriend){ 
                                valueThis[2] = String.valueOf(seqNoFriend); 
                            }else{
                                valueThis[2] = String.valueOf(seqNoThis + 1); 
                            }
                            nodeMap.replace(destThis, valueThis); 
                            needToSendUpdate = true;
                        }
                    
                    }else if(seqNoThis < seqNoFriend){
                        valueThis[2] = String.valueOf(seqNoFriend);
                        nodeMap.remove(destThis);
                        nodeMap.put(destThis, valueThis);
                        needToSendUpdate = true;
                    }
                    
                    
                 }else if(weightToDestNow == Double.POSITIVE_INFINITY  
                    && weightFriendsToDestination != Double.POSITIVE_INFINITY){ 
                        valueThis[0] = nextToFriend;    
                        valueThis[1] = String.valueOf(weightFriendsToDestination + weightToFriend);

                        if(seqNoThis < seqNoFriend){ 
                            valueThis[2] = String.valueOf(seqNoFriend);
                        }else{
                            valueThis[2] = String.valueOf(seqNoThis + 1);
                        }
                        nodeMap.replace(destThis, valueThis);
                        needToSendUpdate = true;
                }
            }else{
                String[] newStringFriends = new String[4];
                newStringFriends[0] = nextToFriend;
                newStringFriends[1] = String.valueOf(Double.parseDouble(valueFriend[1]) + weightToFriend);
                newStringFriends[2] = valueFriend[2]; 
                newStringFriends[3] = friendsId;
                nodeMap.put(destFriend, newStringFriends);
                needToSendUpdate = true;
            }
        } //end of for
        if(needToSendUpdate){
            this.sendUpdates();
        }
        
       return true; 
    }
     
     public void sendRemoveNode(String id){
            for(Iterator<HashMap.Entry<Node1, Integer>> it = this.neighboursList.entrySet().iterator(); it.hasNext(); ) {
             HashMap.Entry<Node1, Integer> entry = it.next();
            if(entry.getKey().id.equals(id)){
                it.remove();
            }else{
                entry.getKey().removeNode(id);
            }
         }
     }
     
     private void removeNode(String id){
         if(this.nodeMap.containsKey(id)){
            this.nodeMap.remove(id);
            this.sendRemoveNode(id);
         }
     }
     
    public boolean sendpackets(String end){
        if(Double.parseDouble(this.nodeMap.get(end)[1]) != Double.POSITIVE_INFINITY){
            if(!this.id.equals(end)){
                String[] nodeString = this.nodeMap.get(end);
                if(nodeString != null){
                    String next = nodeString[0];
                    Node1 nextNode = this.getNeighbour(next);
                    System.out.print(this.id + " " + nodeString[1] + " ");
                    nextNode.sendpackets(end);
                    return true;
                }else{
                   return false; 
                }

            }else{
                System.out.print(this.id);
                return true;
            }
        }else{
            return false;
        }
        
    }
         
    public void print(){;
        for (HashMap.Entry<String, String[]> entry : nodeMap.entrySet()) {
            String destFriend = entry.getKey();
            String[] valueFriend = entry.getValue();
            System.out.println(destFriend + " " + valueFriend[0] + " " + valueFriend[1] 
                    + " " + valueFriend[2] + " " + valueFriend[3]);
            
            
        }
    }
     
     
    
}
