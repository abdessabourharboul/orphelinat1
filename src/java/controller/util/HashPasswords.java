package controller.util;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class HashPasswords {

    private static final HashPasswords instance = new HashPasswords();

    private HashPasswords() {
    }

    public static HashPasswords getInstance() {
        return instance;
    }

    public static String crypt(String password) {
        return Hashing.sha256().hashString(password, Charsets.UTF_8).toString();
    }

}
