/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.dialect;

import java.sql.Types;
import org.hibernate.dialect.MySQL5Dialect;

/**
 *
 * @author ildiko
 */
public class Mysql5BitBooleanDialect extends MySQL5Dialect{     
    public Mysql5BitBooleanDialect() {
        super();
        registerColumnType( Types.BOOLEAN, "bit" );     
    }       
}

