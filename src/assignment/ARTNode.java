package assignment;

public abstract class ARTNode {

    abstract public int getSize();
    abstract public boolean isFull();
    abstract public int getCapacity();
    abstract public ARTNode getChild(char character);
    abstract public void setChild(char character, ARTNode child);
}
