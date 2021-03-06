package cn.wltc.framework.service;


import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.WebUtils;

import cn.wltc.framework.service.page.PageRequest;
/**
 * �ㄤ���〉缁�欢瑕����被,�扮���〉缁�欢瑕��姝ょ被��ewPageRequest()�规�浠ラ���������〉��缓
 * @author badqiu
 */
public class PageRequestFactory {
    public static final int MAX_PAGE_SIZE = 1000;
    
    static BeanUtilsBean2 beanUtils = new BeanUtilsBean2();
    static {
    	//�ㄤ�娉ㄥ��ユ�绫诲���浆��
    	String[] datePatterns = new String[] {"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.SSS","HH:mm:ss"};
    	ConvertRegisterHelper.registerConverters(beanUtils.getConvertUtils(),datePatterns);
    	
        System.out.println("PageRequestFactory.MAX_PAGE_SIZE="+MAX_PAGE_SIZE);
    }
    
    public static PageRequest bindPageRequest(PageRequest pageRequest,HttpServletRequest request,String defaultSortColumns){
        return bindPageRequest(pageRequest, request, defaultSortColumns, BaseQuery.DEFAULT_PAGE_SIZE);
    }
    
    public static PageRequest bindPageRequest(PageRequest pageRequest, HttpServletRequest request,String defaultSortColumns, int defaultPageSize) {
	    	Map params = WebUtils.getParametersStartingWith(request, "");
	    	try {
	    		beanUtils.copyProperties(pageRequest, params);
		    } catch (IllegalAccessException e) {
		    	throw new IllegalArgumentException("beanUtils.copyProperties() error",e);
			} catch (InvocationTargetException e) {
				throw new IllegalArgumentException("beanUtils.copyProperties() error",e.getTargetException());
			}
	        
	        pageRequest.setPageNumber(getIntParameter(request, "pageNumber", 1));
	        pageRequest.setPageSize(getIntParameter(request, "pageSize", defaultPageSize));
	        pageRequest.setSortColumns(getStringParameter(request, "sortColumns",defaultSortColumns));
	        
	        if(pageRequest.getPageSize() > MAX_PAGE_SIZE) {
	            pageRequest.setPageSize(MAX_PAGE_SIZE);
	        }
	        return pageRequest;
    }
    
    static String getStringParameter(HttpServletRequest request,String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    static int getIntParameter(HttpServletRequest request,String paramName,int defaultValue) {
        String value = request.getParameter(paramName);
        return StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
    }
    

}
