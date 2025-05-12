package com.snek.fancyplayershops;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.jetbrains.annotations.NotNull;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;








/**
 * A class that generates the width and height of each individual character.
 */
public abstract class FontWidthGenerator {
    private FontWidthGenerator() {}
    public static final @NotNull String PACKAGE_NAME = "com.snek.framework.generated";            // The name of the "generated package"
    public static final @NotNull String PACKAGE_PATH = "fancyplayershops/generated";              // The path to the "generated" package
    public static final @NotNull String CLASS_NAME   = "FontSize";                                // The name of the generated class
    public static final @NotNull String FILE_PATH    = PACKAGE_PATH + "/" + CLASS_NAME + ".java"; // The path to the generated class
    public static final          int    PARTS = 32;                                               // The number of methods to split the initialization into
    public static final          int    BREAK = 32;                                               // The maximum number of values to place in a single line
    public static final          int    SECTOR_SIZE  = (Character.MAX_VALUE + 1) / PARTS;




    /**
     * Retrieves the width of every character in the extended ASCII and saves it in the output file.
     * <p>
     * The output file is a ready-to-use Java class with methods that can be used to compute the width and height a String would have
     * when rendered in-game as non-bold text through a TextDisplay with transform scale 1.0f and no shearing.
     */
    public static void generate() {


        // Retrieve text renderer from the client instance and create the generated package
        final Font renderer = Minecraft.getInstance().font;
        try {
            Files.createDirectories(FabricLoader.getInstance().getConfigDir().resolve(PACKAGE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Print the widths to the source file
        try(FileWriter f = new FileWriter(FabricLoader.getInstance().getConfigDir().resolve(FILE_PATH).toString())) {


            // Write package, imports, class, fields and static initializer
            f.write(
                "package " + PACKAGE_NAME + ";\n" +
                "import org.jetbrains.annotations.NotNull;\n" +
                "\n\n\n\n" +
                "public final class " + CLASS_NAME + "{\n" +
                "    private " + CLASS_NAME + "() {}\n" +
                "\n"+
                "    // This value identifies the amount of rendered text pixels that fit in a minecraft block\n" +
                "    public static final int TEXT_PIXEL_BLOCK_RATIO = 40;\n" +
                "\n"+
                "    // The list of widths\n" +
                "    private static final int[] w = new int[" + (int)Character.MAX_VALUE + "];\n" +
                "    static {\n"
            );
            for(int i = 0; i < PARTS; ++i) {
                f.write("        init_" + i + "();\n");
            }
            f.write("    }\n");


            // Write initializer methods
            for(int i = 0; i < PARTS; ++i) {
                f.write("\n    private static void init_" + i + "() {\n");
                f.write("        final @NotNull int[] a = new int[] {\n            ");
                for(int c0 = 0; c0 < SECTOR_SIZE; ++c0) {
                    final char c = (char)(SECTOR_SIZE * i + c0);
                    if(c < Character.MAX_VALUE) {
                        f.write(String.format("0x%01x", c < 32 ? 0 : renderer.width(String.valueOf(c))));
                        f.write(",");
                        if(c0 < SECTOR_SIZE - 1) f.write(((c + 1) % BREAK == 0) ? "\n            " : " ");
                    }
                    else break;
                }
                f.write(String.format(
                    "\n        };" +
                    "\n        for(int i = 0; i < %d; ++i) w[%d + i] = a[i];\n",
                    SECTOR_SIZE - (i == PARTS - 1 ? 1 : 0),
                    SECTOR_SIZE * i
                ));
                f.write("    }\n");
            }


            // Write string length function
            f.write("\n\n\n\n");
            f.write(
                """
                    /**
                     * Calculates the width a string would have when rendered.
                     * <p> This includes the space between each character.
                     */
                    public static float getWidth(final @NotNull String s) {
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


            // Write string width function
            f.write("\n\n\n\n");
            f.write(
                "/**" +
                " * Returns the height a line would have when rendered.\n" +
                " * <p> This does NOT include the space between lines.\n" +
                " */\n" +
                "public static float getHeight() {\n" +
                "    return " + renderer.lineHeight + "f / TEXT_PIXEL_BLOCK_RATIO;\n" +
                "}\n"
            );


            f.write("}");
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Print output notice
        System.out.println("Character dimensions written to \"config/" + FILE_PATH + "\"");
    }
}
