package org.zerock.myapp.entity;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

@Data
public class ShopperProductId implements Serializable{
	@Serial private static final long serialVersionUID = 1L;
	
	private Long shopper;
	private Long product;
} // end class
