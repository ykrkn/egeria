package org.odpi.openmetadata.accessservices.dataengine.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTServerException;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class DataEngineRESTClientTest {

    private static final String SERVER_URL = "https://localhost:9444";
    private static final String SERVER_NAME = "TestServer";
    private static final String USER_ID = "zebra91";
    private static final String GUID = "guid";
    private static final String EXTERNAL_SOURCE_NAME = "externalSourceName";

    @Mock
    private RESTClientConnector connector;

    private DataEngineRESTClient dataEngineRESTClient;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.openMocks(this);

        dataEngineRESTClient = new DataEngineRESTClient(SERVER_NAME, SERVER_URL);

        Field connectorField = ReflectionUtils.findField(DataEngineRESTClient.class, "clientConnector");
        if (connectorField != null) {
            connectorField.setAccessible(true);
            ReflectionUtils.setField(connectorField, dataEngineRESTClient, connector);
            connectorField.setAccessible(false);
        }

        dataEngineRESTClient.setExternalSourceName(EXTERNAL_SOURCE_NAME);
    }

    @Test
    public void upsertDatabase() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RESTServerException {
        GUIDResponse response = mockGUIDResponse();
        Database database = new Database();

        when(connector.callPostRESTCall(eq("upsertDatabase"), eq(GUIDResponse.class), anyString(), any(), any()))
                .thenReturn(response);

        dataEngineRESTClient.upsertDatabase(USER_ID, database);

        assertEquals(GUID, response.getGUID());
    }

    @Test
    public void upsertRelationalTable() throws RESTServerException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        GUIDResponse response = mockGUIDResponse();
        RelationalTable relationalTable = new RelationalTable();

        when(connector.callPostRESTCall(eq("upsertRelationalTable"), eq(GUIDResponse.class), anyString(), any(), any()))
                .thenReturn(response);

        dataEngineRESTClient.upsertRelationalTable(USER_ID, relationalTable);

        assertEquals(GUID, response.getGUID());
    }

    @Test
    public void upsertDataFile() throws RESTServerException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        GUIDResponse response = mockGUIDResponse();
        DataFile dataFile = new DataFile();

        when(connector.callPostRESTCall(eq("upsertDataFile"), eq(GUIDResponse.class), anyString(), any(), any()))
                .thenReturn(response);

        dataEngineRESTClient.upsertDataFile(USER_ID, dataFile);

        assertEquals(GUID, response.getGUID());
    }

    private GUIDResponse mockGUIDResponse() {
        GUIDResponse response = new GUIDResponse();
        response.setGUID(GUID);
        return response;
    }
}