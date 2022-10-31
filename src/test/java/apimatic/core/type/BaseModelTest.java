package apimatic.core.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.Test;
import apimatic.core.models.DeleteBody;
import io.apimatic.core.types.BaseModel;

public class BaseModelTest {

    @Test
    public void testAdditionalProperties() {
        BaseModel baseModel = new BaseModel();
        baseModel.setAdditionalProperties("version", "125");
        Object value = baseModel.getValue("version");
        assertNotNull(value);
    }

    @Test
    public void testGetValueUsingField() {
        BaseModel baseModel = new BaseModel();
        Object value = baseModel.getValue("additionalProperties");
        assertNotNull(value);
    }

    @Test
    public void testGetValueUsingMethod() {
        DeleteBody deleteBody = new DeleteBody.Builder("ali", "QA").build();
        Object value = deleteBody.getValue("getName");
        assertNotNull(value);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetValueUsingNonExistingMethod() {
        DeleteBody body = new DeleteBody.Builder("ali", "QA").build();
        body.getValue("getNameMethod");
    }

    @Test
    public void testSetValueUsingMethod() {
        DeleteBody body = new DeleteBody.Builder("ali", "QA").build();
        body.setValue("setName", "Ahmed");
        String expected = "Ahmed";
        Object actual = body.getValue("name");

        assertEquals(actual, expected);
    }

    @Test
    public void testSetValueUsingField() {
        Map<String, Object> additionalProperties = new HashMap<String, Object>();
        additionalProperties.put("testProperty", "testData");
        BaseModel baseModel = new BaseModel();
        baseModel.setValue("additionalProperties", additionalProperties);
        Object actual = baseModel.getValue("additionalProperties");

        assertEquals(actual, additionalProperties);
    }

    @Test
    public void testAdditionalPropertiesSetValue() {
        BaseModel baseModel = new BaseModel();
        baseModel.setAdditionalProperties("version", "125");
        baseModel.setValue("version", "126");
        Object value = baseModel.getValue("version");
        assertNotNull(value);
    }
}
