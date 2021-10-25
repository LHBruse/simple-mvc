package lhbruse;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

public class AppTest extends BaseJunit4Test {
    @Autowired
    private DispatcherServlet dispatcherServlet;

    @Test
    public void test1() throws ServletException, IOException {
        dispatcherServlet.init();

        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("accept", "text/html");
        request.setParameter("name", "asd");
        request.setRequestURI("/test/dispatch");
        request.setMethod("GET");
        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        response.getHeaderNames().forEach(headerName ->
                System.out.println(headerName + ":" + response.getHeader(headerName)));
    }

    @Test
    public void test2() throws ServletException, IOException {
        dispatcherServlet.init();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "asd");
        request.setRequestURI("/test/dispatch2");
        request.setMethod("GET");

        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        System.out.println("响应到客户端的数据：");
        System.out.println(response.getContentAsString());
    }
}
