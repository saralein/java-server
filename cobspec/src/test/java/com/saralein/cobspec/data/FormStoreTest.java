package com.saralein.cobspec.data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FormStoreTest {
    private FormStore formStore;
    private LinkedHashMap<String, String> data;

    @Before
    public void setUp() {
        formStore = new FormStore();
        data = new LinkedHashMap<String, String>() {{
            put("My", "Data");
        }};

        formStore.addData("/form", data);
    }

    @Test
    public void addsDataToStore() {
        HashMap<String, String> updatedData = formStore.retrieveData("/form");

        assertEquals(data, updatedData);
        assertTrue(updatedData.containsKey("My"));
        assertEquals("Data", updatedData.get("My"));
    }

    @Test
    public void determinesIfDataExistsForAnID() {
        assertFalse(formStore.dataExistsForID("/stuff"));
        assertTrue(formStore.dataExistsForID("/form"));
    }

    @Test
    public void deletesDataForAnID() {
        assertTrue(formStore.dataExistsForID("/form"));
        formStore.deleteData("/form");
        assertFalse(formStore.dataExistsForID("/form"));
    }
}