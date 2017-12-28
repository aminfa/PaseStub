package de.upb.pasestub;


import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class DeployTest{

    /**
     * Tests basic functionality.
     */
    @Test
    public void deployTest1() throws Exception{
        PaseInstance instance = new PaseInstance("localhost:5000");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("a", 5);
        parameters.put("b", 20);
        boolean success = instance.create("plainlib.package1.b.B", parameters);

        Assert.assertTrue(success);

        System.out.println(instance.getInstanceUrl());

        int a = (Integer) instance.getAttribute("a");
        Assert.assertEquals(a, 5);

        parameters = new HashMap<String, Object>();
        parameters.put("c", 2);
        int result = (Integer) instance.callFunction("calc", parameters);
        Assert.assertEquals(result, 45);
    }

    @Test
    public void deployTest_LinearRegression() throws Exception {
        PaseInstance instance = new PaseInstance("localhost:5000");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("normalize", true);
        boolean success = instance.create("sklearn.linear_model.LinearRegression", parameters);

        Assert.assertTrue(success && instance.isCreated());
        
        parameters.clear();
        double[][] X = {{0,0}, {1,1}, {2,2}};
        parameters.put("X", X);
        double [] y =  {0,1,2};
        parameters.put("y", y);
        instance.callFunction("fit", parameters);

        // You will have to know the structure of the return value:
        Map<String, Object> returnedMap = (Map<String,Object>) instance.getAttribute("coef_"); 
        ArrayList<Double> coef_ = (ArrayList<Double>) returnedMap.get("values");
        Assert.assertEquals(0.5, (double) coef_.get(0), 0.01);

        parameters.clear();
        double[][] X2 = {{0.5, 1}, {1, 0.5}};
        parameters.put("X", X2);
        Map<String, Object> returnedMap2 = (Map<String,Object>) instance.callFunction("predict", parameters); 
        ArrayList<Double> predictions = (ArrayList<Double>) returnedMap2.get("values");
        List<Double> expected = Arrays.asList(0.75, 0.75);
        assertThat(predictions, is(expected));
    }
}