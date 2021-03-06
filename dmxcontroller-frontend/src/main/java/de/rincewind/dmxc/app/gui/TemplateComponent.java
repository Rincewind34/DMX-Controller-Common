package de.rincewind.dmxc.app.gui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import de.rincewind.dmxc.app.gui.util.Color;
import de.rincewind.dmxc.app.gui.util.FileLoader;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class TemplateComponent extends VBox {
	
	public static final Color BORDER_COLOR = new Color(0x55, 0x55, 0x55);
	
	public static TemplateComponent deserialize(JsonObject object) {
		String type = object.get("type").getAsString();
		JsonElement data = object.get("data");
		TemplateComponent component;
		
		if (type.equals("fader")) {
			component = new Fader(data);
		} else if (type.equals("hardfader")) {
			component = new HardFader(data);
		} else if (type.equals("masterfader")) {
			component = new MasterFader();
		} else if (type.equals("numberpad")) {
			component = new NumberPad(data);
		} else if (type.equals("rgbfader")) {
			component = new RGBFader(data);
		} else if (type.equals("rgbwfader")) {
			component = new RGBWFader(data);
		} else if (type.equals("submasterfader")) {
			component = new SubmasterFader(data);
		} else if (type.equals("effectfader")) {
			component = new EffectFader(data);
		} else if (type.equals("showcontroller")) {
			component = new ShowController();
		} else {
			return null;
		}
		
		component.setCaption(object.get("caption").getAsString());
		return component;
	}
	
	public static ConfigDefaultController loadConfigController(TemplateComponent component) {
		ConfigDefaultController configPane = new ConfigDefaultController();
		FileLoader.loadFXML(configPane, "configs/default-config.fxml");
		configPane.init(component);
		return configPane;
	}
	
	protected static void setBackgroundColor(Pane pane, Color color) {
		pane.setStyle("-fx-background-color: " + TemplateComponent.BORDER_COLOR.toCSS() + ", " + color.toCSS() + ";");
	}
	
	private Template root;
	
	private Label labelCaption;
	private Pane currentContent;
	
	private Image image;
	
	private List<TextField> captionControler;
	
	public TemplateComponent() {
		this.captionControler = new ArrayList<>();
		
		this.labelCaption = new Label("");
		
		this.image = new Image(this.getDragDropImageStream());
		
		this.setSpacing(5.0D);
		this.setAlignment(Pos.CENTER);
		this.setPrefHeight(Region.USE_COMPUTED_SIZE);
		this.setMaxHeight(Double.MAX_VALUE);
	}
	
	public abstract String getType();
	
	public abstract TemplateComponent newOne();
	
	public void update() {
		
	}
	
	public void setContent(TemplateContent content) {
		if (this.currentContent != null) {
			this.removeContent();
			this.removeCaption();
		}
		
		Pane pane = null;
		
		if (content == TemplateContent.CONTROLLER) {
			pane = this.getToolPane();
		} else if (content == TemplateContent.CONFIG) {
			pane = this.getConfigPane();
		} else if (content == TemplateContent.DRAG_DROP) {
			DragDropNode node = DragDropNode.create(this.image);
			Tooltip tooltip = new Tooltip(this.getTooltip());
			Tooltip.install(node.imageView, tooltip);
			pane = node;
		}
		
		if (pane != null) {
			this.currentContent = pane;
			VBox.setVgrow(this.currentContent, Priority.ALWAYS);
			this.addCaption();
			this.addContent();
		}
	}
	
	public void setCaption(String caption) {
		this.labelCaption.setText(caption);
		
		for (TextField field : this.captionControler) {
			field.setText(caption);
		}
	}
	
	public Template getRoot() {
		return this.root;
	}
	
	public Image getDragDropImage() {
		return this.image;
	}
	
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", this.getType());
		object.addProperty("caption", this.labelCaption.getText());
		object.add("data", this.serializeSimplified());
		return object;
	}
	
	protected abstract String getTooltip();
	
	protected abstract JsonElement serializeSimplified();
	
	protected abstract Pane getToolPane();
	
	protected abstract Pane getConfigPane();
	
	protected abstract InputStream getDragDropImageStream();
	
	protected void setRoot(Template template) {
		this.root = template;
	}
	
	protected void bindCaptionField(TextField textCaption) {
		textCaption.setText(this.labelCaption.getText());
		textCaption.textProperty().addListener((observeable, oldValue, newValue) -> {
			this.setCaption(newValue);
		});
		
		this.captionControler.add(textCaption);
	}
	
	private void addCaption() {
		this.getChildren().add(this.labelCaption);
	}
	
	private void addContent() {
		this.getChildren().add(this.currentContent);
	}
	
	private void removeCaption() {
		this.getChildren().remove(this.labelCaption);
	}
	
	private void removeContent() {
		this.getChildren().remove(this.currentContent);
	}
	
	
	public static class ConfigDefaultController extends VBox {
		
		@FXML
		private TextField textCaption;
		
		private void init(TemplateComponent component) {
			component.bindCaptionField(this.textCaption);
		}
		
	}
	
	private static class DragDropNode extends VBox {
		
		private static DragDropNode create(Image image) {
			DragDropNode node = new DragDropNode();
			FileLoader.loadFXML(node, "dragdropnode.fxml");
			node.imageView.setImage(image);
			return node;
		}
		
		@FXML
		private ImageView imageView;
		
	}
	
}
