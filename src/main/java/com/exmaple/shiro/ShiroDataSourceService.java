/**
 * 
 */

package com.exmaple.shiro;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;


/**
 * @author k.horri
 * 
 */
public class ShiroDataSourceService extends BoneCPDataSource {

    /**
     * 
     */
    private static final long serialVersionUID = -2248923051363839327L;

    /**
     * 
     */
    public ShiroDataSourceService() {

	super();
	this.setDriverClass("com.mysql.jdbc.Driver");
	this.setJdbcUrl("jdbc:mysql://localhost:3306/shiro");
	this.setUsername("root");
	this.setPassword("root");
	this.setMaxConnectionsPerPartition(10);

    }

    /**
     * @param config
     */
    public ShiroDataSourceService(BoneCPConfig config) {

	super(config);

    }

}
