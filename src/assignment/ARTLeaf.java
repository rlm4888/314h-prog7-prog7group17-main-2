package assignment;

public class ARTLeaf extends ARTNode{
    private Page value;

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int getCapacity() {
        return 1;
    }

    @Override
    public ARTNode getChild(char character) {
        throw new RuntimeException("leaf node");
    }

    @Override
    public void setChild(char character, ARTNode child) {
        throw new RuntimeException("leaf node");
    }
}
