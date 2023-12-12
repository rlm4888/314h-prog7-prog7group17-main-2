package assignment;

public class ARTNode48 extends ARTNode {
    private byte[] indirectionArray = new byte[256];
    private ARTNode[] pointerArray = new ARTNode[48];
    private byte pointerArraySize = 0;

    @Override
    public int getSize() {
        return pointerArraySize;
    }

    @Override
    public boolean isFull() {
        return pointerArraySize > 47;
    }

    @Override
    public int getCapacity() {
        return 48;
    }

    public ARTNode getChild(char character) {
        return pointerArray[indirectionArray[character]];
    }

    public void setChild(char character, ARTNode child) {
        if (pointerArraySize > 47) {
            throw new RuntimeException("48 node too big couldnt add node");
        }

        pointerArray[pointerArraySize] = child;
        indirectionArray[character] = pointerArraySize;

        pointerArraySize++;
    }

}
