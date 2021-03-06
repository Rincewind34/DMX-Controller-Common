package de.rincewind.dmxc.app.gui;

import java.io.InputStream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import de.rincewind.dmxc.app.api.Fadeable;
import de.rincewind.dmxc.app.gui.util.FaderBase;
import de.rincewind.dmxc.app.gui.util.FileLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class HardFader extends TemplateComponent {
	
	private ToolController toolController;
	
	public HardFader() {
		this((Fadeable) null);
	}
	
	public HardFader(Fadeable target) {
		this.setCaption("Hard-Fader");
		this.toolController = new ToolController();
		
		FileLoader.loadFXML(this.toolController, "hard-fader.fxml");
		this.toolController.init();
		this.faderBase().setTarget(target);
	}
	
	protected HardFader(JsonElement element) {
		this(Fadeable.deserialize(element.getAsJsonObject()));
	}
	
	@Override
	public String getType() {
		return "hardfader";
	}
	
	@Override
	public TemplateComponent newOne() {
		return new HardFader();
	}
	
	public FaderBase faderBase() {
		return this.toolController.base;
	}
	
	@Override
	protected String getTooltip() {
		return "Effects";
	}
	
	@Override
	protected JsonElement serializeSimplified() {
		if (this.faderBase().getTarget() != null) {
			return this.faderBase().getTarget().serialize();
		} else {
			return new JsonObject();
		}
	}
	
	@Override
	protected Pane getToolPane() {
		return this.toolController;
	}
	
	@Override
	protected Pane getConfigPane() {
		return new Pane();
	}
	
	@Override
	protected InputStream getDragDropImageStream() {
		return FileLoader.getImageStream("faders");
	}
	
	
	private static class ToolController extends HBox {
		
		@FXML
		private Slider fader;
		
		@FXML
		private ToggleButton buttonPushZero;
		
		@FXML
		private ToggleButton buttonSaveMode;
		
		@FXML
		private Button button1;
		
		@FXML
		private Button button2;
		
		@FXML
		private Button button3;
		
		@FXML
		private Button button4;
		
		@FXML
		private Button button5;
		
		private double save1;
		private double save2;
		private double save3;
		private double save4;
		private double save5;
		
		private FaderBase base;
		
		private void init() {
			this.button1.setOnAction((event) -> {
				if (this.buttonSaveMode.isSelected()) {
					this.save1 = this.fader.getValue();
					this.buttonSaveMode.setSelected(false);
				} else {
					this.fader.setValue(this.save1);
				}
			});
			
			this.button2.setOnAction((event) -> {
				if (this.buttonSaveMode.isSelected()) {
					this.save2 = this.fader.getValue();
					this.buttonSaveMode.setSelected(false);
				} else {
					this.fader.setValue(this.save2);
				}
			});
			
			this.button3.setOnAction((event) -> {
				if (this.buttonSaveMode.isSelected()) {
					this.save3 = this.fader.getValue();
					this.buttonSaveMode.setSelected(false);
				} else {
					this.fader.setValue(this.save3);
				}
			});
			
			this.button4.setOnAction((event) -> {
				if (this.buttonSaveMode.isSelected()) {
					this.save4 = this.fader.getValue();
					this.buttonSaveMode.setSelected(false);
				} else {
					this.fader.setValue(this.save4);
				}
			});
			
			this.button5.setOnAction((event) -> {
				if (this.buttonSaveMode.isSelected()) {
					this.save5 = this.fader.getValue();
					this.buttonSaveMode.setSelected(false);
				} else {
					this.fader.setValue(this.save5);
				}
			});
			
			this.base = new FaderBase(this.fader, null, this.buttonPushZero);
		}
		
	}
	
}
