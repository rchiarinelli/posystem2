/**
 * 
 */
package br.com.brainyit.filterengine;

import org.apache.log4j.Logger;

/**
 * @author Rafael Chiarinelli
 *
 */
@Deprecated
public class FilterEngine {

	private static Logger logger = Logger.getLogger(FilterEngine.class);
	
	private static FilterEngine instance;
	
	/**
	 * Recupera a inst&acirc;ncia singleton do filter
	 * @return
	 */
	public synchronized static FilterEngine getInstance(){
		if (instance==null) {
			instance = new FilterEngine();
		}
		return new FilterEngine();
	}
	
	/**
	 * Realiza o parse do filtro, retornando o HQL
	 * para realizar a query.
	 * 
	 * @return
	 */
	public String parseFilter(Filter filter){
		logger.debug("Gerando filtro.");
		StringBuilder query = new StringBuilder();
		query.append("from\t");
		query.append(filter.getClazz().getName());
		query.append("\t");
		query.append("obj\t");
		//Caso houver argumentos, aplicar filtro
		if (!filter.getArguments().isEmpty()){
			query.append("where\t");
			boolean hasArgs = false;
			Object[] values = null;
			boolean hasValues = false; 
			int i = 0;
			for (FilterArgument arg : filter.getArguments()) {
				if (arg.getCondition()==FilterCondition.EQUALS){
					if (hasArgs){
						query.append("and\t");
					} else {
						hasArgs=true;
					}
					hasValues=false;
					query.append(arg.getField());
					query.append("\t");
					query.append(arg.getCondition().getArgValue());
					query.append("(");
					values = arg.getValues();
					for (i = 0; i < values.length; i++) {
						if (hasValues) {
							query.append(",");	
						} else {
							hasValues = true;
						}
						query.append(arg.getValueType().getValueChar());
						query.append(values[i]);						
						query.append(arg.getValueType().getValueChar());
					}
					query.append(")\t");
				} else if (arg.getCondition()==FilterCondition.NOT_EQUALS){
					if (hasArgs){
						query.append("and\t");
					} else {
						hasArgs=true;
					}
					hasValues=false;
					query.append(arg.getField());
					query.append("\t");
					query.append(arg.getCondition().getArgValue());
					query.append("(");
					values = arg.getValues();
					for (i = 0; i < values.length; i++) {
						if (hasValues) {
							query.append(",");	
						} else {
							hasValues = true;
						}
						query.append(arg.getValueType().getValueChar());
						query.append(values[i]);						
						query.append(arg.getValueType().getValueChar());
					}
					query.append(")\t");					
				} else if (arg.getCondition()==FilterCondition.BETWEEN){
					if (hasArgs){
						query.append("and\t");
					} else {
						hasArgs=true;
					}
					hasValues=false;
					query.append(arg.getField());
					query.append("\t");
					query.append(arg.getCondition().getArgValue());
					query.append("\t");
					values = arg.getValues();
					for (i = 0; i < values.length; i++) {
						if (hasValues) {
							query.append(" and ");	
						} else {
							hasValues = true;
						}
						query.append(arg.getValueType().getValueChar());
						query.append(values[i]);						
						query.append(arg.getValueType().getValueChar());
					}
					query.append("\t");					
				} else if (arg.getCondition()==FilterCondition.IN){
					if (hasArgs){
						query.append("and\t");
					} else {
						hasArgs=true;
					}
					hasValues=false;
					query.append(arg.getField());
					query.append("\t");
					query.append(arg.getCondition().getArgValue());
					query.append("(");
					values = arg.getValues();
					for (i = 0; i < values.length; i++) {
						if (hasValues) {
							query.append(",");	
						} else {
							hasValues = true;
						}
						query.append(arg.getValueType().getValueChar());
						query.append(values[i]);						
						query.append(arg.getValueType().getValueChar());
					}
					query.append(")\t");					
				} else if (arg.getCondition()==FilterCondition.LIKE){
					if (hasArgs){
						query.append("and\t");
					} else {
						hasArgs=true;
					}
					hasValues=false;
					query.append(arg.getField());
					query.append("\t");
					query.append(arg.getCondition().getArgValue());
					query.append("(");
					values = arg.getValues();
					for (i = 0; i < values.length; i++) {
						if (hasValues) {
							query.append(",");	
						} else {
							hasValues = true;
						}
						query.append(arg.getValueType().getValueChar());
						query.append(values[i]);						
						query.append(arg.getValueType().getValueChar());
					}
					query.append(")\t");					
				} 
			}
		}
		//Adicionar a clausula order by se estiver setada
		if (filter.getOrderByFields()!=null && filter.getOrderByFields().length>0) {
			query.append("\t");
			query.append("order by");
			query.append("\t");
			boolean hasField = false;
			for (String fieldName : filter.getOrderByFields()) {
				if (hasField) {
					query.append(",");
				} 
				query.append(fieldName);
				hasField = true;
			}
			//adicionar o tipo de ordenacao, se estiver setado
			query.append("\t");
			if (filter.getOrderByType()!=null) {
				query.append(filter.getOrderByType().getValue());
			}
			query.append("\t");
		}
		return query.toString();
	}
}
