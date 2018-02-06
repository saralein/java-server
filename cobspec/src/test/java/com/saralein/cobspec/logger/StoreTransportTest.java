package com.saralein.cobspec.logger;

import com.saralein.cobspec.data.LogStore;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StoreTransportTest {
    private LogStore logStore;
    private LogTransport storeTransport;


    @Before
    public void setUp() {
        logStore = new LogStore();
        storeTransport = new StoreTransport(logStore);
    }

    @Test
    public void addsLogMessagesToStore() {
        storeTransport.log("Error\n");
        storeTransport.log("Another error");

        assertEquals("Error\nAnother error", logStore.retrieveLog());
    }
}