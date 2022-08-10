package io.apimatic.core_lib;

import io.apimatic.core_interfaces.http.HttpContext;
import io.apimatic.core_lib.types.ApiException;
import io.apimatic.core_lib.types.BaseModel;
import io.apimatic.core_lib.utilities.CoreHelper;

/**
 * Hello world!
 *
 */
public class App 
{
    public static <T> void main( String[] args ) throws Exception
    {
        CoreResponseHandler<BaseModel, ApiException> responseHandler = new CoreResponseHandler<BaseModel, ApiException>();
        ErrorCase<ApiException> case1 = ErrorCase.create("error1", (reason, context) -> new ApiException(reason, context));
       
    }
    
    public static ApiException getApiException(String reason, HttpContext context) {
    	return new ApiException(reason, context);
    }
}
