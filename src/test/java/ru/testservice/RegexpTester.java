package ru.testservice;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class RegexpTester {
    @Test
    public void regexpTest(){
        assertTrue("173. Какие мероприятия из перечисленных".matches("^[0-9]{1,}[.]+$"));
    }
}
