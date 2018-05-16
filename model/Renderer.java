/**
 * Copyright (C) 2017 Bitsqwit - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the Public license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the Public license with
 * this file. If not, please write to: keorapetse.finger@yahoo.com
 */
package emu.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {
    public void render(GraphicsContext gc, int[] pixels) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 640, 320);
        gc.setFill(Color.rgb(39, 174, 96));
        for (int j = 0;j < 32; ++j)
            for (int i = 0; i < 64; ++i)
                if (pixels[(j * 64 + i)] != 0)
                    gc.fillRect(i, j, 1, 1);
    }
}
