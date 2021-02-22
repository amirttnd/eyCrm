/**
 *
 */
package com.zaloni.oData.provider;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Component
public class CategoryEntityProvider implements EntityProvider {

    public static final String NAMESPACE = "com.zaloni.odata.model";
    public static final String CONTAINER_NAME = "Container";
    public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);
    public static final String ET_CATEGORY_NAME = "Category";
    public static final FullQualifiedName ET_CATEGORY_FQN = new FullQualifiedName(NAMESPACE, ET_CATEGORY_NAME);
    public static final String ES_CATEGORIES_NAME = "Categories";

    @Override
    public CsdlEntityType getEntityType() {
        CsdlProperty id = new CsdlProperty().setName("ID").setType(
                EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        CsdlProperty name = new CsdlProperty().setName("Name").setType(
                EdmPrimitiveTypeKind.String.getFullQualifiedName());
        CsdlProperty description = new CsdlProperty().setName("Description").setType(
                EdmPrimitiveTypeKind.String.getFullQualifiedName());

        CsdlPropertyRef propertyRef = new CsdlPropertyRef();
        propertyRef.setName("ID");

        CsdlEntityType entityType = new CsdlEntityType();
        entityType.setName(ET_CATEGORY_NAME);
        entityType.setProperties(Arrays.asList(id, name, description));
        entityType.setKey(Arrays.asList(propertyRef));
        return entityType;
    }

    @Override
    public EntityCollection getEntitySet(UriInfo uriInfo) {
        List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
        UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
        EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();
        return getData(edmEntitySet);
    }

    private EntityCollection getData(EdmEntitySet edmEntitySet) {
        EntityCollection entitySet = new EntityCollection();
        List<Entity> entityList = entitySet.getEntities();
        Entity entity1 = new Entity()
                .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, 1))
                .addProperty(new Property(null, "Name", ValueType.PRIMITIVE, "Notebook Basic 15"))
                .addProperty(new Property(null, "Description", ValueType.PRIMITIVE, "Notebook Basic, 1.7GHz - 15 XGA - 1024MB DDR2 SDRAM - 40GB"));
        entity1.setId(createId("Products", 1));
        entityList.add(entity1);
        Entity entity2 = new Entity()
                .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, 2))
                .addProperty(new Property(null, "Name", ValueType.PRIMITIVE, "1UMTS PDA"))
                .addProperty(new Property(null, "Description", ValueType.PRIMITIVE, "Ultrafast 3G UMTS/HSDPA Pocket PC, supports GSM network"));
        entity1.setId(createId("Products", 2));
        entityList.add(entity2);
        Entity entity3 = new Entity()
                .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, 3))
                .addProperty(new Property(null, "Name", ValueType.PRIMITIVE, "Ergo Screen"))
                .addProperty(new Property(null, "Description", ValueType.PRIMITIVE, "17 Optimum Resolution 1024 x 768 @ 85Hz, resolution 1280 x 960"));
        entity1.setId(createId("Products", 3));
        entityList.add(entity3);
        return entitySet;
    }

    private static URI createId(final String entitySetName, final Object id) {
        try {
            return new URI(entitySetName + "(" + String.valueOf(id) + ")");
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
        }
    }

    @Override
    public String getEntitySetName() {
        return ES_CATEGORIES_NAME;
    }

    @Override
    public FullQualifiedName getFullyQualifiedName() {
        return ET_CATEGORY_FQN;
    }

}
