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

import java.util.Random;

public class Chip8CentralProcessingUnit {
    public int key, DT, soundTimer;
    public int opCode, PC, I;

    private int x, y, n, nn, nnn;
    private int sp;

    public boolean bufferFlag, pressed;

    public Random rand;
    public Memory memory;

    public int[] V, R, stack, pixels;

    public Chip8CentralProcessingUnit() {
        rand    = new Random();
        R       = new int[8];
        V       = new int[16];
        stack   = new int[16];
        pixels  = new int[2048];
        PC      = 0x200;
    }

    /**
     * Adds an object of Memory class to Cpu object list.
     *
     * @param extMem
     */
    public void connectMemory(Memory extMem) { memory = extMem; }

    /**
     * Execute one instruction at a time.
     */
    public void step() {
        decodeInstruction(fetchInstruction());
        PC += 2;
        executeInstruction();
        if (DT > 0)
            DT--;
    }

    /**
     * Fetch an instruction from memory.
     *
     * @return An instruction {@int } fetched from memory.
     */
    public int fetchInstruction() {
        return (memory.ram[PC] << 8 ) | (memory.ram[PC + 1] & 0xff);
    }

    /**
     * Decodes an instruction and store results in the instruction register.
     *
     * @param instruction of {@int } type.
     */
    public void decodeInstruction(int instruction) {
        x       = (instruction >> 8 ) & 0x000f;
        y       = (instruction >> 4 ) & 0x000f;
        n       = (instruction      ) & 0x000f;
        nn      = (instruction      ) & 0x00ff;
        nnn     = (instruction      ) & 0x0fff;
        opCode  = (instruction >> 12) & 0xffff;
    }

    public void executeInstruction() {
        if (opCode == 0x00 &&  nnn == 0xe0  ) { for (int p = 0; p < pixels.length; ++p) pixels[p] = 0; bufferFlag = true; return; }
        if (opCode == 0x00 &&  nnn == 0xee  ) { PC = stack[--sp % 16]; return;}
        if (opCode == 0x01                  ) { PC = nnn; return; }
        if (opCode == 0x02                  ) { stack[sp++ % 16] = PC; PC = nnn; return;}
        if (opCode == 0x03                  ) { if (V[x] == nn) PC += 2; return; }
        if (opCode == 0x04                  ) { if (V[x] != nn) PC += 2; return; }
        if (opCode == 0x05 &&  n == 0x00    ) { if (V[x] == V[y]) PC += 2; return; }
        if (opCode == 0x06                  ) { V[x] = nn & 0xff; return; }
        if (opCode == 0x07                  ) { V[x] = (V[x] + nn) & 0xff; return; }
        if (opCode == 0x08 &&  n == 0x00    ) { V[x] =  V[y]; return; }
        if (opCode == 0x08 &&  n == 0x01    ) { V[x] |= V[y]; return; }
        if (opCode == 0x08 &&  n == 0x02    ) { V[x] &= V[y]; return; }
        if (opCode == 0x08 &&  n == 0x03    ) { V[x] ^= V[y]; return; }
        if (opCode == 0x08 &&  n == 0x04    ) { V[x] += V[y]; V[0xf] = (V[x] >> 8) & 1; V[x] &= 0xff; return; }
        if (opCode == 0x08 &&  n == 0x05    ) { V[0xf] = V[x] >= V[y] ? 1 : 0; V[x] -= V[y]; V[x] &= 0xff; return; }
        if (opCode == 0x08 &&  n == 0x06    ) { V[0xf] = V[x] & 0x1; V[x] = (V[x] >> 1) & 0xff;  return; }
        if (opCode == 0x08 &&  n == 0x07    ) { V[0xf] = V[x] >= V[y] ? 1 : 0; V[x] = (V[y] - V[x]) & 0xff; return; }
        if (opCode == 0x08 &&  n == 0x0e    ) { V[0xf] = (V[x] >> 7) & 1; V[x] <<=1; V[x] &= 0xff; return; }
        if (opCode == 0x09 &&  n == 0x00    ) { if (V[x] != V[y]) PC += 2; return; }
        if (opCode == 0x0a                  ) { I = nnn; return; }
        if (opCode == 0x0b                  ) { PC = nnn + V[0]; return; }
        if (opCode == 0x0c                  ) { V[x] = rand.nextInt(256) & nn; return; }
        if (opCode == 0x0d &&  n !=0        ) {
            for (int j = 0; j < n; ++j)
                for (int i = 0; i < 8; ++i){
                    if ((memory.ram[I + j] & (0x80 >> i)) != 0) {
                        V[0xf] = pixels[(V[x] + i + ((V[y] + j) * 64)) % 2048] & 0x1;
                        pixels[(V[x] + i + ((V[y] + j) * 64)) % 2048] ^= 0x1;
                    }
                }
            bufferFlag = true;
            return;
        }
        if (opCode == 0x0e && nn == 0x9e    ) { if ( key == V[x] ) PC += 2; return; } // needs fixing
        if (opCode == 0x0e && nn == 0xa1    ) { if ( key != V[x] ) PC += 2; return; } // needs fixing
        if (opCode == 0x0f && nn == 0x07    ) { V[x] = DT & 0x00ff; return; }
        if (opCode == 0x0f && nn == 0x0a    ) {  if (pressed) { V[x] = key & 0xff;  pressed = true; } else { PC -= 2;  } return; }  // needs fixing
        if (opCode == 0x0f && nn == 0x15    ) { DT = V[x] & 0x00ff; return; }
        if (opCode == 0x0f && nn == 0x18    ) { soundTimer = V[x] & 0x00ff; return; }
        if (opCode == 0x0f && nn == 0x1e    ) { I += V[x]; return; }
        if (opCode == 0x0f && nn == 0x29    ) { I = V[x] * 5; return; }
        if (opCode == 0x0f && nn == 0x33    ) {
            memory.ram[(I    ) & 0xfff] = ((V[x] / 100) % 10) & 0xf;
            memory.ram[(I + 1) & 0xfff] = ((V[x] / 10 ) % 10) & 0xf;
            memory.ram[(I + 2) & 0xfff] = ((V[x]      ) % 10) & 0xf;
            return;
        }
        if (opCode == 0x0f && nn == 0x55    ) { for (int p = 0; p <= x; ++p) memory.ram[I + p] = V[p] & 0xff; return; }
        if (opCode == 0x0f && nn == 0x65    ) { for (int p = 0; p <= x; ++p) V[p] = memory.ram[I + p] & 0xff; return; }

        System.out.format("Unknown Instruction #%04X\n", (opCode | nnn));
    }
}
