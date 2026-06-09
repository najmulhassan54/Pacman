import java.io.*;
import java.util.*;

public class UserStorage {

    private static final String FILE = "users.txt";

    public static void saveUser(String name, String email, String number, String password) {
        try (FileWriter fw = new FileWriter(FILE, true)) {
            fw.write(name + "," + email + "," + number + "," + password + ",0\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean emailExists(String email) {
        for (String[] data : readAll()) {
            if (data.length >= 4 && data[1].equalsIgnoreCase(email)) return true;
        }
        return false;
    }

    public static boolean validateLogin(String email, String password) {
        for (String[] data : readAll()) {
            if (data.length >= 4 &&
                data[1].equalsIgnoreCase(email) &&
                data[3].equals(password)) return true;
        }
        return false;
    }

    public static String[] getUser(String email) {
        for (String[] data : readAll()) {
            if (data.length >= 4 && data[1].equalsIgnoreCase(email)) return data;
        }
        return null;
    }

    public static void updateHighScore(String email, int newScore) {
        List<String[]> all = readAll();
        for (String[] data : all) {
            if (data.length >= 5 && data[1].equalsIgnoreCase(email)) {
                int current = 0;
                try { current = Integer.parseInt(data[4]); } catch (NumberFormatException ignored) {}
                if (newScore > current) data[4] = String.valueOf(newScore);
            }
        }
        writeAll(all);
    }

    public static int getHighScore(String email) {
        String[] data = getUser(email);
        if (data != null && data.length >= 5) {
            try { return Integer.parseInt(data[4]); } catch (NumberFormatException ignored) {}
        }
        return 0;
    }

    public static List<String[]> getTopScores(int limit) {
        List<String[]> all = readAll();
        all.sort((a, b) -> {
            int sa = a.length >= 5 ? tryParse(a[4]) : 0;
            int sb = b.length >= 5 ? tryParse(b[4]) : 0;
            return Integer.compare(sb, sa);
        });
        return all.subList(0, Math.min(limit, all.size()));
    }

    private static List<String[]> readAll() {
        List<String[]> list = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(FILE))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (!line.isEmpty()) list.add(line.split(",", -1));
            }
        } catch (Exception ignored) {}
        return list;
    }

    private static void writeAll(List<String[]> list) {
        try (FileWriter fw = new FileWriter(FILE, false)) {
            for (String[] row : list) fw.write(String.join(",", row) + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static int tryParse(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
}
