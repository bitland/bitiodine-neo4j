package virex4bitcoin.test;

import java.util.Arrays;
import java.util.Collection;


public abstract class Virex4BitcoinTest
{	
    public static Collection<Object[]> testSet(){
    	return Arrays.asList(new Object[][] {
    			{"1Q6DgPKUn1zciWKye9A5UkXWRTmupqFt3", 1233446400},
    			{"1Q6DgPKUn1zciWKye9A5UkXWRTmupqFt3", 1306886400},
    			{"1Q6DgPKUn1zciWKye9A5UkXWRTmupqFt3", 1383264000},
    			{"1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa", 1233446400},
    			{"1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa", 1306886400},
    			{"1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa", 1383264000},
    			{"1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp", 1233446400},
    			{"1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp", 1306886400},
    			{"1dice8EMZmqKvrGE4Qc9bUFf9PX3xaYDp", 1383264000},
    			
    			{"159FTr7Gjs2Qbj4Q5q29cvmchhqymQA7of",1383264000},
    			{"1E29AKE7Lh1xW4ujHotoT4JVDaDdRPJnWu",1383264000}
    	});
    }
}
