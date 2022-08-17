package io.apimatic.core_lib;

import io.apimatic.core_interfaces.http.CoreHttpContext;
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

    }
    
    public static ApiException getApiException(String reason, CoreHttpContext context) {
    	return new ApiException(reason, context);
    }
}
