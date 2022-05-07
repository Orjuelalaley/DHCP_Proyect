import java.io.Serial;

public class DHCPBadPacketException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DHCPBadPacketException() {}

    public DHCPBadPacketException(String message) {
        super(message);
    }

    public DHCPBadPacketException(String message, Throwable cause) {
        super(message, cause);
    }

    public DHCPBadPacketException(Throwable cause) {
        super(cause);
    }
}

