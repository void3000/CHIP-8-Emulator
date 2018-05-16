package emu.controller;
/**
 * Copyright (C) 2017 Bitsqwit - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the Public license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the Public license with
 * this file. If not, please write to: keorapetse.finger@yahoo.com
 */
import emu.Launcher;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class GUIController implements Initializable {

    @FXML
    private Canvas canvas;

    /**
     *  Initialization is triggered when the file {@code gui.fxml} is executed.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Launcher.gc = canvas.getGraphicsContext2D();
        Launcher.gc.setFill(Color.BLACK);
        Launcher.gc.fillRect(0,0, 640,320);
        Launcher.gc.scale(10, 10);
    }
}
