/**
 *
 */
package com.zaloni.oData.controller;

import org.apache.olingo.commons.api.edm.provider.CsdlEdmProvider;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
@RequestMapping(EDMController.URI)
public class EDMController {

    public static final String URI = "/odata";

    @Autowired
    private CsdlEdmProvider edmProvider;

    @Autowired
    private EntityCollectionProcessor enityCollectionProcessor;

    @RequestMapping(value = "*")
    public void process(HttpServletRequest request, HttpServletResponse response) {
        OData odata = OData.newInstance();
        ServiceMetadata edm = odata.createServiceMetadata(edmProvider, new ArrayList<>());
        ODataHttpHandler handler = odata.createHandler(edm);
        handler.register(enityCollectionProcessor);
        System.out.println("token: " + request.getHeader("token"));
        System.out.println("==================================");
        handler.process(new HttpServletRequestWrapper(request) {
            @Override
            public String getServletPath() {
                return EDMController.URI;
            }
        }, response);
    }
}
