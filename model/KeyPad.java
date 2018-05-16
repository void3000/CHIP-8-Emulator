package emu.model;
/**
 * Copyright (C) 2017 Bitsqwit - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the Public license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the Public license with
 * this file. If not, please write to: keorapetse.finger@yahoo.com
 */
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;

public class KeyPad {
    public Map<KeyCode, Integer> keys;

    public boolean keyPressed;

    public int currentKey;

    public KeyPad() {
        keys = new HashMap<>();
    }

    public void initialize() {
         // Chip-8: => QWERTY keyboard:
         //
         // 1 2 3 C    1 2 3 4
         // 4 5 6 D    Q W E R
         // 7 8 9 E    A S D F
         // A 0 B F    Z X C V
        keys.put(KeyCode.DIGIT1, 1 );
        keys.put(KeyCode.DIGIT2, 2 );
        keys.put(KeyCode.DIGIT3, 3 );
        keys.put(KeyCode.DIGIT4, 4 );
        keys.put(KeyCode.Q     , 5 );
        keys.put(KeyCode.W     , 6 );
        keys.put(KeyCode.E     , 7 );
        keys.put(KeyCode.R     , 8 );
        keys.put(KeyCode.A     , 9 );
        keys.put(KeyCode.S     , 10);
        keys.put(KeyCode.D     , 11);
        keys.put(KeyCode.F     , 12);
        keys.put(KeyCode.Z     , 13);
        keys.put(KeyCode.X     , 14);
        keys.put(KeyCode.C     , 15);
        keys.put(KeyCode.V     , 16);
        keys.put(KeyCode.ESCAPE, 17);
        keys.put(KeyCode.F1    , 18);
    }

    public void onKeyPressHandler(KeyEvent keyEvent) {
        if (keys.containsKey(keyEvent.getCode())) {
            keyPressed = true;
            currentKey = keys.get(keyEvent.getCode());
        }
    }

    public void onKeyReleaseHandler(KeyEvent keyEvent) {
        keyPressed = false;
    }
}
