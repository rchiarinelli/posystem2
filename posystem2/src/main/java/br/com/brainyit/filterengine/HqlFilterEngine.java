package br.com.brainyit.filterengine;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Factory de queries Hibernate. Cria uma query Hibernate a partir de um filtro
 * de objetos.
 * 
 * @see Filter
 * @author Mauricio Nagaoka
 */
public class HqlFilterEngine {

	public static final String PARAM_PREFIX = "param_";

	private Session session;

	private LinkedHashMap<String, Object> params;

	/**
	 * Cria uma inst�ncia da factory baseada na session informada.
	 * 
	 * @param session
	 *            Session do Hibernate a ser utilizada para instanciar as
	 *            queries criadas
	 */
	public HqlFilterEngine(Session session) {
		this.session = session;
		this.params = new LinkedHashMap<String, Object>();
	}

	/**
	 * Cria uma query Hibernate a partir o filtro de objetos informado.
	 * 
	 * @param filtro
	 *            filtro a ser utilizado como base para a query
	 * @return query que obt�m os objetos que atendem � especifica��o dada pelo
	 *         filtro
	 */
	public Query createQuery(Filter filtro) {

		params.clear();

		// HQL
		StringBuilder hql = new StringBuilder();
		hql.append("from ");
		hql.append(filtro.getClazz().getName());
		hql.append(" obj");
		Iterator<FilterArgument> it = filtro.getArguments().iterator();
		if (it.hasNext()) {
			hql.append(" where ");
		}
		while (it.hasNext()) {
			FilterArgument arg = it.next();
			switch (arg.getCondition()) {
			case EQUALS:
			case NOT_EQUALS:
			case LIKE:
				hql.append(processSingleValue(arg));
				break;
			case BETWEEN:
				hql.append(processBetween(arg));
				break;
			case IN:
				hql.append(processIn(arg));
				break;
			}
			if (it.hasNext()) {
				hql.append(" AND ");
			}
		}

		// Ordena��o
		String[] orderByFields = filtro.getOrderByFields();
		if (orderByFields != null) {
			hql.append(" ORDER BY ");
			for (int i = 0; i < orderByFields.length; i++) {
				hql.append(orderByFields[i]);
				if (i+1 < orderByFields.length) {
					hql.append(',');
				}
//				if (i == 0) {
//					hql.append(orderByFields[i]);
//				} else if (i == orderByFields.length - 1) {
//					hql.append(orderByFields[i]);
//					String orderByType = filtro.getOrderByType().getValue();
//					if (orderByType != null) {
//						hql.append(' ').append(orderByType);
//					}
//				} else {
//					hql.append(',');
//					hql.append(orderByFields[i]);
//				}
			}
			hql.append("\t");
			String orderByType = filtro.getOrderByType().getValue();
			hql.append(orderByType);
		}

		Query query = session.createQuery(hql.toString().trim());

		// Valores das cl�usulas
		for (Entry<String, Object> param : params.entrySet()) {
			String key = param.getKey();
			Object value = param.getValue();
			if (value.getClass().isArray()) {
				query.setParameterList(key, (Object[]) value);
			} else {
				query.setParameter(key, value);
			}
		}

		return query;
	}

	private StringBuilder processSingleValue(FilterArgument arg) {
		String paramName = PARAM_PREFIX + params.size();
		StringBuilder sb = new StringBuilder(arg.getField()).append(' ')
				.append(arg.getCondition().getArgValue()).append(" :")
				.append(paramName);
		params.put(paramName, arg.getValues()[0]);
		return sb;
	}

	private StringBuilder processBetween(FilterArgument arg) {
		// Sanity check
		Validate.isTrue(arg.getValues().length == 2,
				"Express�o BETWEEN requer dois valores");

		String paramName = PARAM_PREFIX + params.size();
		StringBuilder sb = new StringBuilder(arg.getField()).append(' ')
				.append(arg.getCondition().getArgValue()).append(" :")
				.append(paramName);
		params.put(paramName, arg.getValues()[0]);
		paramName = PARAM_PREFIX + params.size();
		sb.append(" AND").append(" :").append(paramName);
		params.put(paramName, arg.getValues()[1]);
		return sb;
	}

	private StringBuilder processIn(FilterArgument arg) {
		String paramName = PARAM_PREFIX + params.size();
		StringBuilder sb = new StringBuilder(arg.getField()).append(' ')
				.append(arg.getCondition().getArgValue()).append(" (:")
				.append(paramName).append(')');
		params.put(paramName, arg.getValues());
		return sb;
	}
}
