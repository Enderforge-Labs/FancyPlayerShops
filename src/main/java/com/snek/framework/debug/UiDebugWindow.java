package com.snek.framework.debug;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.joml.Vector2f;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;




public class UiDebugWindow extends JPanel {
    private static UiDebugWindow w = null;
    public static UiDebugWindow getW() { return w; }
    static {
        if(DebugCheck.isDebug()) {
            w = new UiDebugWindow();
            JFrame frame = new JFrame("Ui Debug Window by UwU_Snek");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.add(w);
            w.setBackground(Color.BLACK);
            w.requestFocusInWindow();
            frame.setVisible(true);
        }
    }


    private final List<Vector2f> vertices = new ArrayList<>();
    public void add(Vector2f v) {
        vertices.add(v);
    }
    public void clear() {
        vertices.clear();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        // Draw rectangles
        g.setColor(Color.GREEN);
        for(int i = 0; i < vertices.size(); i += 4) {
            final int x1 = centerX - (int)(400 * vertices.get(i + 0).x);
            final int y1 = centerY - (int)(400 * vertices.get(i + 0).y);
            final int x2 = centerX - (int)(400 * vertices.get(i + 1).x);
            final int y2 = centerY - (int)(400 * vertices.get(i + 1).y);
            final int x3 = centerX - (int)(400 * vertices.get(i + 2).x);
            final int y3 = centerY - (int)(400 * vertices.get(i + 2).y);
            final int x4 = centerX - (int)(400 * vertices.get(i + 3).x);
            final int y4 = centerY - (int)(400 * vertices.get(i + 3).y);
            g.drawPolygon(new int[]{ x1, x2, x3, x4 }, new int[] {y1, y2, y3, y4 }, 4);
        }


        // Draw center
        g.setColor(Color.WHITE);
        g.drawLine(centerX, 0, centerX, height);
        g.drawLine(0, centerY, width, centerY);
    }
}