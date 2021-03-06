package synthwave.controllers.v1.catalogs;

import spark.Response;
import spark.Request;
import dev.morphia.Datastore;

import core.constants.DefaultRights;
import engine.dto.RuleDTO;
import core.exceptions.AccessException;
import core.response.transformers.JsonTransformer;

import synthwave.controllers.abstracts.RESTController;
import synthwave.constants.CatalogsMessages;
import synthwave.models.morphia.extend.Catalog;
import synthwave.repositories.morphia.CatalogsRepository;
import synthwave.services.v1.catalogs.CatalogService;
import synthwave.utils.access.RightManager;
import core.utils.Comparator;

/**
 * Class controller for work with catalogs routes
 * @author small-entropy
 * @version 1
 */
public class CatalogsController 
	extends RESTController<Catalog, CatalogsRepository, CatalogService> {

	@Override
	protected void beforeCreateEntityRoute(Request request, Response response) 
		throws AccessException {
		RuleDTO rule = getService().getRule(
			request,
			getRight(),
			getCreateActionName()
		);
        boolean hasAccess;
		if (rule == null) {
			hasAccess = false;
		} else {
			boolean isTrusted = Comparator.id_fromParam_fromToken(request);
			hasAccess = (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
		}
		nextIfHasAccess(hasAccess, "CanNotCreate", "Has no access to create catalog");
	}

	@Override
	protected void beforeDeleteRoute(Request request, Response response) 
		throws AccessException {
		RuleDTO rule = getService().getRule(
			request, 
			getRight(), 
			getDeleteActionName()
		);
        boolean hasAccess = (rule == null) 
			? false 
			: RightManager.chechAccess(request, rule); 
		nextIfHasAccess(hasAccess, "CanNotDelete", "Has no access to delete catalog document");
	}

	@Override
	protected void beforeUpdateRoute(Request request, Response response) 
		throws AccessException {
		RuleDTO rule = getService().getRule(
			request, 
			getRight(), 
			getUpdateActionName()
		);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess;
		if (rule == null) {
			hasAccess = false;
		} else {
			hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
		}
		nextIfHasAccess(hasAccess, "CanNotUpdate", "Has no access to update catalog document");
	}

    
	/**
	 * Default constructor for catalogs controller object
	 * @param datastore Morphia datastore
	 * @param transformer response transformer object
	 */
	public CatalogsController(
			Datastore datastore,
			JsonTransformer transformer
	) {
		super(
				new CatalogService(datastore), 
				transformer,
				DefaultRights.CATALOGS.getName(),
				null,
				"",
				"/owner/:user_id/catalog/:catalog_id",
				"/owner/:user_id",
				true,
				true,
				true,
				true,
				true,
				CatalogsMessages.CREATED.getMessage(),
				CatalogsMessages.LIST.getMessage(),
				CatalogsMessages.ENTITY.getMessage(),
				CatalogsMessages.UPDATED.getMessage(),
				CatalogsMessages.DELETED.getMessage()
		);
	}
}
