/**
 *
 */
package com.zaloni.oData.provider;


import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.server.api.uri.UriInfo;

public interface EntityProvider {

    CsdlEntityType getEntityType();

    String getEntitySetName();

    EntityCollection getEntitySet(UriInfo uriInfo);

    FullQualifiedName getFullyQualifiedName();
}
