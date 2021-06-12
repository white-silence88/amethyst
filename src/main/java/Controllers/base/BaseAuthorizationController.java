package Controllers.base;

import Controllers.core.v1.AbstractController;
import DataTransferObjects.RuleDTO;
import Sources.UsersSource;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;
import Utils.v1.RightManager;
import spark.Request;

/**
 *
 * @author small-entropy
 */
public class BaseAuthorizationController extends AbstractController {
    
    protected static final String MSG_REGISTERED = ResponseMessages.REGISTERED.getMessage(); 
    protected static final String MSG_LOGIN = ResponseMessages.LOGIN.getMessage();
    protected static final String MSG_PASSWORD_CHANGED = ResponseMessages.PASSWORD_CHANGED.getMessage();
    protected static final String MSG_AUTOLOGIN = ResponseMessages.AUTOLOGIN.getMessage();
    protected static final String MSG_LOGOUT = ResponseMessages.LOGOUT.getMessage();
    
    protected static final String RULE = DefaultRights.USERS.getName();
    
    /**
     * Method for get rule by username from request body
     * @param request Spark request object
     * @param usersSource datastore source for users collection
     * @param right right string
     * @param action action name
     * @return 
     */
    protected static RuleDTO getRule_byUsername(
            Request request,
            UsersSource usersSource,
            String right,
            String action
    ) {
        return RightManager.getRuleByRequest_Username(
                request, 
                usersSource, 
                right, 
                action
        );
    }
}