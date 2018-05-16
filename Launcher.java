package emu;
/**
 * Copyright (C) 2017 Bitsqwit - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the Public license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the Public license with
 * this file. If not, please write to: keorapetse.finger@yahoo.com
 */
import emu.model.Chip8CentralProcessingUnit;
import emu.model.KeyPad;
import emu.model.Memory;
import emu.model.Renderer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Launcher extends Application {
    public Chip8CentralProcessingUnit chip8CentralProcessingUnit;
    public static GraphicsContext gc;
    public Memory memory;
    public KeyPad keyPad;
    public Renderer renderer;

    @Override
    public void start(Stage primaryStage) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("view/gui.fxml"));
        Scene scene = new Scene(root, 630, 300);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Chip 8 Emulator!");
        primaryStage.show();

        keyPad = new KeyPad();
        memory = new Memory(4096);
        renderer = new Renderer();
        chip8CentralProcessingUnit = new Chip8CentralProcessingUnit();

        keyPad.initialize();
        memory.initializeRam();
        chip8CentralProcessingUnit.connectMemory(memory);
        scene.setOnKeyPressed(keyPad::onKeyPressHandler);
        scene.setOnKeyReleased(keyPad::onKeyReleaseHandler);

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            loadROM(file.getAbsolutePath());
        }else {
            loadROM("src/emu/boot/Boot.ch8");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                long lastTick = System.nanoTime();
                long currentTick;
                double delta = 0;
                double ticks = 60.0;
                double ns = 1000000000 / ticks;
                while (true){
                    currentTick = System.nanoTime();
                    delta += (currentTick - lastTick)/ ns;
                    lastTick = currentTick;
                    try {
                        chip8CentralProcessingUnit.step();
                        chip8CentralProcessingUnit.pressed = keyPad.keyPressed;
                        chip8CentralProcessingUnit.key = chip8CentralProcessingUnit.pressed ? keyPad.currentKey : -1;
                        while(delta >= 1) {
                            if (chip8CentralProcessingUnit.bufferFlag) {
                                chip8CentralProcessingUnit.bufferFlag = false;
                                renderer.render(gc, chip8CentralProcessingUnit.pixels);
                            }
                            delta--;
                        }
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void loadROM(final String pathname) throws IOException {
        final FileInputStream in = new FileInputStream(pathname);
        for (int c, j = 0x200;(c = in.read()) != -1; ++j)
            memory.ram[j] = c;
        in.close();
    }

    public static void main(String[] args) {
        Launcher.launch(args);
        System.exit(0);
    }
}
