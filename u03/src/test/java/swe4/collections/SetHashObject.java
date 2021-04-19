package swe4.collections;

public class SetHashObject {

    final int hashCode;

    public SetHashObject(int haschCode) {
        this.hashCode = haschCode;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode() && obj instanceof SetHashObject;
    }
}
