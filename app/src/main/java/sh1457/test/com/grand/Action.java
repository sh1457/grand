package sh1457.test.com.grand;

class Action {
    private static final String TAG = "Action";

    private int aid;
    private int gid;
    private String astring;

    Action() {
        this.aid=0;
        this.gid=0;
        this.astring=null;
    }

    Action(int gid) {
        this.aid=0;
        this.gid=gid;
        this.astring=null;
    }

    Action(int gid, int aid, String astring) {
        this.aid=aid;
        this.gid=gid;
        this.astring=astring;
    }

    int getID() {
        return this.aid;
    }

    void setID(int aid) {
        this.aid=aid;
    }

    String getString() {
        return this.astring;
    }

    void setString(String astring) {
        this.astring=astring;
    }
}
