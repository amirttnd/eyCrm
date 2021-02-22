package com.zaloni.oData.provider;

import org.apache.olingo.commons.api.data.AbstractEntityCollection;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class GenericEntityCollectionProcessor implements EntityCollectionProcessor {

    @Autowired
    private ApplicationContext ctx;

    private OData odata;
    private ServiceMetadata serviceMetadata;

    public void init(OData odata, ServiceMetadata serviceMetadata) {
        this.odata = odata;
        this.serviceMetadata = serviceMetadata;
    }

    public void readEntityCollection(ODataRequest request,
                                     ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
            throws ODataApplicationException, SerializerException {

        List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
        UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths
                .get(0);
        EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

        AbstractEntityCollection entitySet = getData(uriInfo);
        ODataSerializer serializer = odata.createSerializer(responseFormat);

        EdmEntityType edmEntityType = edmEntitySet.getEntityType();
        ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet)
                .build();

        final String id = request.getRawBaseUri() + "/" + edmEntitySet.getName();

        EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions
                .with().id(id).contextURL(contextUrl).build();

        SerializerResult serializerResult = serializer.entityCollection(serviceMetadata, edmEntityType, entitySet, opts);
        InputStream serializedContent = serializerResult.getContent();
        response.setContent(serializedContent);
        response.setStatusCode(HttpStatusCode.OK.getStatusCode());
        response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
    }


    private EntityCollection getData(UriInfo uriInfo) {
        List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
        UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0); // in our example, the first segment is the EntitySet
        EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();
        EntityCollection entitySet = null;
        Map<String, EntityProvider> entityProviders = ctx.getBeansOfType(EntityProvider.class);
        for (String entity : entityProviders.keySet()) {
            EntityProvider entityProvider = entityProviders.get(entity);
            if (entityProvider.getEntityType().getName().equals(edmEntitySet.getEntityType().getName())) {
                entitySet = entityProvider.getEntitySet(uriInfo);
                break;
            }
        }
        return entitySet;
    }

}
