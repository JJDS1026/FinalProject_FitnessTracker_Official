package madriaga.labs;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    private String uuid;
    private String usernames;
    private String passwords;
    public String getUuid(){return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public String getUsernames() {
        return usernames;
    }

    public void setUsernames(String usernames) {
        this.usernames = usernames;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", usernames='" + usernames + '\'' +
                ", passwords='" + passwords + '\'' +
                '}';
    }
}
