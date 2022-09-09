package ru.netology.graphics.image;

public class ColorSchemaRealisation implements TextColorSchema {

    @Override
    public char convert(int color) {
        char[] schemaConvert = {'#', '$', '@', '%', '*', '+', '-', '.'};
        return schemaConvert[color / 32];
    }
}
