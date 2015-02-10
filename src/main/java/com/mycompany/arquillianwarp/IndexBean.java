/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.arquillianwarp;

import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author kikuta
 */
@Named(value = "indexBean")
@Dependent
public class IndexBean {

    /**
     * Creates a new instance of IndexBean
     */
    public IndexBean() {
    }
    
}
