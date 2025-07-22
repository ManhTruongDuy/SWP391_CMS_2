package util;


import vn.payos.PayOS;

public class PayOSUtil {

    private static final String CLIENT_ID = "ae019f41-d3ea-4478-8a0e-836e499fd3b4";
    private static final String API_KEY = "c4918efa-f45a-400c-9366-60e8b0ce400a";
    private static final String CHECKSUM_KEY = "c7f21ba6d7bd0c254115e8f674ffcc370077d5392d88b4a7cfe1f69f6fba684c";

    private static final PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);

    public static PayOS getPayOS() {
        return payOS;
    }
}