package Controllers.v1;

import DataTransferObjects.RuleDTO;
import Models.Catalog;
import Responses.SuccessResponse;
import Services.v1.CatalogService;
import Sources.CatalogsSource;
import Sources.UsersSource;
import Transformers.JsonTransformer;
import Utils.constants.DefaultActions;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;
import Utils.v1.RightManager;
import dev.morphia.Datastore;
import java.util.List;

import static spark.Spark.*;

/**
 * Class controller for work with catalogs routes
 * @author small-entropy
 */
public class CatalogsController {
    
    /**
     * Static method for initialize catalogs routes
     * @param store Morphia datastore object
     * @param transformer converter to JSON
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Create catalog datastore source
        CatalogsSource catalogsSource = new CatalogsSource(store);
        // Create user datastore source
        UsersSource userSource = new UsersSource(store);
        
        // Route for work with catalog list
        get("", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, userSource, DefaultRights.CATALOGS.getName(), DefaultActions.CREATE.getName());
            List<Catalog> catalogs = CatalogService.getCatalogs(req, catalogsSource, rule);
            return new SuccessResponse<>(ResponseMessages.CATALOGS_LIST.getMessage(), catalogs);
        }, transformer);

        // Route for create catalog
        post("/owner/:user_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, userSource, DefaultRights.CATALOGS.getName(), DefaultActions.CREATE.getName());
            Catalog catalog = CatalogService.createCatalog(req, catalogsSource, userSource, rule);
            return new SuccessResponse<>(ResponseMessages.CATALOG_CREATED.getMessage(), catalog);
        }, transformer);
        
        // Route for create catalog document
        get("/owner/:user_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, userSource, DefaultRights.CATALOGS.getName(), DefaultActions.CREATE.getName());
            List<Catalog> catalogs = CatalogService.getCatalogsByUser(req, catalogsSource, rule);
            return new SuccessResponse<>(ResponseMessages.CATALOG.getMessage(), catalogs);
        }, transformer);
        
        // Route for get user catalog by id
        get("/owner/:user_id/catalog/:catalog_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, userSource, DefaultRights.CATALOGS.getName(), DefaultActions.READ.getName());
            Catalog catalog = CatalogService.getCatalogById(req, catalogsSource, rule);
            return new SuccessResponse<>(ResponseMessages.CATALOG.getMessage(), catalog);
        }, transformer);
        
        // Route for update catalog
        put("/owner/:user_id/catalog/:catalog_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, userSource, DefaultRights.CATALOGS.getName(), DefaultActions.UPDATE.getName());
            Catalog catalog = CatalogService.updateCatalog(req, catalogsSource, rule);
            return new SuccessResponse<>(ResponseMessages.CATALOG_UPDATED.getMessage(), catalog);
        }, transformer); 
        
        // Route for delete catalog
        delete("/owner/:user_id/catalog/:catalog_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, userSource, DefaultRights.CATALOGS.getName(), DefaultActions.DELETE.getName());
            Catalog catalog = CatalogService.deleteCatalog(req, catalogsSource, rule);
            return new SuccessResponse<>(ResponseMessages.CATALOG_DELETED.getMessage(), catalog);
        }, transformer);
    }
}
