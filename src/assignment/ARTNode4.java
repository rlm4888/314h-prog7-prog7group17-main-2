package assignment;

import java.util.Arrays;

public class ARTNode4 extends ARTNode {
    private byte[] indirectionArray = new byte[4];
    private byte[] mismatchArray = new byte[4];
    private ARTNode[] pointerArray = new ARTNode[4];

    byte arraySizes = 0;

    @Override
    public int getSize() {
        return arraySizes;
    }

    @Override
    public boolean isFull() {
        return arraySizes > 3;
    }

    @Override
    public int getCapacity() {
        return 4;
    }

    public ARTNode getChild(char character) {
        // use some wacky shit to try to get java to optimize this garbage
        // consider making static global variables for the mismatchArrays somewhere else
        // would be more cache aligned/performant
        Arrays.fill(mismatchArray, (byte) character);
        int index = Arrays.mismatch(indirectionArray, mismatchArray);

        return pointerArray[index];
    }

    public void setChild(char character, ARTNode child) {
        if (arraySizes > 3) {
            throw new RuntimeException("oops you fucked up here");
        }

        indirectionArray[arraySizes] = (byte) character;
        pointerArray[arraySizes] = child;

        arraySizes++;
    }
}
