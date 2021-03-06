package synthwave.repositories.morphia;

import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import synthwave.filters.CatalogsFilter;
import synthwave.models.morphia.extend.Catalog;
import engine.repositories.morphia.BasePropertyRepository;

/**
 * Class of catalog profile repository
 * @author small-entropy
 * @version 1
 */
public class CatalogProfileRepository 
	extends BasePropertyRepository<Catalog, CatalogsFilter, CatalogsRepository> {

	/**
	 * Default repository constructor. Create instance by
	 * datastore object & blacklist
	 * @param datastore Morphia datastore object
	 * @param blacklist blacklist property
	 */
	public CatalogProfileRepository(
			Datastore datastore,
			List<String> blacklist
	) {
		super("profile", blacklist, new CatalogsRepository(datastore));
	}
	
	@Override
	public Catalog findOneById(ObjectId entityId, String[] excludes) {
		CatalogsFilter filter = new CatalogsFilter(entityId, excludes);
		return this.getRepository().findOneById(filter);
	}
	
	@Override
	public void save(Catalog catalog) {
		this.getRepository().save(catalog);
	}
}
