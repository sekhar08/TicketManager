package TicketManager.service;

import TicketManager.entities.Train;
import TicketManager.entities.User;
import TicketManager.util.UserServiceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;
    private List<User> userList;
    private ObjectMapper objectmapper = new ObjectMapper();
    private static final String UserFilePath = "app/src/main/java/TicketManager/localdb/users.json";

    UserBookingService(User user) throws IOException {
        this.user = user;
        loadUser();

    }

    public UserBookingService() throws IOException{
        loadUser();
    }



    public List<User> loadUser() throws IOException{
        File users = new File(UserFilePath);
        return objectmapper.readValue(users, new TypeReference<List<User>>(){});
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public ObjectMapper getObjectmapper() {
        return objectmapper;
    }

    public void setObjectmapper(ObjectMapper objectmapper) {
        this.objectmapper = objectmapper;
    }

    public boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equalsIgnoreCase(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signup(User user1){
        try {
            userList.add(user1);
            saveUserToFile();
            return Boolean.TRUE;
        }
        catch(IOException err){
            return Boolean.FALSE;
        }
    }

    private void saveUserToFile() throws IOException {
        File UserFile = new File(UserFilePath);
        objectmapper.writeValue(UserFile, userList);
    }

    //Json --> Object (user) -> Deserialize
    //Object --> Json -> Serialize

    public void printTicket(){
        user.printTicket();
    }

    public Boolean cancelTrain(String ticketId) throws IOException {
        //todo: logic for cancelling train ticket here;
        Optional<User> foundUser = userList.stream().filter(u -> u.getName().equalsIgnoreCase(user.getName())).findFirst();
        if(foundUser.isPresent()){
            boolean removed = foundUser.get().removeTicket(ticketId);
            if(removed){
                saveUserToFile();
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


    public List<Train> getTrains(String sourceTrain, String destinationTrain) {
        // logic for getting the source and destination train
        TrainService trainService = new TrainService();
        return trainService.searchTrains(sourceTrain,destinationTrain);
    }
}