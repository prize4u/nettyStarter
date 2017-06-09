package s.im.message;

public enum MessageType {

    // server related
    SERVICE_REQ((byte) 0)
    , SERVICE_RESP((byte) 1)
    , ONE_WAY((byte) 2)
    , LOGIN_REQ((byte) 3)
    , LOGIN_RESP((byte) 4)
    , HEARTBEAT_REQ((byte) 5)
    , HEARTBEAT_RESP((byte) 6)

    // socketio client -> netty server
    , CLIENT_TO_SERVER_CHAT_REQ((byte) 7)
    , CLIENT_TO_SERVER_ACK((byte) 8)

    // netty server -> socketio client
    , SERVER_TO_CLIENT_CHAT_REQ((byte) 9)
    , SERVER_TO_CLIENT_ACK((byte) 10)



    ;
    private byte value;

    private MessageType(byte value) {
        this.value = value;
    }

    public byte value() {
        return this.value;
    }
}
