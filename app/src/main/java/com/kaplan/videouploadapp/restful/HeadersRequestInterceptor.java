package com.kaplan.videouploadapp.restful;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * Created by Fatih Kaplan on 20/04/16.
 */
public class HeadersRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

//        request.getHeaders().setContentType(MediaType.MULTIPART_FORM_DATA);
//        request.getHeaders().set("content-type","form-data");


//        final Map<String, String> parameterMap = new HashMap<String, String>(4);
//        parameterMap.put("charset", "utf-8");
//        request.getHeaders().setContentType(
//                new MediaType("application", "json", parameterMap));
        return execution.execute(request, body);
    }
}
//        request.getHeaders().set("Accept",MediaType.MULTIPART_FORM_DATA_VALUE);
//        final Map<String, String> parameterMap = new HashMap<String, String>(4);
//        parameterMap.put("charset", "utf-8");
//        request.getHeaders().setContentType(
//                new MediaType("application","json", parameterMap));
//        request.getHeaders().setAccept(Arrays.asList(MediaType.MULTIPART_FORM_DATA));
//        return execution.execute(request, body);
//    }
//}