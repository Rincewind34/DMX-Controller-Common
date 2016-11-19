package de.rincewind.dmxc.system.environment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.rincewind.dmxc.system.Main;
import de.rincewind.dmxc.system.network.Client;

public class DMXData {
	
	private Map<Client, Short> datas;
	
	public DMXData() {
		this.datas = new HashMap<>();
	}
	
	public void update(Client client, Short value) {
		if (value == null) {
			this.datas.remove(client);
		} else {
			this.datas.put(client, value);
		}
	}
	
	public void removeClient(Client client) {
		this.datas.remove(client);
	}
	
	public Short getCurrentValue() {
		if (Main.environment().getMergingMethod() == MergingMethod.FIRST_LOGIN) {
			Client target = null;
			
			for (Client client : this.datas.keySet()) {
				if (this.datas.get(client) == null) {
					continue;
				}
				
				if (target == null || client.isBefore(target)) {
					target = client;
				}
			}
			
			if (target == null) {
				return null;
			} else {
				return this.datas.get(target);
			}
		} else {
			short current = -1;
			
			for (short value : this.datas.values()) {
				if (value > current) {
					current = value;
				}
			}
			
			if (current == -1) {
				return null;
			}
			
			return current;
		}
	}
	
	public Map<Client, Short> getDatas() {
		return Collections.unmodifiableMap(this.datas);
	}
	
}
