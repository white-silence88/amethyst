package synthwave.applications;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import com.mongodb.client.MongoClients;

import engine.applicatoins.RestApplication;
import core.response.transformers.JsonTransformer;

import engine.controllers.ErrorsController;
import synthwave.controllers.v1.companies.CompaniesController;
import synthwave.controllers.v1.companies.CompanyProfileController;
import synthwave.controllers.v1.companies.CompanyPropertiesController;
import synthwave.controllers.v1.users.UserRightsController;
import synthwave.controllers.v1.products.ProductsController;
import synthwave.controllers.v1.categories.CategoriesController;
import synthwave.controllers.v1.categories.CategoryProfileController;
import synthwave.controllers.v1.categories.CategoryPropertiesController;
import synthwave.controllers.v1.users.AuthorizationController;
import synthwave.controllers.v1.users.UserPropertiesController;
import synthwave.controllers.v1.users.UserProfileController;
import synthwave.controllers.v1.users.UserController;
import synthwave.controllers.v1.tags.TagsController;
import synthwave.controllers.v1.catalogs.CatalogProfileController;
import synthwave.controllers.v1.catalogs.CatalogPropertiesController;
import synthwave.controllers.v1.catalogs.CatalogsController;

import static spark.Spark.*;

/**
 * Class for REST API Synthwave HTTP server
 * @author small-entropy
 */
public class RESTSynthwave extends RestApplication<Datastore, JsonTransformer> {
    
    /** Path to Morphia models */
    private final String modelsPath;

    /**
     * Default constructor for Synthwave REST API application
     * @param storeNulls state store null fields for Morphia models
     * @param storeEmpties state store rmpties fields for Morphia models
     * @param modelsPath path to morphia models
     * @param dbName database name
     * @param origins allowed origins
     * @param headers allowed headers
     * @param methods allowed methods
     * @param port port of web server
     */
    public RESTSynthwave(
        boolean storeNulls,
        boolean storeEmpties,
        String modelsPath,
        String dbName,
        String origins,
        String headers,
        String methods,
        int port
    ) {
        super(
            Morphia.createDatastore(
                MongoClients.create(),
                dbName,
                MapperOptions
                    .builder()
                    .storeNulls(storeNulls)
                    .storeEmpties(storeEmpties)
                    .build()
            ),
            new JsonTransformer(),
            origins,
            headers,
            methods,
            port
        ); 
        this.modelsPath = modelsPath;
    }

    @Override
    protected void routesInit() {
        var datastore = getDatastore();
        var transformer = getTransformer();
        // BEGIN: REGISTER ROUTES FOR API VERSION 1
        path("/v1", () -> {
            // BEGIN: REGISTER ROUTES FOR USERS API
            path("/users", () -> {
                new AuthorizationController(datastore, transformer).register();
                new UserController(datastore, transformer).register();
                new UserPropertiesController(datastore, transformer).register();
                new UserProfileController(datastore, transformer).register();
                new UserRightsController(datastore, transformer).register();
            });
            // END: REGISTER ROUTES FOR USERS API

            // BEGIN: REGISTER ROUTES FOR CATALOGS API
            path("/catalogs", () -> {
                new CatalogsController(datastore, transformer).register();
                new CatalogProfileController(datastore, transformer).register();
                new CatalogPropertiesController(datastore, transformer).register();
            });
            // END: REGISTER ROUTES FOR CATALOGS API

            // BEGIN: REGISTER ROUTES FOR CATEGORIES API
            path("/categories", () -> {
                new CategoriesController(datastore, transformer).register();
                new CategoryProfileController(datastore, transformer).register();
                new CategoryPropertiesController(datastore, transformer).register();
            });
            // END: REGISTER ROUTES FOR CATEGORIES API

            // BEGIN: REGISTER ROUTES FOR COMPANIES API
            path("/companies", () -> {
                new CompaniesController(datastore, transformer).register();
                new CompanyProfileController(datastore, transformer).register();
                new CompanyPropertiesController(datastore, transformer).register();
            });
            // END: REGISTER ROUTES FOR COMPANIES API

            // BEGIN: REGISTER ROUTES FOR TAGS API
            path("/tags", () -> {
                new TagsController(datastore, transformer).register();
            });
            // END: REGISTER ROUTES FOR TAGS API

            // BEGIN: REGISTER ROUTES FOR PRODUCTS API
            path("/products", () -> {
                new ProductsController(datastore, transformer).register();
            });
            // END: REGISTER ROUTES FOR PRODUCTS API
        });
        // END: REGISTER ROUTES FOR API VERSION 1
    }

    @Override
    protected void datastorePrepare() {
        // Map all models from package
        getDatastore().getMapper().mapPackage(modelsPath);
        // Ensure database indexes by models
        getDatastore().ensureIndexes();
    }

    @Override
    protected void errorsHandlersInit() {
        ErrorsController.errors_InternalServerError();
        ErrorsController.errors_ExternalPackagesErrors();
        ErrorsController.errors_Custom();
    }
}

 