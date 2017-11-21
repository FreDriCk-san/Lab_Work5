package tests;

import classes.Formatter;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

public class FormatterTest {

    @Test
    public void failFormat() {

        try {
            check(null, null, 123);
            Assertions.fail("Null format");
        } catch (NullPointerException e) {
        }

        try {
            check("", "{0}", null);
            Assertions.fail("Null params");
        } catch (NullPointerException e) {
        }

        try {
            check(null, "{1}", 123);
            Assertions.fail("Argumanet out range");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    @Test
    public void successFormat() {

        check("", "");
        check("", "", 123);
        check("123", "{0}", 123);
        check("123_#asd", "{0}_{1}#asd", 123, null);
        check("asd 123 asd", "asd {0} asd", 123);
        check("test", "{0}", "test");
        check("123.12", "{0}", 123.12);
        check("321 123", "{1} {0}", 123, 321);
        check("Age:3", "{0}", new TestClass(3));
    }

    private void check(String result, String format, Object... params) {

        String actual = Formatter.build(format, params);
        Assertions.assertEquals(result, actual);
    }


    public class TestClass {
        public int age;

        public TestClass(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Age:" + age;
        }
    }
}