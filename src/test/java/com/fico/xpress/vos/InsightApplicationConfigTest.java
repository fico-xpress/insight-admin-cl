package com.fico.xpress.vos;

import org.junit.Test;
import org.meanbean.test.BeanTester;

import static org.junit.Assert.assertTrue;

public class InsightApplicationConfigTest {
    @Test
    public void testGettersAndSetters(){
        BeanTester beanTester = new BeanTester();
        beanTester.testBean(InsightApplicationConfig.class);
    }

    @Test
    public void testConstructor(){
        InsightApplicationConfig insightApplicationConfig = new InsightApplicationConfig("uname", "pass");
        assertTrue("uname".equalsIgnoreCase(insightApplicationConfig.getUserName()));
        assertTrue("pass".equalsIgnoreCase(insightApplicationConfig.getPassword()));
    }
}
