package Controllers.base;

import Controllers.core.v1.AbstractController;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;

/**
 *
 * @author small-entropey
 */
public class BaseProductsController extends AbstractController {
    protected static final String RULE = DefaultRights.PRODUCTS.getName();

    protected static final String MSG_LIST = ResponseMessages.PRODUCTS.getMessage();
    protected static final String MSG_ENTITY = ResponseMessages.PRODUCT.getMessage();
    protected static final String MSG_CREATED = ResponseMessages.PRODUCT_CREATED.getMessage();
    protected static final String MSG_UPDATED = ResponseMessages.PRODUCT_UPDATED.getMessage();
    protected static final String MSG_DELETED = ResponseMessages.PRODUCT_DELETED.getMessage();

}