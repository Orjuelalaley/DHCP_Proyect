public record Option(byte code, byte[] value) {

    // METODOS

    public static Option nuevaOpcion(byte code, byte value) {
        return new Option(code, Utils.byte2Bytes(value));
    }
}
