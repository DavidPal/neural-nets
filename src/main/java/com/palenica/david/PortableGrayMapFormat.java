package com.palenica.david;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class PortableGrayMapFormat {

    public static void saveAsPGM(final String fileName, byte[][] data) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.println("P2");
        writer.println(String.format("%d %d", data[0].length, data.length));
        writer.println("255");
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                writer.print(String.format("%3d ", 255 - Byte.toUnsignedInt(data[i][j])));
            }
            writer.println();
        }
        writer.close();
    }

}
