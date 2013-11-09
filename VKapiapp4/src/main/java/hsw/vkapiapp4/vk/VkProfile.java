package hsw.vkapiapp4.vk;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class VkProfile extends GenericJson {

    static final String[] sColumnNames = {"id", "first_name", "last_name"};

    @Key
    public int id;

    @Key
    public String first_name = "";

    @Key
    public String last_name = "";

    public static String[] getColumnNames() {
        return sColumnNames.clone();
    }

    public VkProfile() {
    }

    public VkProfile(int id, String first_name, String last_name) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    @Override
    public String toString() {
        return full_name();
    }

    public String full_name() {
        return first_name + ' ' + last_name;
    }
}