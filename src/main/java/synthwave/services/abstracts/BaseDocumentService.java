package synthwave.services.abstracts;

import engine.dto.RuleDTO;
import core.models.morphia.standalones.Standalone;
import synthwave.filters.UsersFilter;
import synthwave.models.morphia.extend.User;
import synthwave.repositories.morphia.UsersRepository;
import synthwave.utils.access.RightManager;
import core.utils.Comparator;
import dev.morphia.Datastore;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Abstract class for create service (for document)
 * @author small-entropy
 */
public abstract class BaseDocumentService <M extends Standalone, R> extends BaseService<M, R> {
	
	/** Property with repository for work with users data */
	UsersRepository usersRepository;
	
	/**
	 * Method for check access to action by request, right name & action name
	 * @param request Spark request object
	 * @param right name of right
	 * @param action name of action
	 * @return result of check
	 */
	public boolean checkHasAccess(Request request, String right, String action) {
		RuleDTO rule = getRule(request, right, action);
		return checkHasAccess(request, rule);
	}
	
	/**
	 * Method for check access to action by request & rule object
	 * @param request Spark request object
	 * @param rule rule data transfer object
	 * @return result of check
	 */
	public boolean checkHasAccess(Request request, RuleDTO rule) {
		boolean isTrusted = Comparator.id_fromParam_fromToken(request);
		return checkHasAccess(rule, isTrusted);
	}
	
	/**
	 * Method fir check access by rule object & truested state
	 * @param rule rule data transfer object
	 * @param isTrusted state of trusted
	 * @return result of check
	 */
	public boolean checkHasAccess(RuleDTO rule, boolean isTrusted) {
		boolean hasAccess;
		if (rule != null) {
			 return checkExistHasAccess(rule, isTrusted);
		} else {
			hasAccess = false;
		}
		return hasAccess;
	}
	
	/**
	 * Method for check access on work with global fields
	 * @param request Spark request object
	 * @param right name of right
	 * @param action name of action
	 * @return result of check
	 */
	public boolean checkHasGlobalAccess(Request request, String right, String action) {
		RuleDTO rule = getRule(request, right, action);
        return  checkHasGlobalAccess(request, rule);
	}
	
	/**
	 * Method for check access on work with global fields
	 * @param request Spark reqeust object
	 * @param rule rule data transfer object
	 * @return result of check
	 */
	public boolean checkHasGlobalAccess(Request request, RuleDTO rule) {
		boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        return (isTrusted) ? rule.isMyGlobal(): rule.isOtherGlobal();
	}
	
	/**
	 * Method for get result check rule
	 * @param rule rule data transfer object
	 * @param isTrusted state of trusted
	 * @return result of check
	 */
	protected boolean checkExistHasAccess(RuleDTO rule, boolean isTrusted) {
		return (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
	}
	
	/**
	 * Default constructor for create base document service. Create instance 
	 * by datastore & repository
	 * @param datastore Morphia datastore object
	 * @param repository service main repository
	 */
	public BaseDocumentService(Datastore datastore, R repository) {
		super(repository);
		this.usersRepository = new UsersRepository(datastore);
	}
	
	/**
	 * Constructor for create base document service. Create instance
	 * by datastore, repository & exclude fields (for some levels)
	 * @param datastore Morphia datastore object
	 * @param repository service main repository
	 * @param globalExcludes global exclude fields
	 * @param publicExcludes public exclude fields
	 * @param privateExcludes private exclude fields
	 */
	public BaseDocumentService(
           Datastore datastore,
           R repository,
           String[] globalExcludes,
           String[] publicExcludes,
           String[] privateExcludes
    ) {
		super(repository, globalExcludes, publicExcludes, privateExcludes);
		this.usersRepository = new UsersRepository(datastore);
	}
	
	/**
	 * Getter for user repository
	 * @return users repository
	 */
	public UsersRepository getUsersRepository() {
        return usersRepository;
    }

	/**
	 * Setter for users repository
	 * @param usersRepository new value for users repository
	 */
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    
    /**
     * Method for get for user document by entity id
     * @param id entity id
     * @return user document
     */
    protected User getUserById(ObjectId id) {
        UsersFilter filter = new UsersFilter(id, new String[] {});
        return getUsersRepository().findOneById(filter);
    }
    
    /**
     * Method for get rule data transfer object by request, 
     * right name & action name
     * @param request Spark request object
     * @param right right name
     * @param action action name
     * @return rule data transfer object
     */
    public RuleDTO getRule(
            Request request,
            String right,
            String action
    ) {
        return RightManager.getRuleByRequest_Token(
                request, 
                getUsersRepository(), 
                right, 
                action
        );
    }
}
