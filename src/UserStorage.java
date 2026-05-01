import java.io.*;
import java.util.*;

public class UserStorage {

    private static final String FILE = "users.txt";

    public static void saveUser(String name, String email, String number, String password) {
        try (FileWriter fw = new FileWriter(FILE, true)) {
            fw.write(name + "," + email + "," + number + "," + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateLogin(String email, String password) {
        try (Scanner sc = new Scanner(new File(FILE))) {

            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");

                if (data.length == 4) {
                    String fileEmail = data[1];
                    String filePass = data[3];

                    if (fileEmail.equals(email) && filePass.equals(password)) {
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("User file not found yet.");
        }

        return false;
    }
}