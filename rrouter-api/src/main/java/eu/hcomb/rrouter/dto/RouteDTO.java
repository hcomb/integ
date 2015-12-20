package eu.hcomb.rrouter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RouteDTO {

	protected Long id;
	
	protected Boolean active;

	@JsonProperty("event")
	protected String eventName;
	
	protected Long fromId;
	protected String fromInstanceName;
	protected String fromKey;
	protected String fromType;
	protected EndpointDTO from;
		
	protected Long toId;
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
		from.setId(fromId);
		from.setInstance(fromInstanceName);
		from.setKey(fromKey);
		from.setType(fromType);
		setFrom(from);
		setFromId(null);
		setFromInstanceName(null);
		setFromKey(null);
		setFromType(null);

		EndpointDTO to = new EndpointDTO();
		to.setId(toId);
		to.setInstance(toInstanceName);
		to.setKey(toKey);
		to.setType(toType);
		setTo(to);
		setToId(null);
		setToInstanceName(null);
		setToKey(null);
		setToType(null);
	}
	public void setFromId(Long fromId) {
		this.fromId = fromId;
	}
	public void setToId(Long toId) {
		this.toId = toId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}	
}
