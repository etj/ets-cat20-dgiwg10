package org.opengis.cite.cat20.dgiwg10;

import java.net.URI;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.opengis.cite.cat20.dgiwg10.util.CSWClient;
import org.opengis.cite.cat20.dgiwg10.util.ClientUtils;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.w3c.dom.Document;

import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;

/**
 * A supporting base class that sets up a common test fixture. These configuration methods are invoked before those
 * defined in a subclass.
 */
public class CommonFixture {

    /**
     * Root test suite package (absolute path).
     */
    public static final String ROOT_PKG_PATH = "/org/opengis/cite/cat20/dgiwg10/";

    /** A DOM document containing service metadata (OGC capabilities). */
    protected Document capabilitiesDoc;

    /**
     * A client component for interacting with a WFS.
     */
    protected CSWClient cswClient;

    /**
     * An HTTP request message.
     */
    protected Document requestDocument;

    /**
     * An HTTP response message.
     */
    protected ClientResponse response;

    /**
     * The response document parsed from the HTTP response.
     */
    protected Document responseDocument;

    /**
     * Initializes the common test fixture with a client component for interacting with HTTP endpoints.
     *
     * @param testContext
     *            The test context that contains all the information for a test run, including suite attributes.
     */
    @BeforeClass
    public void initCommonFixture( ITestContext testContext ) {
        Object obj = testContext.getSuite().getAttribute( SuiteAttribute.TEST_SUBJECT.getName() );
        if ( null == obj ) {
            throw new SkipException( "Test subject not found in ITestContext." );
        }
        this.capabilitiesDoc = (Document) obj;
        this.cswClient = new CSWClient( this.capabilitiesDoc );
    }

    @BeforeMethod
    public void clearMessages() {
        this.requestDocument = null;
        this.response = null;
        this.responseDocument = null;
    }

    /**
     * Obtains the (XML) response entity as a DOM Document. This convenience method wraps a static method call to
     * facilitate unit testing (Mockito workaround).
     *
     * @param response
     *            A representation of an HTTP response message.
     * @param targetURI
     *            The target URI from which the entity was retrieved (may be null).
     * @return A Document representing the entity.
     *
     * @see ClientUtils#getResponseEntityAsDocument
     */
    public Document getResponseEntityAsDocument( ClientResponse response, String targetURI ) {
        return ClientUtils.getResponseEntityAsDocument( response, targetURI );
    }

    /**
     * Builds an HTTP request message that uses the GET method. This convenience method wraps a static method call to
     * facilitate unit testing (Mockito workaround).
     *
     * @param endpoint
     *            A URI indicating the target resource.
     * @param qryParams
     *            A Map containing query parameters (may be null);
     * @param mediaTypes
     *            A list of acceptable media types; if not specified, generic XML ("application/xml") is preferred.
     * @return A ClientRequest object.
     *
     * @see ClientUtils#buildGetRequest
     */
    public ClientRequest buildGetRequest( URI endpoint, Map<String, String> qryParams, MediaType... mediaTypes ) {
        return ClientUtils.buildGetRequest( endpoint, qryParams, mediaTypes );
    }

}
