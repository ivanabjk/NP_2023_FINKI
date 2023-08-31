package ChatSystem;

import java.util.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.TreeSet;

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String [] params = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs, params);
                    }
                }
            }
        }
    }

}

class ChatRoom{
    private final String name;
    private TreeSet<String> users;

    public ChatRoom(String name) {
        this.name = name;
        users = new TreeSet<>();
    }
    public void addUser(String username){
        users.add(username);
    }
    public void removeUser(String username){
        users.remove(username);
    }
    public boolean hasUser(String username){
        return users.contains(username);
    }
    public int numUsers(){
        return users.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name+"\n");
        if(users.isEmpty())
            sb.append("EMPTY\n");
        else{
            for(String user : users){
                sb.append(user+"\n");
            }
        }
        return sb.toString();
    }
}
class ChatSystem{
    TreeMap<String, ChatRoom> chatRooms;
    TreeSet<String> users;

    public ChatSystem() {
        chatRooms = new TreeMap<>();
        users = new TreeSet<>();
    }
    public void addRoom(String roomName){
        chatRooms.putIfAbsent(roomName, new ChatRoom(roomName));
    }
    public void removeRoom(String roomName){
        chatRooms.remove(roomName);
    }
    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if(!chatRooms.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        return chatRooms.get(roomName);
    }
    public String getUser(String userName) throws NoSuchUserException {
        if(!users.contains(userName))
            throw new NoSuchUserException(userName);
        return userName;
    }
    public void register(String userName){
        users.add(userName);
        LinkedList<ChatRoom> minUserRooms = new LinkedList<>();
        int min = Integer.MAX_VALUE;
        for(ChatRoom room : chatRooms.values()){
            if(room.numUsers() < min){
                minUserRooms = new LinkedList<ChatRoom>();
                min = room.numUsers();
            }
            if(room.numUsers() == min){
                minUserRooms.add(room);
            }
        }
        if(minUserRooms.isEmpty()) return;
        minUserRooms.getFirst().addUser(userName);
    }
    public void registerAndJoin(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        register(userName);
        joinRoom(userName, roomName);
    }
    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        chatRooms.get(getRoom(roomName)).addUser(getUser(userName));
    }
    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        chatRooms.get(getRoom(roomName)).removeUser(getUser(username));
    }
    public void followFriend(String username, String friend_username) throws NoSuchRoomException, NoSuchUserException {
        for( Map.Entry<String, ChatRoom> room : chatRooms.entrySet()){
            if(room.getValue().hasUser(getUser(friend_username)))
                joinRoom(getUser(username), room.getKey());
        }
    }

}
class NoSuchRoomException extends Exception{

    public NoSuchRoomException() {
    }

    public NoSuchRoomException(String s) {
        super(s);
    }
}
class NoSuchUserException extends Exception{
    public NoSuchUserException() {
    }

    public NoSuchUserException(String s) {
        super(s);
    }
}