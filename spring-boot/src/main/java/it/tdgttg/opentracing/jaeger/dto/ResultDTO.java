package it.tdgttg.opentracing.jaeger.dto;

public class ResultDTO {

	private Integer value;
	
	public ResultDTO(Integer value) {
		super();
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}	
	
}
