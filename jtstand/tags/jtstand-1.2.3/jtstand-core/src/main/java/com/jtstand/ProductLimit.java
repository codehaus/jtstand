/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestLimit.java is part of JTStand.
 *
 * JTStand is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JTStand is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author albert_kurucz
 */
@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"product_id", "name"}))
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class ProductLimit extends TestLimit implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @ManyToOne
    private Product product;

    @XmlTransient
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            setCreator(product.getCreator());
        }
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash += (product != null ? product.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductLimit)) {
            return false;
        }
        ProductLimit other = (ProductLimit) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.product == null && other.getProduct() != null) || (this.product != null && !this.product.equals(other.getProduct()))) {
            return false;
        }
        return true;
    }
}
