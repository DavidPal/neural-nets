package com.palenica.david;

import java.io.IOException;
import java.io.InputStream;

public class IdxFormatDecoder {

    public enum DataType {
        UNSIGNED_BYTE,   // 1 byte
        SIGNED_BYTE,     // 1 byte
        SHORT,           // 1 byte
        INT,             // 4 bytes
        FLOAT,           // 4 bytes
        DOUBLE,          // 8 bytes
    }

    public static class Header {
        public final DataType dataType;
        public final byte dimension;
        public final int[] sizes;

        private Header(DataType dataType, byte dimension, int[] sizes) {
            this.dataType = dataType;
            this.dimension = dimension;
            this.sizes = sizes;
        }
    }

    public static byte[] readBytes(InputStream input, int size) throws IOException {
        byte[] data = new byte[size];
        int bytesRead = 0;
        while (bytesRead < size) {
            bytesRead += input.read(data, bytesRead, size - bytesRead);
        }
        return data;
    }

    public static int[] readInts(InputStream input, int size) throws IOException {
        final byte[] bytes = readBytes(input, 4*size);
        final int[] output = new int[size];
        for (int i = 0; i < size; i++) {
            output[i] = (Byte.toUnsignedInt(bytes[4*i + 0]) << 24) |
                        (Byte.toUnsignedInt(bytes[4*i + 1]) << 16) |
                        (Byte.toUnsignedInt(bytes[4*i + 2]) <<  8) |
                        (Byte.toUnsignedInt(bytes[4*i + 3]) <<  0);
        }
        return output;
    }

    public static Header decodeHeader(InputStream input) throws IllegalArgumentException, IOException {
        final byte[] bytes = readBytes(input, 4);

        // First two bytes must be zeros.
        if (bytes[0] != 0 || bytes[1] != 0) {
            throw new IllegalArgumentException("Invalid first two bytes");
        }

        // Third byte is the data type
        DataType dataType;
        switch (bytes[2]) {
            case 0x08: dataType = DataType.UNSIGNED_BYTE; break;
            case 0x09: dataType = DataType.SIGNED_BYTE; break;
            case 0x0B: dataType = DataType.SHORT; break;
            case 0x0C: dataType = DataType.INT; break;
            case 0x0D: dataType = DataType.FLOAT; break;
            case 0x0E: dataType = DataType.DOUBLE; break;
            default: throw new IllegalArgumentException("Invalid data type");
        }

        // Fourth byte is number of dimensions.
        final byte dimension = bytes[3];

        // For each dimension, there is a 32-bit integer, size along the dimension.
        final int[] sizes = readInts(input, dimension);

        return new Header(dataType, dimension, sizes);
    }

    public static byte[] decodeBytes1D(InputStream input) throws IllegalArgumentException, IOException {
        Header header = decodeHeader(input);
        if (header.dataType != DataType.UNSIGNED_BYTE && header.dataType != DataType.SIGNED_BYTE) {
            throw new IllegalArgumentException("BYTE data type expected");
        }
        if (header.dimension != 1) {
            throw new IllegalArgumentException("Dimension 1 expected");
        }

        final byte[] output = readBytes(input, header.sizes[0]);
        return output;
    }

    public static byte[][][] decodeBytes3D(InputStream input) throws IllegalArgumentException, IOException {
        Header header = decodeHeader(input);
        if (header.dataType != DataType.UNSIGNED_BYTE && header.dataType != DataType.SIGNED_BYTE) {
            throw new IllegalArgumentException("BYTE data type expected, got " + header.dataType.toString());
        }

        if (header.dimension != 3) {
            throw new IllegalArgumentException("Dimension 3 expected");
        }

        final byte[][][] output = new byte[header.sizes[0]][header.sizes[1]][];

        for (int i = 0; i < header.sizes[0]; i++) {
            for (int j = 0; j < header.sizes[1]; j++) {
                output[i][j] = readBytes(input, header.sizes[2]);
            }
        }

        return output;
    }


}
