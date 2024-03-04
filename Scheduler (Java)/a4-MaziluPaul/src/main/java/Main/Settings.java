package Main;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Settings {


    private static Settings instance;

    private final String repoType;

    private final String repoFile1;

    private final String repoFile2;

    private Settings(String repoType, String repoFile1, String repoFile2) {
        this.repoType = repoType;
        this.repoFile1 = repoFile1;
        this.repoFile2 = repoFile2;
    }

    public String getRepoFile1() {
        return repoFile1;
    }

    public String getRepoFile2() {
        return repoFile2;
    }

    public String getRepoType() {
        return repoType;
    }

    private static Properties loadSettings() {
        try (FileReader fr = new FileReader("settings.properties")) {
            Properties settings = new Properties();
            settings.load(fr);
            return settings;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized Settings getInstance() {
        Properties properties = loadSettings();
        instance = new Settings(properties.getProperty("Repository"), properties.getProperty("Pacienti"), properties.getProperty("Programari"));
        return instance;
    }
}

