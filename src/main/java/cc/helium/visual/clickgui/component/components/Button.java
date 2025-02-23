package cc.helium.visual.clickgui.component.components;

import cc.helium.module.Module;
import cc.helium.util.render.RenderUtil;
import cc.helium.value.Value;
import cc.helium.value.impl.BoolValue;
import cc.helium.value.impl.ModeValue;
import cc.helium.value.impl.NumberValue;
import cc.helium.visual.clickgui.ClickGui;
import cc.helium.visual.clickgui.component.Component;
import cc.helium.visual.clickgui.component.Frame;
import cc.helium.visual.clickgui.component.components.sub.CheckboxButton;
import cc.helium.visual.clickgui.component.components.sub.ModeButton;
import cc.helium.visual.clickgui.component.components.sub.SliderButton;
import cc.helium.visual.font.FontManager;

import java.awt.*;
import java.util.ArrayList;

public class Button extends Component {
	public final Module mod;
	public final Frame parent;
	public int offset;
	private boolean isHovered;
	public final ArrayList<Component> subcomponents;
	public boolean open;
	public final int height;
	
	public Button(Module mod, Frame parent, int offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<>();
		this.open = false;
		height = 12;
		int opY = offset + height;
		if (!mod.getValues().isEmpty()) {
			for(Value<?> s : mod.getValues()){
				if (s instanceof ModeValue) {
					this.subcomponents.add(new ModeButton((ModeValue) s, this, opY));
					opY += height;
				}
				if (s instanceof NumberValue) {
					this.subcomponents.add(new SliderButton((NumberValue) s, this, opY));
					opY += height;
				}
				if (s instanceof BoolValue) {
					this.subcomponents.add(new CheckboxButton((BoolValue) s, this, opY));
					opY += height;
				}
			}
		}
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
		int opY = offset + height;
		for(Component comp : this.subcomponents) {
			comp.setOff(opY);
			opY += height;
		}
	}

	@Override
	public void renderComponent() {
		RenderUtil.rect(parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + height + this.offset, 0x33000000);
		RenderUtil.rect(parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + height + this.offset, 0x33000000);

		if(this.mod.isEnable() && this.isHovered) {
			RenderUtil.rect(parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + height + this.offset, 0x20000000);
		}

		if(this.mod.isEnable()) {
			RenderUtil.rect(parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + height + this.offset, 0x40000000);
		}

		if(this.isHovered) {
			RenderUtil.rect(parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + height + this.offset, 0x30000000);
		}

		String text = this.mod.isEnable() ? this.mod.getName() : this.isHovered ? "§7" + this.mod.getName() : "§f" + this.mod.getName();
		float x = parent.getX() + (float) parent.getWidth() / 2;
		int y = (parent.getY() + offset + 7) - 2;
		FontManager.sf_light18.drawString(text, x - (double) FontManager.sf_light18.getStringWidth(text) / 2, y - FontManager.sf_light18.getHeight() / 2F, new Color(255, 233, 181).getRGB());

		if(this.open) {
			if (!this.subcomponents.isEmpty()) {
				for (Component comp : this.subcomponents) {
					comp.renderComponent();
				}
				RenderUtil.rect(parent.getX() + 2, parent.getY() + this.offset + height, parent.getX() + 3, parent.getY() + this.offset + ((this.subcomponents.size() + 1) * height), ClickGui.color);
			}
		}
	}
	
	@Override
	public int getHeight() {
		if(this.open) {
			return (height * (this.subcomponents.size() + 1));
		}
		return height;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.isHovered = isMouseOnButton(mouseX, mouseY);
		if(!this.subcomponents.isEmpty()) {
			for(Component comp : this.subcomponents) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0) {
			this.mod.toggle();
		}
		if(isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			this.parent.refresh();
		}
		for(Component comp : this.subcomponents) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(Component comp : this.subcomponents) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int key) {
		for(Component comp : this.subcomponents) {
			comp.keyTyped(typedChar, key);
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		return x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + height + this.offset;
	}
}
