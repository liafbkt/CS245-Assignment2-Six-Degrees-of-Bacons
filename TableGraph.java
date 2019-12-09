import java.util.*;

public class TableGraph {
    private Hashtable<String, LinkedList<String>> Cast_List;

    public TableGraph() {
        Cast_List = new Hashtable<String, LinkedList<String>>();
    }

    /**
     * Check if the actor is in the Hashtable
     * @param key - name of the actor
     * @return - true if exists
     */
    public boolean isInList(String key) {
        return Cast_List.containsKey(key);
    }

    /**
     * Update the hashtable
     * Represent relationships between actors through linked list
     * @param casts - all the actors from the same movie
     */
    public void addEdge(String[] casts) {
        for (String actor : casts) {
            // add actor into the hash table
            if (!isInList(actor)) {
                LinkedList<String> temp = new LinkedList(Arrays.asList(casts));
                temp.remove(actor);
                Cast_List.put(actor, temp);
            } else {
                //update existed actor's linked list
                LinkedList<String> temp = Cast_List.get(actor);
                for (String cast : casts) {
                    if (!temp.contains(cast) || !actor.equals(cast)) {
                        temp.add(cast);
                    }
                }
                Cast_List.replace(actor, temp);
            }
        }
    }


    /**
     *
     * @param src - actor1
     * @param dst - acotr2
     * @return - an Array List recording the path between src and dst
     * @throws Exception
     */
    public ArrayList<String> CalculateShortestPath(String src, String dst) throws Exception {
        if(!isInList(src) || !isInList(dst)){
            throw new Exception();
        }
        ArrayList<String> path = new ArrayList<String>();
        LinkedList<String> queue = new LinkedList<String>();
        //ArrayList<String> visited = new ArrayList<String>();
        Hashtable<String,String> visited = new Hashtable<String, String>();
        queue.add(src);
        visited.put(src,src);
        while (queue.size() != 0){
            String s = queue.poll();
            // Base case
            if(s.equals(dst)){
                String name = s;
                while(!name.equals(src)){
                    path.add(name);
                    name = visited.get(name);
                }
                path.add(name);
                return path;
            }else{
                for(String name : Cast_List.get(s)){
                    if(!visited.containsKey(name)){
                        //works as name.next = s;
                        visited.put(name,s);
                        queue.add(name);
                    }
                }
            }
        }
        return path;
    }

}

