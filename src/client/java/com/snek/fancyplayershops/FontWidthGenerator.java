package com.snek.fancyplayershops;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;








/**
 * A class that generates the width and height of each individual character.
 */
public abstract class FontWidthGenerator {
    private FontWidthGenerator() {}
    public static final String CLASS_NAME   = "FontSize";
    public static final String DIR_PATH     = "fancyplayershops/generated";
    public static final String FILE_PATH    = DIR_PATH + "/" + CLASS_NAME + ".java";
    public static final String PACKAGE_NAME = "com.snek.framework.generated";

    public static final int PARTS = 32;




    /**
     * Retrieves the width of every character in the extended ASCII and saves it in the output file.
     *     The output file is a ready-to-use Java class with methods that can be used to compute the width and height a String would have
     *     when rendered in-game as non-bold text through a TextDisplayEntity with transform scale 1.0f and no shearing.
     */
    public static void generate() {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        try {
            Files.createDirectories(FabricLoader.getInstance().getConfigDir().resolve(DIR_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Print the widths to the source file
        try(FileWriter f = new FileWriter(FabricLoader.getInstance().getConfigDir().resolve(FILE_PATH).toString())) {
            f.write(
                "package " + PACKAGE_NAME + ";\n" +
                "\n\n\n\n" +
                "public final class " + CLASS_NAME + "{\n" +
                "    private " + CLASS_NAME + "() {}\n" +
                "\n"+
                "    // This value identifies the amount of rendered text pixels that fit in a minecraft block\n" +
                "    public static final int TEXT_PIXEL_BLOCK_RATIO = 40;\n" +
                "\n"+
                "    // The list of widths\n" +
                "    private static final int[] w = new int[" + (Character.MAX_VALUE - 1) + "];\n" +
                "    static {\n"
            );
            for(int i = 0; i < PARTS; ++i) {
                f.write("        init_" + i + "();\n");
            }
            f.write("    }\n");


            // Iterate through all the other characters
            for(int i = 0; i < PARTS; ++i) {
                f.write("\n    private static final void init_" + i + "() {\n        ");
                for(int c0 = 32; c0 < Character.MAX_VALUE / PARTS; c0++) {
                    char c = (char)((Character.MAX_VALUE / PARTS) * i + c0);
                    f.write(String.format("w[%d] = 0x%01x", (int)c, c < 32 ? 0 : renderer.getWidth(String.valueOf(c))));
                    if(c < Character.MAX_VALUE - 1) {
                        f.write(";");
                        f.write(((c + 1) % 32 == 0) ? "\n        " : " ");
                    }
                }
                f.write("\n    }\n");
            }


            // Write string length function
            f.write("\n\n\n\n");
            f.write(
                """
                    /**
                     * Calculates the width a string would have when rendered.
                     * This includes the space between each character.
                     */
                    public static float getWidth(String s) {
                        int r = 0;
                        for(int i = 0; i < s.length(); ++i) {
                            final char c = s.charAt(i);
                            if(c >= Character.MAX_VALUE) r += 9;
                            else r += w[c];
                        }
                        return (float)r / TEXT_PIXEL_BLOCK_RATIO;
                    }
                """
            );


            //TODO actually calculate the height
            // Write string width function
            f.write("\n\n\n\n");
            f.write(
                """
                    /**
                     * Returns the height a line would have when rendered.
                     * This does NOT include the space between lines.
                     */
                    public static float getHeight() {
                        return 9f / TEXT_PIXEL_BLOCK_RATIO;
                    }
                """
            );


            f.write("}");
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Print output notice
        System.out.println("Character dimensions written to \"config/" + FILE_PATH + "\"");
    }
}
