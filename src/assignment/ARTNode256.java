package assignment;

public class ARTNode256 extends ARTNode {
    private ARTNode[] pointerArray = new ARTNode[256];
    private int size = 0;

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isFull() {
        return size > 255;
    }

    @Override
    public int getCapacity() {
        return 256;
    }

    public ARTNode getChild(char character) {
        return pointerArray[character];
    }

    public void setChild(char character, ARTNode child) {
        if (pointerArray[character] != null) throw new RuntimeException("256 node slot already full");
        pointerArray[character] = child;

        // increment size
        size++;
    }
}
