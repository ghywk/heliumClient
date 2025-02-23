package cc.helium.visual.clickgui.component.components.sub;

import cc.helium.util.render.RenderUtil;
import cc.helium.value.impl.NumberValue;
import cc.helium.visual.clickgui.component.Component;
import cc.helium.visual.clickgui.component.components.Button;
import cc.helium.visual.font.FontManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.BigDecimal;

public class SliderButton extends Component {

	private boolean hovered;

	private final NumberValue set;
	private final Button parent;
	private int offset;
	private int x;
	private int y;
	private boolean dragging = false;

	private double renderWidth;
	
	public SliderButton(NumberValue value, Button button, int offset) {
		this.set = value;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void renderComponent() {
		RenderUtil.rect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 12, hovered ? 0x99000000 : 0x88000000);
        RenderUtil.rect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (int) renderWidth, parent.parent.getY() + offset + 12, 0x88000000);
		if(this.hovered) {
			RenderUtil.rect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (int) renderWidth, parent.parent.getY() + offset + 12, 0x88000000);
		}
		RenderUtil.rect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0x88111111);

		GL11.glPushMatrix();
		GL11.glScalef(0.75f,0.75f, 0.75f);
		FontManager.arial18.drawString(this.hovered ? "§7" + this.set.getName() + " " : this.set.getName() + " ", (parent.parent.getX() * 1.333333333333f + 9), (parent.parent.getY() + offset + 2) * 1.33333333333333f, Color.white.getRGB());
		FontManager.arial18.drawString(this.hovered ? "§7" + this.set.getValue() : String.valueOf(this.set.getValue()), (parent.parent.getX() + 86) * 1.3333333333f - FontManager.arial18.getStringWidth(this.hovered ? "§7" + this.set.getValue() : String.valueOf(this.set.getValue())), (parent.parent.getY() + offset + 2) * 1.3333333333f, Color.white.getRGB());

        GL11.glPopMatrix();
	}

	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
		
		double diff = Math.min(88, Math.max(0, mouseX - this.x));

		double min = set.getMin();
		double max = set.getMax();
		
		renderWidth = (88) * (set.getValue() - min) / (max - min);
		
		if (dragging) {
			if (diff == 0) {
				set.setValue(set.getMin());
			}
			else {
				double difference = diff / 88;
				double newValue = roundToPlace((difference * (max - min) + min), set.getInc());
				set.setValue(newValue);
			}
		}
	}
	
	private static double roundToPlace(double num, double inc) {
		num = (double) Math.round(num * (1.0 / inc)) / (1.0 / inc);
		return num;
    }
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
			dragging = true;
		}
		if(isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
			dragging = true;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}
	
	public boolean isMouseOnButtonD(int x, int y) {
		return x > this.x && x < this.x + (parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 12;
	}
	
	public boolean isMouseOnButtonI(int x, int y) {
		return x > this.x + parent.parent.getWidth() / 2 && x < this.x + parent.parent.getWidth() && y > this.y && y < this.y + 12;
	}

	public static double round(double value, double inc) {
		if (inc == 0.0) {
			return value;
		}
		if (inc == 1.0) {
			return Math.round(value);
		}
		double halfOfInc = inc / 2.0;
		double floored = Math.floor(value / inc) * inc;
		if (value >= floored + halfOfInc) {
			return new BigDecimal(Math.ceil(value / inc) * inc).doubleValue();
		}
		return new BigDecimal(floored).doubleValue();
	}
}
