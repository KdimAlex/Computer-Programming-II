package prog04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayStackTest {

    @Test
    void push() {
        assert StackTest.test(new ArrayStack());
    }
}