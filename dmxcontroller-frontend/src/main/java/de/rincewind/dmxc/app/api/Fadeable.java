package de.rincewind.dmxc.app.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Fadeable {
	
	public static Fadeable deserialize(JsonObject object) {
		if (!object.has("type")) {
			return null;
		}
		
		String type = object.get("type").getAsString();
		JsonElement data = object.get("data");
		Fadeable fadeable = null;
		
		if (type.equals("master")) {
			fadeable = Master.instance();
		} else if (type.equals("channel")) {
			fadeable = Channel.fromJson(data);
		} else if (type.equals("channelselection")) {
			fadeable = new ChannelSelection(data);
		} else if (type.equals("submaster")) {
			fadeable = Submaster.get(data);
		} else if (type.equals("effect")) {
			fadeable = Effect.get(data);
		} else if (type.equals("show")) {
			fadeable = Show.instance();
		}
		
		return fadeable;
	}
	
	
	public abstract void update(Short value);
	
	public abstract String getType();
	
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", this.getType());
		object.add("data", this.serializeSimplified());
		return object;
	}
	
	protected abstract JsonElement serializeSimplified();
	
}
