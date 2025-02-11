package com.vortexwolf.dvach.common.library;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

import android.content.Context;

import com.vortexwolf.dvach.common.Constants;
import com.vortexwolf.dvach.settings.ApplicationSettings;

public class ExtendedHttpClient extends DefaultHttpClient {
    private static final String TAG = "ExtendedHttpClient";

    private static final int SOCKET_OPERATION_TIMEOUT = 15 * 1000;

    private static final BasicHttpParams sParams;
    private static final ClientConnectionManager sConnectionManager;

    static {

        // Client parameters
        BasicHttpParams params = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, SOCKET_OPERATION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, SOCKET_OPERATION_TIMEOUT);
        ConnManagerParams.setTimeout(params, SOCKET_OPERATION_TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        HttpProtocolParams.setUserAgent(params, Constants.USER_AGENT_STRING);

        // HTTPS scheme registry
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        SSLSocketFactory ssf = SSLSocketFactory.getSocketFactory();
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        schemeRegistry.register(new Scheme("https", ssf, 443));

        // Multi threaded connection manager
        sParams = params;
        sConnectionManager = new ThreadSafeClientConnManager(params, schemeRegistry);

    }

    public ExtendedHttpClient() {
        super(sConnectionManager, sParams);

        this.addRequestInterceptor(new DefaultRequestInterceptor());
        this.addResponseInterceptor(new GzipResponseInterceptor());
    }

    /** Releases all resources of the request and response objects */
    public static void releaseRequestResponse(HttpRequestBase request, HttpResponse response) {
        if (response != null) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (Exception e) {
                    MyLog.e(TAG, e);
                }
            }
        }

        if (request != null) {
            request.abort();
        }
    }

    public static String getLocationHeader(HttpResponse response) {
        Header header = response.getFirstHeader("Location");
        if (header != null) {
            return header.getValue();
        }

        return null;
    }

    /** Adds default headers */
    private static class DefaultRequestInterceptor implements HttpRequestInterceptor {
        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            request.addHeader("Accept-Encoding", "gzip");
        }
    }

    /** Handles responces with the gzip encoding */
    private static class GzipResponseInterceptor implements HttpResponseInterceptor {
        @Override
        public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
            HttpEntity entity = response.getEntity();
            if (entity == null) return;
            Header header = entity.getContentEncoding();
            if (header == null) return;
            String contentEncoding = header.getValue();
            if (contentEncoding == null) return;

            if (contentEncoding.contains("gzip")) {
                response.setEntity(new GzipDecompressingEntity(response.getEntity()));
            }
        }
    }

    private static class GzipDecompressingEntity extends HttpEntityWrapper {
        public GzipDecompressingEntity(final HttpEntity entity) {
            super(entity);
        }

        @Override
        public InputStream getContent() throws IOException, IllegalStateException {
            // the wrapped entity's getContent() decides about repeatability
            InputStream wrappedin = wrappedEntity.getContent();
            return new GZIPInputStream(wrappedin);
        }

        @Override
        public long getContentLength() {
            // length of ungzipped content is not known
            return -1;
        }
    }

}
