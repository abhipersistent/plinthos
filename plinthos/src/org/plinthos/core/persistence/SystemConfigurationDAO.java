package org.plinthos.core.persistence;

import java.util.Date;
import java.util.List;

import org.plinthos.core.model.SystemConfigurationProperty;

public interface SystemConfigurationDAO extends GenericDAO<SystemConfigurationProperty, Integer> {

	public List<SystemConfigurationProperty> findModifiedSinceLastRefresh(Date lastRefreshDate);	
	
}
