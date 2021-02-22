package com.zaloni.oData.provider;

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;
import org.apache.olingo.commons.api.ex.ODataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GenericEdmProvider extends CsdlAbstractEdmProvider {

    @Autowired
    private ApplicationContext ctx;

    public static final String NAMESPACE = "com.zaloni.odata.model";
    public static final String CONTAINER_NAME = "Container";
    public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

    @Override
    public List<CsdlSchema> getSchemas() throws ODataException {
        CsdlSchema schema = new CsdlSchema();
        schema.setNamespace(NAMESPACE);
        Map<String, EntityProvider> entityProviders = ctx.getBeansOfType(EntityProvider.class);
        List<CsdlEntityType> entityTypes = new ArrayList<>();
        for (String entity : entityProviders.keySet()) {
            EntityProvider entityProvider = entityProviders.get(entity);
            entityTypes.add(entityProvider.getEntityType());
        }
        schema.setEntityTypes(entityTypes);
        schema.setEntityContainer(getEntityContainer());
        List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
        schemas.add(schema);
        return schemas;
    }

    @Override
    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
        CsdlEntityType result = null;
        Map<String, EntityProvider> entityProviders = ctx.getBeansOfType(EntityProvider.class);
        for (String entity : entityProviders.keySet()) {
            EntityProvider entityProvider = entityProviders.get(entity);
            CsdlEntityType entityType = entityProvider.getEntityType();
            if (entityType.getName().equals(entityTypeName.getName())) {
                result = entityType;
                break;
            }
        }
        return result;
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) throws ODataException {

        CsdlEntitySet result = null;
        Map<String, EntityProvider> entityProviders = ctx.getBeansOfType(EntityProvider.class);

        for (String entity : entityProviders.keySet()) {
            EntityProvider entityProvider = entityProviders.get(entity);
            CsdlEntityType entityType = entityProvider.getEntityType();
            if (entityProvider.getEntitySetName().equals(entitySetName)) {
                result = new CsdlEntitySet();
                result.setName(entityProvider.getEntitySetName());
                result.setType(entityProvider.getFullyQualifiedName());
                break;
            }

        }
        return result;

    }

    @Override
    public CsdlEntityContainer getEntityContainer() throws ODataException {
        List<CsdlEntitySet> entitySets = new ArrayList<>();
        Map<String, EntityProvider> entityProviders = ctx.getBeansOfType(EntityProvider.class);

        for (String entity : entityProviders.keySet()) {
            EntityProvider entityProvider = entityProviders.get(entity);
            entitySets.add(getEntitySet(CONTAINER, entityProvider.getEntitySetName()));
        }

        CsdlEntityContainer entityContainer = new CsdlEntityContainer();
        entityContainer.setName(CONTAINER_NAME);
        entityContainer.setEntitySets(entitySets);


        return entityContainer;
    }

    @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(
            FullQualifiedName entityContainerName) throws ODataException {
        if (entityContainerName == null || entityContainerName.equals(CONTAINER)) {
            CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
            entityContainerInfo.setContainerName(CONTAINER);
            return entityContainerInfo;
        }
        return null;
    }
}
