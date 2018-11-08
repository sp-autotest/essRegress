package struct;

/**
 * Created by mycola on 08.11.2018.
 */
public class Soap {
    private String action;
    private String host;
    private String request;

    public Soap() {
    }

    public Soap(String action, String host, String request) {
        this.action = action;
        this.host = host;
        this.request = request;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
