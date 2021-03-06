package synthwave.controllers.abstracts;

import java.util.List;

import engine.controllers.BaseController;
import core.exceptions.AccessException;
import core.exceptions.DataException;
import core.exceptions.TokenException;
import core.models.morphia.standalones.Standalone;
import engine.response.answer.Success;
import core.response.transformers.JsonTransformer;
import spark.Response;
import spark.Request;
import synthwave.services.abstracts.BaseService;

import static spark.Spark.*;

/**
 * Class for create REST controllers
 * @author small-entropy
 */
public abstract class RESTController<
    M extends Standalone, 
    R, 
    S extends BaseService<M, R>> 
    extends BaseController<S, JsonTransformer> {
    
    private final String entityPath;
    private final String listPath;
    private final String entityPathByOwner;
    private final String listByPathByOwner;
    private final boolean needAccessCheckCreate;
    private final boolean needAccessCheckList;
    private final boolean needAccessCheckEntity;
    private final boolean needAccessCheckUpdate;
    private final boolean needAccessCheckDelete;
    private final String msgCreate;
    private final String msgList;
    private final String msgEntity;
    private final String msgUpdate;
    private final String msgDelete;

    /**
     * Default constructor for create REST controller
     * @param service service instance
     * @param transformer response transformer
     * @param right name of right
     * @param entityPath path for work with entity
     * @param listPath path for work with list
     * @param entityPathByOwner path for work with entity by owner
     * @param listByPathByOwner path for work with entities list by owner
     * @param needAccessCheckCreate state of need check access for create entity method
     * @param needAccessCheckList state of need check access for read entities list method
     * @param needAccessCheckEntity state of need check access for read entity method
     * @param needAccessCheckUpdate state of need check access for update entity method
     * @param needAccessCheckDelete state of need check access for delete entity method
     * @param msgCreate message for success create
     * @param msgList message for success get list
     * @param msgEntity message for success get entity
     * @param msgUpdate message for success update entity
     * @param msgDelete message for success delete entity
     */
    public RESTController(
        S service, 
        JsonTransformer transformer,
        String right,
        String entityPath,
        String listPath,
        String entityPathByOwner,
        String listByPathByOwner,
        boolean needAccessCheckCreate,
        boolean needAccessCheckList,
        boolean needAccessCheckEntity,
        boolean needAccessCheckUpdate,
        boolean needAccessCheckDelete,
        String msgCreate,
        String msgList,
        String msgEntity,
        String msgUpdate,
        String msgDelete
    ) {
        super(service, transformer, right);
        this.listPath = listPath;
        this.entityPath = entityPath;
        this.entityPathByOwner = entityPathByOwner;
        this.listByPathByOwner = listByPathByOwner;
        this.needAccessCheckCreate = needAccessCheckCreate;
        this.needAccessCheckList = needAccessCheckList;
        this.needAccessCheckEntity = needAccessCheckEntity;
        this.needAccessCheckUpdate = needAccessCheckUpdate;
        this.needAccessCheckDelete = needAccessCheckDelete;
        this.msgCreate = msgCreate;
        this.msgList = msgList;
        this.msgEntity = msgEntity;
        this.msgUpdate = msgUpdate;
        this.msgDelete = msgDelete;
    }

    // BEGIN: BLOCK DELETE ROUTE
    /**
     * Method register delete method
     * @param path url pattern for delete method
     */
    private void deleteRoute(String path) {
        delete(path, (request, response) -> {
            M entity = (needAccessCheckDelete)
                ? getService().deleteEntity(request, getRight(), getDeleteActionName())
                : getService().deleteEntity(request);
            return new Success<>(msgDelete, entity);
        }, getTransformer()); 
    }

    protected void beforeDeleteRoute(Request request, Response response) 
        throws AccessException, TokenException, DataException {}

    /**
     * Method for register delete route
     */
    @Override
    protected void deleteRoute() {
        deleteRoute(entityPath);
    }

    /**
     * Method for register delete route by user
     */
    protected void deleteByOwnerRoute() {
        deleteRoute(entityPathByOwner);
    }
    // END: BLOCK DELETE ROUTE

    // BEGIN: BLOCK UPDATE ROUTE
    /**
     * Method for register update roure
     * @param path url path
     */
    private void updateRoute(String path) {
        put(path, (request, response) -> {
            M entity = (needAccessCheckUpdate)
                ? getService().updateEntity(request, getRight(), getUpdateActionName())
                : getService().updateEntity(request);
            return new Success<>(msgUpdate, entity);
        }, getTransformer());
    }

    protected void beforeUpdateRoute(Request request, Response response) 
        throws AccessException, TokenException, DataException {}

    protected void nextIfHasAccess(
            boolean hasAccess, 
            String message, 
            String text) 
        throws AccessException {
        if (!hasAccess) {
            Error error = new Error(text);
            throw new AccessException(message, error); 
        }
    }

    /**
     * Method for register update route
     */
    @Override
    protected void updateRoute() {
        updateRoute(entityPath);
    }

    /**
     * Method for register update route by owner
     */
    protected void updateByOwnerRoute() {
        updateRoute(entityPathByOwner);
    }
    // END: BLOCK UPDATE ROUTE

    // BEGIN: BLOCK GET ENTITY BY ID ROUTE
    /**
     * Method for register route for get entity by id
     */
    @Override
    protected void getEntityByIdRoute() {
        get(entityPath, (request, response) -> {
            M entity = (needAccessCheckEntity)
                ? getService().getEntityById(request, getRight(), getReadActionName())
                : getService().getEntityById(request);
            return new Success<>(msgEntity, entity);
        }, getTransformer());
    }

    protected void beforeGetEntityByIdRoute(Request request, Response response) 
        throws AccessException, TokenException, DataException {}

    /**
     * Method for register get entity by id & owner id
     */
    protected void getEntityByIdByOwnerRoute() {
        get(entityPathByOwner, (request, response) -> {
            M entity = (needAccessCheckEntity)
                ? getService().getEntityByIdByOwner(request, getRight(), getReadActionName())
                : getService().getEntityByIdByOwner(request);
            return new Success<>(msgEntity, entity);
        }, getTransformer());
    }

    protected void beforeGetEntityByIdByOwnerRoute(Request request, Response response) 
        throws AccessException, TokenException, DataException {}
    // END: BLOCK GET ENTITY BY ID ROUTE

    // BEGIN: BLOCK GET ENTITIES LIST ROUTE
    /**
     * Method for register route get entities list
     */
    @Override
    protected void getListRoute() {
        get(listPath, (request, response) -> {
            List<M> entities = (needAccessCheckList)
                ? getService().getEntitiesList(request, getRight(), getReadActionName())
                : getService().getEntitiesList(request);
            return new Success<>(msgList, entities);
        }, getTransformer());
    }

    protected void beforeGetList(Request request, Response response) 
        throws TokenException, AccessException, DataException {}

    /**
     * Method for register route to get entitites list by owner id
     */
    protected void getListByOwnerRoute() {
        get(listByPathByOwner, (request, response) -> {
            List<M> entities = (needAccessCheckList)
                ? getService().getEntitiesListByOwner(request, getRight(), getReadActionName())
                : getService().getEntitiesListByOwner(request);
            return new Success<>(msgList, entities);
        }, getTransformer());
    }

    protected void beforeGetListByOwnerRoute(Request request, Response response) 
        throws AccessException, TokenException, DataException {}
    // END: BLOCK GET ENTITIES LIST ROUTE

    // BEGIN: BLOCK GET ENTITIES LIST ROUTE
    /**
     * Method for register route to create entity
     * @param path url path
     */
    private void createEntity(String path) {
        post(path, (request, response) -> {
            M entity = (needAccessCheckCreate)
                ? getService().createEntity(request, getRight(), getCreateActionName())
                : getService().createEntity(request);
            return new Success<>(msgCreate, entity);
        }, getTransformer());
    }
    
    protected void beforeCreateEntityRoute(Request request, Response response) 
        throws AccessException, TokenException, DataException {}

    /**
     * Method for register route to create entity
     */
    @Override
    protected void createEntityRoute() {
        createEntity(listPath);
    }

    /**
     * Method for register route to create entity by owner id
     */
    protected void createEntityByOwner() {
        createEntity(listByPathByOwner);
    }
    // END: BLOCK GET ENTITIES LIST ROUTE

    protected void customRoutes() {}

    protected void afterDelete() {}

    protected void registerBeforeListPath() {
        before(getListPath(), (request, response) -> {
            switch (request.requestMethod()) {
                case "POST": 
                    beforeCreateEntityRoute(request, response);
                case "GET":
                    beforeGetList(request, response);
                default:
                    break;
            }
        });
    }

    protected void registerBeforeEntityPath() {
        before(getEntityPath(), (request, response) -> {
            switch (request.requestMethod()) {
                case "GET":
                    beforeGetEntityByIdRoute(request, response);
                case "PUT":
                    beforeUpdateRoute(request, response);
                    break;
                case "DELETE":
                    beforeDeleteRoute(request, response);
                    break;
                default:
                    break;
            }
        });
    }

    protected void registerBeforeListPathByOwner() {
        before(getListByPathByOwner(), (request, response) -> {
            switch (request.requestMethod()) {
                case "POST":
                    beforeCreateEntityRoute(request, response);
                    break;
                case "GET":
                    beforeGetListByOwnerRoute(request, response);
                    break;
                default:
                    break;
            }
        });
    }

    protected void registerBeforeEntityPathByOwner() {
        before(getEntityPathByOwner(), (request, response) -> {
            switch (request.requestMethod()) {
                case "GET":
                    beforeGetEntityByIdByOwnerRoute(request, response);
                    break;
                case "PUT":
                    beforeUpdateRoute(request, response);
                    break;
                case "DELETE":  
                    beforeDeleteRoute(request, response);
                default:
                    break;
            }
        });
    }

    @Override
    public final void register() {
        registerBefore();
        if (listPath != null) {
            registerBeforeListPath();
            getListRoute();
            if (entityPath != null) {
                createEntityRoute();
            }
        }
        
        if (entityPath != null) {
            registerBeforeEntityPath();
            getEntityByIdRoute();
            updateRoute();
            deleteRoute();
        }

        if (listByPathByOwner != null) {
            registerBeforeListPathByOwner();
            getListByOwnerRoute();
            if (entityPathByOwner != null) {
                createEntityByOwner();
            }
        }

        if (entityPathByOwner != null) {
            registerBeforeEntityPathByOwner();
            getEntityByIdByOwnerRoute();
            updateByOwnerRoute();
            deleteByOwnerRoute();
        }
        customRoutes();
        registerAfter();
    }

    /**
     * Getter for success create entity message
     * @return value for success create entity message
     */
    public String getMsgCreate() {
        return msgCreate;
    }

    /**
     * Getter for success get entities list message
     * @return value of success get entities list message
     */
    public String getMsgList() {
        return msgList;
    }

    /**
     * Getter for success get entity message
     * @return value of success get entity message
     */
    public String getMsgEntity() {
        return msgEntity;
    }

    /**
     * Getter for success update message
     * @return value of success update message
     */
    public String getMsgUpdate() {
        return msgUpdate;
    }

    /**
     * Getter for success delete message
     * @return value for success delete message
     */
    public String getMsgDelete() {
        return msgDelete;
    }

    /**
     * Getter for state of need check access for create entity method
     * @return state of need check access for create entity method
     */
    public boolean getNeedAccessCheckCreate() {
        return needAccessCheckCreate;
    }

    /**
     * Getter for state of need check access for read entities list method
     * @return state of need check access for read entities list method
     */
    public boolean getNeedAccessCheckList() {
        return needAccessCheckList;
    }

    /**
     * Getter for state of need check access for read entity method
     * @return state of need check access for read entity method
     */
    public boolean getNeedAccessCheckEntity() {
        return needAccessCheckEntity;
    }

    /**
     * Getter for state of need check access for update entity method
     * @return state of need check access for update entity method
     */
    public boolean getNeedAccessCheckUpdate() {
        return needAccessCheckUpdate;
    }

    /**
     * Getter for state of need check access for delete entity method
     * @return state of need check access for delete entity method
     */
    public boolean getNeedAccessCheckDelete() {
        return needAccessCheckDelete;
    }
    
    /**
     * Getter for list path
     * @return value of list path
     */
    public String getListPath() {
        return listPath;
    }

    /**
     * Getter for entity path
     * @return value of entity path
     */
    public String getEntityPath() {
        return entityPath;
    }

    /**
     * Getter for entity path with owner
     * @return value of entity path with owner
     */
    public String getEntityPathByOwner() {
        return entityPathByOwner;
    }

    /**
     * Getter for list path with owner
     * @return value of list path with owner
     */
    public String getListByPathByOwner() {
        return listByPathByOwner;
    }
}
