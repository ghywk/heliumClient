package cc.helium.visual.clickgui.component;

import cc.helium.Client;
import cc.helium.module.Category;
import cc.helium.module.Module;
import cc.helium.module.impl.render.ClickGUI;
import cc.helium.util.render.RenderUtil;
import cc.helium.visual.clickgui.component.components.Button;
import cc.helium.visual.font.FontUtil;

import java.awt.*;
import java.util.ArrayList;

public class Frame {

	public final ArrayList<Component> components;
	public final Category category;
	public boolean open;
	public final int width;
	public int y;
	public int x;
	public final int barHeight;
	private boolean isDragging;
	public int dragX;
	public int dragY;

	public Frame(Category cat) {
		this.components = new ArrayList<>();
		this.category = cat;
		this.width = 88;
		this.x = 5;
		this.y = 5;
		this.barHeight = 13;
		this.dragX = 0;
		this.open = false;
		this.isDragging = false;
		int tY = this.barHeight;

		for (Module mod : Client.getInstance().moduleManager.getModulesInCategory(category)) {
			Button modButton = new Button(mod, this, tY);
			this.components.add(modButton);
			tY += 12;
		}
	}

	public ArrayList<Component> getComponents() {
		return components;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void renderFrame() {
		ClickGUI cgui = Client.getInstance().moduleManager.getModule(ClickGUI.class);
		int color = new Color(cgui.red.getValue().intValue(), cgui.green.getValue().intValue(), cgui.blue.getValue().intValue(), cgui.blue.getValue().intValue()).getRGB();
		RenderUtil.rect(this.x - 2, this.y - 2, this.x + this.width + 2, this.y + this.barHeight, color);

		FontUtil.drawTotalCenteredStringWithShadowVerdana(this.category.name(), (this.x + (float) this.width / 2), (this.y + 7) - 3, Color.WHITE);

        for (Component component : this.components) {
			if (this.open) {
				if (!this.components.isEmpty()) {
					component.renderComponent();
				}
			}
		}
	}

	public void refresh() {
		int off = this.barHeight;
		for (Component comp : components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}


	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public void updatePosition(int mouseX, int mouseY) {
		if (this.isDragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
		}
	}

	public boolean isWithinHeader(int x, int y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
	}
}
