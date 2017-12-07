/**
 * 
 */
package br.com.brainyit.posystem2.restful.util;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.com.brainyit.posystem2.exception.RestBeanConvertionException;
import br.com.brainyit.posystem2.restbean.RestBean;

/**
 * Classe utilitaria para converter classes {@link RestBean}, diretamente
 * para classes JSON. Usada principalmente quando é preciso gerar respostas
 * JSON para os componentes DOJO.
 * 
 * @author Rafael Chiarinelli
 *
 */
public class JSONViewHelper {
	
	private static Logger logger = Logger.getLogger(JSONViewHelper.class);
	/**
	 * Converte um {@link List} de RestBean, para um JSON com um array, no seguinte formato:
	 * {"items":[key1:value1,key2:value2....]}.
	 * @param items
	 * @return
	 */
	public synchronized JSONGridObject convertBeanListToJSONGridItems(List<? extends RestBean> items) throws RestBeanConvertionException{
		JSONGridObject jsonResponse = new JSONGridObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject entry = null;
		for (RestBean restBean : items) {
			entry = this.convertBeanToJSON(restBean);
			jsonArray.put(entry);
		}
		jsonResponse.setItems(jsonArray);
		return jsonResponse;
	}
	
	public synchronized JSONArray convertBeanListToJSONArray(Collection<? extends RestBean> items) throws RestBeanConvertionException{
		JSONArray jsonArray = new JSONArray();
		JSONObject entry = null;
		for (RestBean restBean : items) {
			entry = this.convertBeanToJSON(restBean);
			jsonArray.put(entry);
		}
		return jsonArray;
	}
	
	/**
	 * Converte o {@link ViewBean} passado para um objeto JSON, no seguinte formato:
	 * {key1:value1,key2:value2...}
	 * 
	 * @param items
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized JSONObject convertBeanToJSON(RestBean item) throws RestBeanConvertionException{
		CustomJSONObject jsonResponse = null;
		try {
			//recuperar os getter da classe
			jsonResponse = new CustomJSONObject();
			//Map<String,String> beanDescription = BeanUtils.describe(item);
			PropertyUtilsBean p = new PropertyUtilsBean();
			PropertyDescriptor[] propsDesc = p.getPropertyDescriptors(item);
			for (PropertyDescriptor pDesc : propsDesc) {
				if (!pDesc.getName().equals("class")) { 
					jsonResponse.put(pDesc.getName(),p.getProperty(item, pDesc.getName()));					
				}
			}
		} catch (JSONException exc) {
			logger.error(exc);
			throw new RestBeanConvertionException(exc);
		} catch (Exception exc) {
			logger.equals(exc);
			throw new RestBeanConvertionException(exc);
		}
		return jsonResponse;
	}
	
	/**
	 * Converte o Object passado para um objeto JSON, no seguinte formato:
	 * {key1:value1,key2:value2...}
	 * 
	 * @param items
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized JSONObject convertBeanToJSON(Object item) throws RestBeanConvertionException{
		JSONObject jsonResponse = null;
		try {
			//recuperar os getter da classe
			jsonResponse = new JSONObject();
			Map<String,String> beanDescription = BeanUtils.describe(item);
			Set<String> keys = beanDescription.keySet();
			for (String k : keys) {
				if (!k.equals("class") && k.contains("get")) {
					jsonResponse.put(k
								, beanDescription.get(k));
				}
			}
		} catch (JSONException exc) {
			logger.error(exc);
			throw new RestBeanConvertionException(exc);
		} catch (Exception exc) {
			logger.equals(exc);
			throw new RestBeanConvertionException(exc);
		}
		return jsonResponse;
	}
	

	
	
}
