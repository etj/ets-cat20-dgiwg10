package org.opengis.cite.cat20.dgiwg10;

import javax.ws.rs.core.MediaType;

import org.opengis.cite.cat20.dgiwg10.util.XMLUtils;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.w3c.dom.Document;

import com.sun.jersey.api.client.ClientResponse;

/**
 * A listener that augments a test result with diagnostic information in the event that a test method failed. This
 * information will appear in the XML report when the test run is completed.
 */
public class TestFailureListener extends TestListenerAdapter {

    /**
     * Sets the "request" and "response" attributes of a test result. The value of these attributes is a string that
     * contains information about the content of an outgoing or incoming message: target resource, status code, headers,
     * entity (if present). The entity is represented as a String with UTF-8 character encoding.
     *
     * @param result
     *            A description of a test result (with a fail verdict).
     */
    @Override
    public void onTestFailure( ITestResult result ) {
        super.onTestFailure( result );
        Object instance = result.getInstance();
        if ( CommonFixture.class.isInstance( instance ) ) {
            CommonFixture fixture = CommonFixture.class.cast( instance );
            result.setAttribute( "request", getRequestMessageInfo( fixture.requestDocument ) );
            result.setAttribute( "response", getResponseMessageInfo( fixture.response, fixture.responseDocument ) );
        }
    }

    /**
     * Gets diagnostic information about a request message. If the request contains a message body, it should be
     * represented as a DOM Document node or as an object having a meaningful toString() implementation.
     *
     * @param requestDocument
     *            The XML request entity.
     * @return A string containing information gleaned from the request message.
     */
    String getRequestMessageInfo( Document requestDocument ) {
        if ( requestDocument != null ) {
            return XMLUtils.writeNodeToString( requestDocument );
        }
        return "No request document.";
    }

    /**
     * Gets diagnostic information about a response message.
     *
     * @param rsp
     *            An object representing an HTTP response message.
     * @param responseDocument
     *            The XML response entity.
     * @return A string containing information gleaned from the response message.
     */
    String getResponseMessageInfo( ClientResponse rsp, Document responseDocument ) {
        if ( null == rsp ) {
            return "No response message.";
        }
        StringBuilder msgInfo = new StringBuilder();
        msgInfo.append( "Status: " ).append( rsp.getStatus() ).append( '\n' );
        msgInfo.append( "Headers: " ).append( rsp.getHeaders() ).append( '\n' );
        if ( responseDocument != null && rsp.getType().isCompatible( MediaType.APPLICATION_XML_TYPE ) ) {
            msgInfo.append( XMLUtils.writeNodeToString( responseDocument ) );
            msgInfo.append( '\n' );
        }
        return msgInfo.toString();
    }

}
