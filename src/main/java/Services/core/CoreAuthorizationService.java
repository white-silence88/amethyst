package Services.core;

import DataTransferObjects.UserDTO;
import Filters.UsersFilter;
import Models.User;
import Sources.UsersSource;
import Utils.common.JsonWebToken;
import Utils.common.RequestUtils;
import java.util.Arrays;
import java.util.List;
import spark.Request;

/**
 * Class for base functions for create authorization service
 * @author small-entropy
 */
public abstract class CoreAuthorizationService extends CoreService {

    /**
     * Method for login user by token
     * @param request Spark request object
     * @param source source for work with users collection
     * @param filterForReturn filter object for return answer
     * @param filterForSearch filter object for search user
     * @return founded user document
     */
    protected static User autoLoginUser(Request request, UsersSource source, UsersFilter filterForReturn, UsersFilter filterForSearch) {
        // Get user document by token
        User user = CoreUserService.getUserByToken(request, source, filterForSearch);
        // Get token from request
        String token = RequestUtils.getTokenByRequest(request);
        // Check user on exist & check issuedToken (token must be contains in this field)
        // If all check right - return user object,
        // else - return null.
        return (user != null
                && token != null
                && user.getIssuedTokens() != null
                && user.getIssuedTokens().contains(token))
                ? CoreUserService.getUserByToken(request, source, filterForReturn)
                : null;
    }


    /**
     * Method for auth user
     * @param request Spark request object
     * @param source source for work with users collection
     * @return user document
     */
    protected static User loginUser(Request request, UsersSource source) {
        // Transform JSON object from body to Map
        UserDTO userDTO = CoreUserService.getUserDtoFromBody(request);
        // Get field "password" from map
        String password = userDTO.getPassword();
        // Find user by username from request body
        User user = CoreUserService.getUserByUsername(userDTO, source);
        // Verified user password:
        // if user not find - false,
        // if user send wrong password - false,
        // if user send correct password - true
        boolean verified = user != null && user.verifyPassword(password).verified;
        // Check verified result
        if (verified) {
            // Get user generated token
            List<String> tokens = user.getIssuedTokens();
            // Check list of user generated tokens.
            // If user have a generated token - get it.
            // If user haven't a generated token - generate new token
            String token;
            if (tokens == null || tokens.isEmpty()) {
                token = JsonWebToken.encode(user);
                user.setIssuedTokens(Arrays.asList(token));
                source.save(user);
            }
            return user;
        } else {
            return null;
        }
    }

    /**
     * Base method for register user
     * @param userDTO user data transfer obejct
     * @param source source for work with users collection
     * @return user document
     */
    protected static User registerUser(UserDTO userDTO, UsersSource source) {
        // Create user document
        userDTO.setProperties(CoreUserPropertyService.getDefaultUserProperty());
        userDTO.setProfile(CoreUserProfileService.getDefaultProfile());
        userDTO.setRights(CoreRightService.getDefaultRightList());
        return source.create(userDTO);
    }

    /**
     * Method for remove user token
     * @param request Spark request object
     * @param source source for work with users collection
     * @return user document
     */
    protected static User logoutUser(Request request, UsersSource source) {
        // Not include in token value
        final int NOT_IN_LIST = -1;
        // Create filter object
        UsersFilter filter = new UsersFilter();
        // Set exludes field for filter
        filter.setExcludes(CoreUserService.ALL_ALLOWED);
        // Get user document from database (full document)
        User user = CoreUserService.getUserByToken(request, source, filter);
        // Check user document on exist
        // If founded user document not equal null - remove token from issued token list,
        // save changes & return saved document.
        // If founded user document equal null - return null.
        if (user != null) {
            // Get token from request
            String token = RequestUtils.getTokenByRequest(request);
            // Get user token index
            int tokenIndex = user.getIssuedTokens().indexOf(token);
            // Check token index on list contains
            // If token index not contains in list - return null.
            // If token index contains in list - save changes, and return
            // saved document from database
            if (tokenIndex != NOT_IN_LIST) {
                // Remove from issued tokens list token from request
                user.getIssuedTokens().remove(tokenIndex);
                // Save changes in database
                source.save(user);
                // return user document
                return user;
            } else {
                // If token not include in issued token list
                // return null
                return null;
            }
        } else {
            // Return null if user not found
            return null;
        }
    }
}
