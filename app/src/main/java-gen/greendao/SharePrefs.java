package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SHARE_PREFS.
 */
public class SharePrefs {

    private String key;
    private String value;

    public SharePrefs() {
    }

    public SharePrefs(String key) {
        this.key = key;
    }

    public SharePrefs(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
