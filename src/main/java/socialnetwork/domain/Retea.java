package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;

//singleton
public class Retea {
    private List<Utilizator> users;
    private static Retea instance = null;

    private Retea(Iterable<Utilizator> users) {
        this.users = new ArrayList<>();
        for (Utilizator user: users) {
            this.users.add(user);
        }
    }
    public static Retea getInstance(Iterable<Utilizator> users){
        if(instance == null)
            instance = new Retea(users);
        return instance;
    }

    /**
     * adds an user to the network
     * @param user
     */
    public void addUser(Utilizator user){
        this.users.add(user);
    }

    /**
     * deletes an user from the network
     * @param user
     */
    public void deleteUser(Utilizator user){
        this.users.remove(user);
    }

    /**
     * Executes DFS from an user of the network
     * @param u
     * @param visited
     */
    private void DFSUtil(Utilizator u, boolean[] visited) {
        visited[users.indexOf(u)] = true;
        if(u.getFriends()!=null)
            for (Utilizator x : u.getFriends()) {
                if(!visited[users.indexOf(x)]) DFSUtil(x,visited);
            }
    }

    /**
     * Executes DFS from an user of the network and adds the first name to a list
     * @param u
     * @param visited
     * @param names
     */
    private void DFSList(Utilizator u , boolean[] visited, List<String> names){
        visited[users.indexOf(u)] = true;
        names.add(u.getFirstName());
        if(u.getFriends()!=null)
            for (Utilizator x : u.getFriends()) {
                if(!visited[users.indexOf(x)]) DFSList(x,visited,names);
            }
    }

    /**
     *
     * @return the number of connectec components
     */
    public int numberOfConnectedComponents() {
        int number=0;
        boolean[] visited = new boolean[users.size()];
        for(int v = 0; v < users.size(); ++v) {
            if(!visited[v]) {
                DFSUtil(users.get(v),visited);
                number = number + 1;
            }
        }
        return number;
    }

    /**
     *
     * @return a list with the names of the users in the largest community
     */
    public List<String> longestConnectedComponent(){
        List<String> usersList = null;
        boolean[] visited = new boolean[users.size()];
        for(int v = 0; v < users.size(); ++v) {
            if(!visited[v]) {
                List<String> list = new ArrayList<>();
                DFSList(users.get(v),visited,list);
                if(usersList == null)
                    usersList = list;
                else if(list.size() > usersList.size())
                    usersList = list;
            }
        }
        return usersList;
    }
}
