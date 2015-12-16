package eu.hcomb.rrouter.dto;

public class RouteDTO {

	protected Long id;
	
	protected Boolean active;

	protected String fromInstanceName;
	protected String fromKey;
	protected String fromType;
	protected EndpointDTO from;
		
	protected String toInstanceName;
	protected String toKey;
	protected String toType;
	protected EndpointDTO to;
	
	public void setFromInstanceName(String fromInstanceName) {
		this.fromInstanceName = fromInstanceName;
	}
	public void setFromKey(String fromKey) {
		this.fromKey = fromKey;
	}
	public void setFromType(String fromType) {
		this.fromType = fromType;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public void setToInstanceName(String toInstanceName) {
		this.toInstanceName = toInstanceName;
	}
	public void setToKey(String toKey) {
		this.toKey = toKey;
	}
	public void setToType(String toType) {
		this.toType = toType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public EndpointDTO getFrom() {
		return from;
	}
	public void setFrom(EndpointDTO from) {
		this.from = from;
	}
	public EndpointDTO getTo() {
		return to;
	}
	public void setTo(EndpointDTO to) {
		this.to = to;
	}
	public void fix() {
		EndpointDTO from = new EndpointDTO();
		from.setInstance(fromInstanceName);
		from.setKey(fromKey);
		from.setType(fromType);
		setFrom(from);
		setFromInstanceName(null);
		setFromKey(null);
		setFromType(null);

		EndpointDTO to = new EndpointDTO();
		to.setInstance(toInstanceName);
		to.setKey(toKey);
		to.setType(toType);
		setTo(to);
		setToInstanceName(null);
		setToKey(null);
		setToType(null);
	}	
}
