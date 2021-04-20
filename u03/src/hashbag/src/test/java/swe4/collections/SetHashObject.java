package swe4.collections;

/**
 * Object which can be used to set flexible hashCodes and provoke collisions
 */
public class SetHashObject {

    final int hashCode;
    final boolean equals;

    public SetHashObject(int hashCode, boolean equals) {
        this.hashCode = hashCode;
        this.equals = equals;

    }

    public SetHashObject(int hashCode) {
        this(hashCode, true);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode()
                && obj instanceof SetHashObject
                && this.equals == ((SetHashObject) obj).equals;
    }
}
