package ch.nivisan.raincloud.serialization;

public class RCType {
    public static final byte UNKNOWN = 0;
    public static final byte BYTE = 1;
    public static final byte SHORT = 2;
    public static final byte CHAR = 3;
    public static final byte INT = 4;
    public static final byte LONG = 5;
    public static final byte FLOAT = 6;
    public static final byte DOUBLE = 7;
    public static final byte BOOLEAN = 8;
    public static final byte BITFIELD = 9;

    // Sizes as bytes
    public static final byte BYTE_SIZE = 1;
    public static final byte SHORT_SIZE = 2;
    public static final byte CHAR_SIZE = 2;
    public static final byte INT_SIZE = 4;
    public static final byte LONG_SIZE = 8;
    public static final byte FLOAT_SIZE = 4;
    public static final byte DOUBLE_SIZE = 8;
    public static final byte BOOLEAN_SIZE = 1;
    
    public static byte getSize(byte type) {
        switch (type) {
            case BYTE:      return 1;
            case SHORT:     return 2;
            case CHAR:      return 2;
            case INT:       return 4;
            case LONG:      return 8;
            case FLOAT:     return 4;
            case DOUBLE:    return 8;
            case BOOLEAN:   return 1;
            case BITFIELD:  return 1;
            default:
                assert(false);
                return -1;
        }
    }
}
