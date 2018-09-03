package com.cffex.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data @NoArgsConstructor @EqualsAndHashCode @ToString
public class ResponseResult implements Serializable {
	private static final long serialVersionUID = 1L;
	protected final String version = "1.0";
	//0--correct
	protected int errorCode = 0;
	protected String errorMsg = null;
	protected Object data = "";
}
