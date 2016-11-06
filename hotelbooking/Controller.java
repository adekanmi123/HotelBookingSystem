package hotelbooking;

import java.util.List;
import java.util.Map;

import static hotelbooking.Utils.printMessage;

public class Controller {

    static UserDAO userDAO = new UserDAO();
    static HotelDAO hotelDAO = new HotelDAO();

    private CurrentUser currentUser;

    public Controller() {
        this.currentUser = CurrentUser.getInstance();
    }

    User registerUser(User user) {
        user.setUserRegistered(true);
        return userDAO.save(user);
    }

    List<Hotel> findHotelByName(String name) {
        if (isUserRegistered()) {
            return hotelDAO.getByName(name);
        } else {
            return null;
        }
    }

    List<Hotel> findHotelByCity(String city) {
        if (isUserRegistered()) {
            return hotelDAO.getByCity(city);
        } else {
            return null;
        }
    }

    boolean bookRoom(long roomId, long userId, long hotelId) {
        if (isUserRegistered()) {
            User user = userDAO.getById(userId);
            return hotelDAO.bookRoom(user, hotelId, roomId);
        } else {
            return false;
        }
    }

    boolean cancelReservation(long roomId, long userId, long hotelId) {
        if (isUserRegistered()) {
            User user = userDAO.getById(userId);
            return hotelDAO.cancelRoomReservation(user, hotelId, roomId);
        } else {
            return false;
        }
    }

    //Example for map: city - Kiev, hotelName - Radisson, price - 200, persons - 2
    List<Hotel> findRoom (Map < String, String > params){
        String city;
        String hotelName;
        double price;
        int persons;

        if (!isUserRegistered()) {
            return null;
        }

        city = params.getOrDefault("city", null);
        hotelName = params.getOrDefault("hotelName", null);
        try {
            price = Double.valueOf(params.get("price"));
        } catch (NumberFormatException e) {
            price = 0;
        }
        try {
            persons = Integer.parseInt(params.get("persons"));
        } catch (NumberFormatException e) {
            persons = 0;
        }
        return hotelDAO.getRooms(city, hotelName, price, persons);
    }

    public boolean isUserRegistered() {
        User localCurrentUser = currentUser.getCurrentUser();
        boolean userRegistered = false;
        try {
            userRegistered = localCurrentUser.isUserRegistered();
        } catch (NullPointerException e) {
            printMessage("Try/Catch in isUserRegistered() method from the Controller class: NullPointerException! Current user is not set!");
            return userRegistered;
        }
        if (!userRegistered) {
            printMessage("Current user is not registered! Please, register user in the system! " + localCurrentUser);
        }
        return userRegistered;
    }

    List<User> getAllUsers() {
        if (isUserRegistered()) {
            return userDAO.getAll();
        } else {
            return null;
        }
    }

    List<Hotel> getAllHotels() {
        if (isUserRegistered()) {
            return hotelDAO.getAll();
        } else {
            return null;
        }
    }

    Hotel getHotelById(long hotelId) {
        if (isUserRegistered()) {
            return hotelDAO.getById(hotelId);
        } else {
            return null;
        }
    }
}
